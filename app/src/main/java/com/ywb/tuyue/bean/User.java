package com.ywb.tuyue.bean;


import com.simple.util.db.annotation.SimpleColumn;
import com.simple.util.db.annotation.SimpleId;
import com.simple.util.db.annotation.SimpleTable;

/**
 * 用户
 *
 * @author mhdt
 * @version 1.0
 * @created 2017/2/4
 */
@SimpleTable(name = "t_user")
public class User {
    /**
     * accountId : 10
     * accountName : xingyang
     * password : null
     * sex : 1
     * birthday : 1993-06-08
     * phoneNo : 17362959705
     * primaryId : null
     * pushSwitch : null
     * createTime : 2017-10-16 10:29:13
     * deviceToken : fbdc925f-32ab-47fc-a79c-eb935eaccc6f
     */
    @SimpleId
    @SimpleColumn(name = "accountId")
    private int accountId;
    @SimpleColumn(name = "accountName")
    private String accountName;
    @SimpleColumn(name = "password")
    private String password;
    @SimpleColumn(name = "sex")
    private String sex;
    @SimpleColumn(name = "birthday")
    private String birthday;
    @SimpleColumn(name = "phoneNo")
    private String phoneNo;
    @SimpleColumn(name = "primaryId")
    private int primaryId;
    @SimpleColumn(name = "pushSwitch")
    private int pushSwitch;
    @SimpleColumn(name = "createTime")
    private String createTime;
    @SimpleColumn(name = "deviceToken")
    private String deviceToken;
    @SimpleColumn(name = "headPortraitPath")
    private String headPortraitPath;
    @SimpleColumn(name = "qrcode")
    private String qrcode;
    @SimpleColumn(name = "ossUrl")
    private String ossUrl;//Oss头地址
    @SimpleColumn(name = "recommandCode")
    private String recommandCode;//Oss头地址

    public String getRecommandCode() {
        return recommandCode;
    }

    public void setRecommandCode(String recommandCode) {
        this.recommandCode = recommandCode;
    }

    public String getOssUrl() {
        return ossUrl;
    }

    public void setOssUrl(String ossUrl) {
        this.ossUrl = ossUrl;
    }

    public String getHeadPortraitPath() {
        return headPortraitPath;
    }

    public void setHeadPortraitPath(String headPortraitPath) {
        this.headPortraitPath = headPortraitPath;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public int getPushSwitch() {
        return pushSwitch;
    }

    public void setPushSwitch(int pushSwitch) {
        this.pushSwitch = pushSwitch;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
