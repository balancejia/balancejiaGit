package com.bb.dd.inter;

/**
 * 异步操作的返回接口，由调用者实现来接受异步返回的数据/错误
 * @author Administrator
 *
 */
public interface AsyncOperatorListener {
	/**
	 * 调用成功
	 * @param bikeSitesView
	 */
	public void onSuccess(Object obj);
	
	/**
	 * 调用失败
	 */
	public void onFail(String message);
	
	/**
	 * 更新进度
	 * @param progress
	 */
	public void onProgressUpdate(int progress);
}