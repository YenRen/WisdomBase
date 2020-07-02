/**
 *
 * @author zhoujun
 * @date 2015年11月19日
 */
package com.hnxx.wisdombase.db.sp;

public class SystemSp extends BaseSp {

	/** 保存最近崩溃日志文件名 */
	private static final String K_CRASH_FILE_NAME = "crashFileName";

	@Override
	protected String getCustomSpName() {
		return "systemSp";
	}

	public String getCrashFileName() {
		return this.getString(K_CRASH_FILE_NAME);
	}
	public void setCrashFileName(String crashFileName) {
		this.put(K_CRASH_FILE_NAME, crashFileName);
	}
}
