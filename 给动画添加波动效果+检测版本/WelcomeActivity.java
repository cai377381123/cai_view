package com.fanyu.infopool;

import java.util.List;

import org.json.JSONException;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.fanyu.home.im.ActivitySupport;
import com.fanyu.home.im.manager.XmppConnectionManager;
import com.fanyu.home.im.model.LoginConfig;
import com.fanyu.home.im.task.LoginTaskXMPP;
import com.fanyu.home.main1.service.mapservice;
import com.fanyu.infopool.app.Preferences;
import com.fanyu.infopool.common.Constant;
import com.fanyu.infopool.funs.Configuration;
import com.fanyu.infopool.funs.Funs;
import com.fanyu.infopool.funs.FunsApplication;
import com.fanyu.infopool.funs.Login;
import com.fanyu.infopool.http.HttpAuthException;
import com.fanyu.infopool.http.HttpException;
import com.fanyu.infopool.http.ResponseException;
import com.fanyu.infopool.service.UpdateService;
import com.fanyu.infopool.task.GenericTask;
import com.fanyu.infopool.task.TaskAdapter;
import com.fanyu.infopool.task.TaskFeedback;
import com.fanyu.infopool.task.TaskListener;
import com.fanyu.infopool.task.TaskParams;
import com.fanyu.infopool.task.TaskResult;
import com.fanyu.rest_home.R;

public class WelcomeActivity extends ActivitySupport {

	private final String TAG = "WelcomeAct";
	private View mWelcome;
	private boolean istrue = true;

	private SharedPreferences mPreferences;
	private String count;
	public static String versionString;

	private boolean mLogout = false;
	private boolean mVersion = true;
	private boolean mService = false;
	boolean is_auto_check = false;

	private String mUsername;
	private String mPassword;

	private GenericTask mLoginTask;

	private GenericTask mCheckVersionTask;

	private LoginConfig loginConfig;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.sports_welcome, null);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		/** 设置透明度渐变动画 */
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		// 设置动画持续时间
		animation.setDuration(1000);
		view.setAnimation(animation);
		setContentView(view);
		versionString = FunsApplication.localVersion;
		// 获取count值，判断是否第一次进入应用
		count = mPreferences.getString("count", "0");

		// 是否自动登录，登录过一次以后，则为自动登录
		is_auto_check = mPreferences.getBoolean(Preferences.IS_AUTO_CHECK,
				false);
		mUsername = mPreferences.getString(Preferences.USERNAME_KEY, "");
		mPassword = mPreferences.getString(Preferences.PASSWORD_KEY, "");

		System.out.println("mUsername===============" + mUsername);
		// author dxf
		// 如果登录过则自动登录，否则进入游客模式
		loginConfig = getLoginConfig();
		ActivityManager activityManger = (ActivityManager) getSystemService(ACTIVITY_SERVICE);// 获取Activity管理器
		List<ActivityManager.RunningServiceInfo> serviceList = activityManger.getRunningServices(99999);// 从窗口管理器中获取正在运行的Service
		String res = getServicesName(serviceList);
		System.out.println("sercice size==="+serviceList.size());
		mService = ServiceIsStart(serviceList,"com.fanyu.infopool.service.UpdateService");
		if (is_auto_check && mVersion) {
			if (!TextUtils.isEmpty(Configuration.ip)) {
				if(mService==false){
					doCheckVersion();// 检测apk版本
				}
				doAutoLogin(is_auto_check);
			}
		} else {
			new Thread(new Thread1()).start();
		}

	}
	
	/**
	 * 
	 * @param list
	 * @param className
	 * @return
	 */
	private boolean ServiceIsStart(List<ActivityManager.RunningServiceInfo> list, String className) {// 判断某个服务是否启动
		for (int i = 0; i < list.size(); i++) {
			if (className.equals(list.get(i).service.getClassName()))
				return true;
		}
		return false;
	}
	
	/**
	 * // 获取所有服务的名称
	 * @param list
	 * @return
	 */
	private String getServicesName(List<ActivityManager.RunningServiceInfo> list) {
		String res = "";
		for (int i = 0; i < list.size(); i++) {
			res += list.get(i).service.getClassName() + "/n";
		}
		return res;
	}

	class Thread1 implements Runnable {
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (istrue) {
				// 第一次进入应用则进入导航界面
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, LoginActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();

			}
		}
	}

	@Override
	protected boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	// 检测服务器apk的版本号
	private void doCheckVersion() {
		if (mCheckVersionTask != null
				&& mCheckVersionTask.getStatus() == GenericTask.Status.RUNNING) {
			Log.d(TAG, "check version task running.");
			return;
		} else {
			mCheckVersionTask = new CheckVersionTask();
			mCheckVersionTask.setListener(mCheckVersionTaskListener);
			TaskParams params = new TaskParams();
			mCheckVersionTask.execute(params);
		}
	}

	private class CheckVersionTask extends GenericTask {

		private String msg = getString(R.string.xiazai_please_check_server_error);

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				String versionString = FunsApplication.mFuns.getVersion();
				String serverVersion = versionString.substring(1,
						versionString.length() - 1);
				Log.d(TAG, "server version:" + serverVersion);

				FunsApplication.serverVersion = serverVersion;

				mVersion = false;

				return TaskResult.OK;
			} catch (ResponseException e) {
				Log.e(TAG, e.getMessage());
				return TaskResult.FAILED;
			} catch (HttpException e) {
				Log.e(TAG, e.getMessage());
				return TaskResult.FAILED;
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				return TaskResult.FAILED;
			} catch (RuntimeException e) {
				Log.e(TAG, e.getMessage());
				return TaskResult.FAILED;
			}

		}

		public String getMsg() {
			return this.msg;
		}

	}

	/**
	 * 检测版本
	 */
	private TaskListener mCheckVersionTaskListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			onCheckVersionBegin();
		}

		private void onCheckVersionBegin() {
			// 提示检测服务联通状态
			TaskFeedback.getInstance(TaskFeedback.DIALOG_MODE,
					WelcomeActivity.this).start(
					getString(R.string.xiazai_please_check_servering));
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {

		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			// if (result == TaskResult.OK) {
			onCheckVersionSuccess();
			// } else {
			// onCheckVersionFailure(((CheckVersionTask) task).getMsg());
			// }
		}

		private void onCheckVersionFailure(String msg) {
			// TaskFeedback.getInstance(TaskFeedback.DIALOG_MODE,
			// LoginActivity.this)
			// .failed(msg);
			//
			// Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
		}

		private void onCheckVersionSuccess() {
			TaskFeedback.getInstance(TaskFeedback.DIALOG_MODE,
					WelcomeActivity.this).success("");
			checkVersion();

			// doAutoLogin(is_auto_check);
		}

		@Override
		public String getName() {
			return "check version";
		}
	};

	/**
	 * 
	 * 自动登录
	 * 
	 * @param is_auto_check
	 */
	private void doAutoLogin(boolean is_auto_check) {
		System.out.println("mUsername===============" + mUsername + mPassword);
		if (is_auto_check && !mLogout) {
			if (!TextUtils.isEmpty(mUsername) && !TextUtils.isEmpty(mPassword)) {
				doLogin();

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 初始化xmpp配置

		XmppConnectionManager.getInstance().init(loginConfig);
	}

	/**
	 * 检查是否更新版本
	 */
	private boolean checkVersion() {
		Log.d(TAG, "check version ...");
		System.out.println(FunsApplication.localVersion + "----------------"
				+ FunsApplication.serverVersion);
		if (Double.parseDouble(FunsApplication.localVersion) < Double
				.parseDouble(FunsApplication.serverVersion)) {
			// 发现新版本，提示用户更新
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("软件升级")
					.setMessage("发现新版本,建议立即更新使用.")
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent updateIntent = new Intent(
											WelcomeActivity.this,
											UpdateService.class);
									updateIntent
											.putExtra(
													Constant.APP_NAME,
													"oldage"
															+ "_"
															+ FunsApplication.serverVersion);
									startService(updateIntent);
									next();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									next();
								}
							});
			alert.create().show();

			return true;

		} else {
			next();
			return false;
		}
	}

	private void next() {
		/**
		 * 检测成功网络成功完成后，如果自动登录标识为真，则进入自动登录模式
		 */
		// author dxf
		// 如果登录过则自动登录，否则进入游客模式
		// if (is_auto_check) {
		//
		// doAutoLogin(is_auto_check);
		//
		// } else {
		// new Thread(new Thread1()).start();
		// }
	}

	private void loginXMPP() {
		System.out.println(mPassword + mUsername);
		loginConfig.setPassword(mPassword);
		loginConfig.setUsername(mUsername);
		loginConfig.setNovisible(false);

		LoginTaskXMPP loginTask = new LoginTaskXMPP(WelcomeActivity.this,
				loginConfig);
		loginTask.execute();
	}

	private void doLogin() {
		if (mLoginTask != null
				&& mLoginTask.getStatus() == GenericTask.Status.RUNNING) {
			Log.d(TAG, "login task running.");
			return;
		} else {
			if (!TextUtils.isEmpty(mUsername) & !TextUtils.isEmpty(mPassword)) {
				mLoginTask = new LoginTask();
				mLoginTask.setListener(mLoginTaskListener);

				TaskParams params = new TaskParams();
				params.put(Preferences.USERNAME_KEY, mUsername.trim());
				params.put(Preferences.PASSWORD_KEY, mPassword.trim());
				Log.d(TAG, "login username:" + mUsername + ",password:"
						+ mPassword);
				mLoginTask.execute(params);
			} else {
				updateProgress(getString(R.string.login_status_null_username_or_password));

			}
		}
	}

	private class LoginTask extends GenericTask {

		private String msg = getString(R.string.login_status_failure);

		public String getMsg() {
			return msg;
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			TaskParams param = params[0];
			publishProgress(getString(R.string.login_status_logging_in) + "...");

			SharedPreferences.Editor editor = mPreferences.edit();

			try {
				Thread.sleep(2 * 1000);
				String username = param.getString(Preferences.USERNAME_KEY);
				String password = param.getString(Preferences.PASSWORD_KEY);
				Login login = FunsApplication.mFuns.login(username, password);
				if (login.getStatus() == 0) {
					msg = getString(R.string.login_status_invalid_username_or_password);
					publishProgress(msg);
					return TaskResult.FAILED;
				}
				if (login.getStatus() == 1) {
					editor.putString(Preferences.USER_ID, login.getId());
					editor.putString(Preferences.OLDID, login.getOldid());
					editor.putString(Preferences.USER_IMG, login.getUserimg());
					editor.putString(Preferences.NICKNAME, login.getNickname());
					editor.putString(Preferences.USER_TYPE, login.getUsertype());
					editor.putString(Preferences.ALTITUDE, login.getX());
					editor.putString(Preferences.LATITUDE, login.getY());
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this, mapservice.class);
					startService(intent);
				}

			} catch (HttpException e) {
				Log.e(TAG, e.getMessage(), e);

				if (e instanceof HttpAuthException) {
					msg = getString(R.string.login_status_invalid_username_or_password);
				} else {
					msg = getString(R.string.login_status_network_or_connection_error);
				}
				publishProgress(msg);
				return TaskResult.FAILED;
			} catch (RuntimeException e) {
				msg = getString(R.string.login_status_network_or_connection_error);
				publishProgress(msg);
				return TaskResult.FAILED;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				msg = getString(R.string.login_status_network_or_connection_error);
				publishProgress(msg);
				return TaskResult.FAILED;
			}

			editor.putString(Preferences.USERNAME_KEY, mUsername);
			editor.commit();

			return TaskResult.OK;
		}
	}

	private TaskListener mLoginTaskListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {
			onLoginBegin();
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {
			updateProgress((String) param);
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.OK) {
				onLoginSuccess();
			} else {
				onLoginFailure(((LoginTask) task).getMsg());
			}
		}

		@Override
		public String getName() {
			return "login";
		}
	};

	private void updateProgress(String progress) {
		// mProgressText.setText(progress);
	}

	private void onLoginBegin() {
		disableLogin();
		// TaskFeedback.getInstance(TaskFeedback.DIALOG_MODE, WelcomeAct.this)
		// .start(getString(R.string.login_status_logging_in));
	}

	private void disableLogin() {

	}

	private void onLoginSuccess() {

		TaskFeedback
				.getInstance(TaskFeedback.DIALOG_MODE, WelcomeActivity.this)
				.success("");
		updateProgress("");
		SharedPreferences.Editor editor = mPreferences.edit();
		Log.d(TAG, "Storing credentials.");
		String userId = mPreferences.getString(Preferences.USER_ID, "");
		loginXMPP();

	}

	private void onLoginFailure(String reason) {
		TaskFeedback
				.getInstance(TaskFeedback.DIALOG_MODE, WelcomeActivity.this)
				.failed(reason);
		enableLogin();

		FunsApplication.mFuns = new Funs();
	}

	private void enableLogin() {
		Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

}
