package cn.edu.nuc.csce.dcldims.entity;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class Configurationparameter {

    private String pjname;//项目名称
    private String dopeople;//执行人
    private String shoupeople;//授权人
    private String doublehoist;//双机抬吊
    private String allmonitor;//实时监测
    private String frequency;//1s频率

    public String getPjname() {
        return pjname;
    }

    public void setPjname(String pjname) {
        this.pjname = pjname;
    }

    public String getDopeople() {
        return dopeople;
    }

    public void setDopeople(String dopeople) {
        this.dopeople = dopeople;
    }

    public String getShoupeople() {
        return shoupeople;
    }

    public void setShoupeople(String shoupeople) {
        this.shoupeople = shoupeople;
    }

    public String getDoublehoist() {
        return doublehoist;
    }

    public void setDoublehoist(String doublehoist) {
        this.doublehoist = doublehoist;
    }

    public String getAllmonitor() {
        return allmonitor;
    }

    public void setAllmonitor(String allmonitor) {
        this.allmonitor = allmonitor;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getQzjname() {
        return qzjname;
    }

    public void setQzjname(String qzjname) {
        this.qzjname = qzjname;
    }

    public String getQzjdriver() {
        return qzjdriver;
    }

    public void setQzjdriver(String qzjdriver) {
        this.qzjdriver = qzjdriver;
    }

    public String getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(String installLocation) {
        this.installLocation = installLocation;
    }

    public String getGoushenkey() {
        return goushenkey;
    }

    public void setGoushenkey(String goushenkey) {
        this.goushenkey = goushenkey;
    }

    public String getQzjnametwo() {
        return qzjnametwo;
    }

    public void setQzjnametwo(String qzjnametwo) {
        this.qzjnametwo = qzjnametwo;
    }

    public String getQzjdrivertwo() {
        return qzjdrivertwo;
    }

    public void setQzjdrivertwo(String qzjdrivertwo) {
        this.qzjdrivertwo = qzjdrivertwo;
    }

    public String getGoushenkeytwo() {
        return goushenkeytwo;
    }

    public void setGoushenkeytwo(String goushenkeytwo) {
        this.goushenkeytwo = goushenkeytwo;
    }

    public String getInstallLocationtwo() {
        return installLocationtwo;
    }

    public void setInstallLocationtwo(String installLocationtwo) {
        this.installLocationtwo = installLocationtwo;
    }

    private String stateInsatll;//监测设备1安装状态
    private String qzjname;//起重机名称
    private String qzjdriver;//起重机司机
    private String installLocation;//安装位置
    private String goushenkey;//
    private String calibrationkeyrollkey;
    private String calibrationkeypitchkey;

    public String getStateInsatll() {
        return stateInsatll;
    }

    public void setStateInsatll(String stateInsatll) {
        this.stateInsatll = stateInsatll;
    }

    public String getTwo_stateInsatll() {
        return two_stateInsatll;
    }

    public void setTwo_stateInsatll(String two_stateInsatll) {
        this.two_stateInsatll = two_stateInsatll;
    }

    private String two_stateInsatll;//监测设备2安装状态
    private String qzjnametwo;//起重机2名称
    private String qzjdrivertwo;//起重2机司机
    private String goushenkeytwo;//
    private String twocalibrationkeyrollkey;
    private String twocalibrationkeypitchkey;

    public String getCalibrationkeyrollkey() {
        return calibrationkeyrollkey;
    }

    public void setCalibrationkeyrollkey(String calibrationkeyrollkey) {
        this.calibrationkeyrollkey = calibrationkeyrollkey;
    }

    public String getCalibrationkeypitchkey() {
        return calibrationkeypitchkey;
    }

    public void setCalibrationkeypitchkey(String calibrationkeypitchkey) {
        this.calibrationkeypitchkey = calibrationkeypitchkey;
    }

    public String getTwocalibrationkeyrollkey() {
        return twocalibrationkeyrollkey;
    }

    public void setTwocalibrationkeyrollkey(String twocalibrationkeyrollkey) {
        this.twocalibrationkeyrollkey = twocalibrationkeyrollkey;
    }

    public String getTwocalibrationkeypitchkey() {
        return twocalibrationkeypitchkey;
    }

    public void setTwocalibrationkeypitchkey(String twocalibrationkeypitchkey) {
        this.twocalibrationkeypitchkey = twocalibrationkeypitchkey;
    }

    private String installLocationtwo;//安装位置


}

