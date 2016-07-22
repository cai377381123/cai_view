package com.fanyu.home.main3.activity;

import java.io.File;

import com.fanyu.boundless.R;
import com.fanyu.home.main1.activity.ImageGridActivity;
import com.fanyu.home.main1.activity.ShuoshuoActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeFragmentAct extends FragmentActivity implements
		View.OnClickListener {

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CHOOSE_PICTURES = 6;
	private static final int ADDRESS = 2;
	
	private String imagePath;
	
	private LinearLayout ll_btn_zuoye;
	private LinearLayout ll_btn_huodong;
	private LinearLayout ll_btn_daoli;
	private LinearLayout ll_btn_tongzhi;
	private LinearLayout ll_btn_dongtai;
	private LinearLayout ll_btn_toupiao;
	private LinearLayout ll_btn_tuijianmovie;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		View view = LinearLayout.inflate(this, R.layout.activity_fragment_home,
				null);
		setContentView(view);
		initView(view);
	}

	private void initView(View view) {
		ll_btn_zuoye = (LinearLayout) view.findViewById(R.id.ll_btn_zuoye);
		ll_btn_huodong = (LinearLayout) view.findViewById(R.id.ll_btn_huodong);
		ll_btn_daoli = (LinearLayout) view.findViewById(R.id.ll_btn_daoli);
		ll_btn_tongzhi = (LinearLayout) view.findViewById(R.id.ll_btn_tongzhi);
		ll_btn_dongtai = (LinearLayout) view.findViewById(R.id.ll_btn_dongtai);
		ll_btn_toupiao = (LinearLayout) view.findViewById(R.id.ll_btn_toupiao);
		ll_btn_tuijianmovie=(LinearLayout) view.findViewById(R.id.ll_btn_tuijianmovie);

		ll_btn_zuoye.setOnClickListener(this);
		ll_btn_huodong.setOnClickListener(this);
		ll_btn_daoli.setOnClickListener(this);
		ll_btn_tongzhi.setOnClickListener(this);
		ll_btn_dongtai.setOnClickListener(this);
		ll_btn_toupiao.setOnClickListener(this);
		ll_btn_tuijianmovie.setOnClickListener(this);
		
		
		
		//头像选择按钮
		mBtnsctp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkSDCardAvailable()) {
					File f = new File(
							Environment.getExternalStorageDirectory(),
							"OuRecord");
					if (!f.exists()) {
						f.mkdirs();
					}
					showPicturePicker(HomeFragmentAct.this);
				} else {
					Toast.makeText(getBaseContext(), "未涨到SD卡!",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_btn_zuoye:

			break;

		case R.id.ll_btn_huodong:

			break;
		case R.id.ll_btn_daoli:

			break;
		case R.id.ll_btn_tongzhi:

			break;
		case R.id.ll_btn_dongtai:

			break;
		case R.id.ll_btn_toupiao:

			break;
		case R.id.ll_btn_tuijianmovie:

			break;
			
		}

	}
	
	
	/**
	 * 检测手机卡
	 * 
	 * @return
	 */
	public static boolean checkSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	
	public void showPicturePicker(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "拍照", "相册" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case TAKE_PICTURE:
							StringBuilder fileName = new StringBuilder();
							fileName.append(Environment
									.getExternalStorageDirectory());
							fileName.append(File.separator + "oldage"
									+ File.separator);
							String wj = fileName.toString();
							File file = new File(wj);
							if (!file.getAbsoluteFile().exists()) {
								file.mkdirs();
							}
							fileName.append(System.currentTimeMillis());
							fileName.append(".jpg");
							imagePath = fileName.toString();
							Intent intent = new Intent(
									android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(imagePath)));
							startActivityForResult(intent, TAKE_PICTURE);
							break;

						// 选择照片
						case CHOOSE_PICTURE:
							System.out.println("");
							Intent intentb = new Intent(HomeFragmentAct.this,
									ImageGridActivity.class);
							startActivityForResult(intentb, CHOOSE_PICTURES);
							break;
						default:
							break;
						}
					}

				});
		builder.create().show();
	}
}
