package com.hnxx.wisdombase.framework.api;

import android.graphics.drawable.Drawable;

/**
 * apk应用信息
 * @author Administrator
 *
 */
public class ApkInfo {
	/*文件名*/
	private String filename;
	
	private String versionCode;
	
	/*应用名*/
	private String appname;
	/*包名*/
	private String packagename;
	/*版本号*/
	private String version;
	/*应用图标*/
	private Drawable appicon;
	/*应用图片请求路径*/
	private String appiconpath;
	//状态 -2未下载完成 -1已下载完成未安装 1 已安装  2 升级
	private int state = 0;
	/*apk保存路径*/
	private String apkpath;
	public String getVersionCode() {
		return versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	/*升级的url*/
	private String updateurl;
	/*应用id*/
	private String appid;
	/*应用大小*/
	private String appsize;
	/*已下载百分比*/
	private float downloadedpercent;
	/*应用已下载的大小*/
	private String downloadedsize;
	/*下载路径*/
	private String downloadurl;
	/*保存上一个appid*/
	private String oldappid;

	private String versionName;
	
	public ApkInfo() {
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Drawable getAppicon() {
		return appicon;
	}
	public void setAppicon(Drawable appicon) {
		this.appicon = appicon;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
	public String getApkpath() {
		return apkpath;
	}
	public void setApkpath(String apkpath) {
		this.apkpath = apkpath;
	}
	
	public String getUpdateurl() {
		return updateurl;
	}
	public void setUpdateurl(String updateurl) {
		this.updateurl = updateurl;
	}
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	
	public String getAppsize() {
		return appsize;
	}
	public void setAppsize(String appsize) {
		this.appsize = appsize;
	}
	
	
	public float getDownloadedpercent() {
		return downloadedpercent;
	}
	public void setDownloadedpercent(float downloadedpercent) {
		this.downloadedpercent = downloadedpercent;
	}
	
	public String getDownloadedsize() {
		return downloadedsize;
	}
	public void setDownloadedsize(String downloadedsize) {
		this.downloadedsize = downloadedsize;
	}
	
	public String getDownloadurl() {
		return downloadurl;
	}
	public void setDownloadurl(String downloadurl) {
		this.downloadurl = downloadurl;
	}
	
	
	public String getAppiconpath() {
		return appiconpath;
	}
	public void setAppiconpath(String appiconpath) {
		this.appiconpath = appiconpath;
	}
	
	
	public String getOldappid() {
		return oldappid;
	}
	public void setOldappid(String oldappid) {
		this.oldappid = oldappid;
	}
	@Override
	public String toString() {
		return "appid:"+appid+",filename:"+filename+",appname:"+appname+",packagename:"+packagename+",version:"+
		version+",appicon:"+appicon+",state:"+state+",apkpath:"+apkpath+",updateurl:"+updateurl+",appsize:"+appsize
		+",downloadedpercent:"+downloadedpercent+",downloadedsize:"+downloadedsize+",downloadurl:"+downloadurl
		+",appiconpath:"+appiconpath+",oldappid:"+oldappid;
	}
	public void setVersionName(String versionName) {
		this.versionName=versionName;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode=versionCode;
	}
	
}
