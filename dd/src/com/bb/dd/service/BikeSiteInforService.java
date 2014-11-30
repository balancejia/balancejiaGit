package com.bb.dd.service;

import java.util.ArrayList;
import java.util.List;

import mixedserver.protocol.RPCException;
import mixedserver.protocol.jsonrpc.client.Client;
import android.os.AsyncTask;

import com.bb.dd.dao.DaoFactory;
import com.bb.dd.inter.AsyncOperatorListener;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.util.BeanUtils;
import com.bb.dd.util.CommonVariable;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Util;
import com.j256.ormlite.dao.Dao;
import com.topdt.application.BikeSiteInfor;
import com.topdt.application.entity.BikeSiteLandmarkView;
import com.topdt.application.entity.BikeSiteRealView;
import com.topdt.application.entity.BikeSiteStatusView;
import com.topdt.application.entity.BikeSitesView;
import com.topdt.coal.entity.BikeSite;
import com.topdt.coal.entity.BikeSiteException;

public class BikeSiteInforService {
	private Client client;

	public BikeSiteInforService() {
		client = Client.getClient(CommonVariable.REQUEST_RPCPATH);
		client.setDencryptMessage(true);
	}

	/**
	 * 1、 用户第一次加载当前系统日期前，整量数据加载 2、 多站点信息查询 3、 单站点信息查询
	 * all:用户初次加载当前系统日期前状态有效，整量数据加载 107009#
	 * 107010:内容为“站点ID字符串”ID之间以#号分隔。(限制：提供最多50个站点，多出截断) 10700: 单站点信息查询
	 * 
	 * @return
	 */
	public void loadBikeSites(String all, AsyncOperatorListener callback) {
		new AsyLoadBikeSites(callback).execute(all);
	}

	/**
	 * 用户数据更新 查询更新时间在上次更新时间到当前服务器时间之间的站点
	 * 
	 * @param updateTime
	 *            上次更新时间
	 * @return
	 */
	public BikeSitesView loadBikeSitesUpdate(String updateDate) {
		BikeSitesView bikeSitesView = null;
		BikeSiteInfor bikeSiteInfor = (BikeSiteInfor) client.openProxy(
				"bikeSiteInfor", BikeSiteInfor.class);
		try {
			bikeSitesView = bikeSiteInfor.loadBikeSitesUpdate(updateDate,Cons.getUserInfor());
		} catch (RPCException e) {
			e.printStackTrace();
		} finally {
			client.closeProxy(bikeSiteInfor);
		}
		return bikeSitesView;
	}

	/**
	 * 用户数据需要更新数据的数量 查询更新时间在上次更新时间到当前服务器时间之间的站点 的数量
	 * 
	 * @param updateTime
	 *            上次更新时间
	 * @return
	 */
	public void loadBikeSitesUpdateCount(String updateDate, AsyncOperatorListener callback) {
		new AsyLoadBikeSitesUpdateCount(callback).execute(updateDate);
	}

	/**
	 * 1.返回缓存中全部的站点实时信息 2.多站点实时信息查询 3.单站点实时信息查询 参数内容为： all: 缓存中全部的站点实时信息 107009#
	 * 107010:多站点实时信息查询，ID之间以#号分隔。(限制：提供最多50个站点，多出截断) 107009: 单站点实时信息查询
	 * 
	 * @param string
	 * @return
	 */
	public List<BikeSiteRealView> loadBikeSitesReal(String parameter) {//, AsyncOperatorListener callback
		//new AsyLoadBikeSitesReal(callback).execute(parameter);
		List<BikeSiteRealView> list = new ArrayList<BikeSiteRealView>();
		BikeSiteInfor bikeSiteInfor = (BikeSiteInfor) client.openProxy("bikeSiteInfor", BikeSiteInfor.class);
		try {
			list = bikeSiteInfor.loadBikeSitesReal(parameter,Cons.getUserInfor());
		} catch (RPCException e) {
			Util.l("----------"+e.getMessage());
			e.printStackTrace();
			return list=null;
		} finally {
			client.closeProxy(bikeSiteInfor);
		}
		return list;
	}

	/**
	 * 1.返回缓存中“全部”的站点储量分析结果 2.返回缓存中“部分”的站点储量分析结果
	 * 
	 * @param string
	 * @return
	 */
	public List<BikeSiteStatusView> loadBikeSitesAnalysis(String parameter) {
		List<BikeSiteStatusView> list = new ArrayList<BikeSiteStatusView>();
		BikeSiteInfor bikeSiteInfor = (BikeSiteInfor) client.openProxy(
				"bikeSiteInfor", BikeSiteInfor.class);
		try {
			list = bikeSiteInfor.loadBikeSitesAnalysis(parameter,Cons.getUserInfor());
		} catch (RPCException e) {
			e.printStackTrace();
		} finally {
			client.closeProxy(bikeSiteInfor);
		}
		return list;
	}

	/**
	 * 搜索站点 根据后台给站点地理位置的标签，返回站点id和名称 parameter 地理位置关键字
	 * 
	 * @param info
	 * @return
	 */
	public List<BikeSiteLandmarkView> loadBikeSitesSearch(String parameter) {
		List<BikeSiteLandmarkView> list = new ArrayList<BikeSiteLandmarkView>();
		BikeSiteInfor bikeSiteInfor = (BikeSiteInfor) client.openProxy(
				"bikeSiteInfor", BikeSiteInfor.class);
		try {
			list = bikeSiteInfor.loadBikeSitesSearch(parameter,Cons.getUserInfor());
		} catch (RPCException e) {
			e.printStackTrace();
		} finally {
			client.closeProxy(bikeSiteInfor);
		}
		return list;
	}

	/**
	 * 站点错误提交 选择报错类型包括有站点位置、站点故障、站点停用/铲除，获取当前位置，填写备注，对报错信息进行提交
	 * 
	 * @return
	 */
	public boolean saveBikeSitesException(BikeSiteException bikeSiteException) {
		boolean flag = false;
		BikeSiteInfor bikeSiteInfor = (BikeSiteInfor) client.openProxy(
				"bikeSiteInfor", BikeSiteInfor.class);
		try {
			flag = bikeSiteInfor.saveBikeSitesException(bikeSiteException,Cons.getUserInfor());
		} catch (RPCException e) {
			e.printStackTrace();
		} finally {
			client.closeProxy(bikeSiteInfor);
		}
		return flag;
	}
	
	private class AsyLoadBikeSitesReal extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		List<BikeSiteRealView> result;
		String message;

		AsyLoadBikeSitesReal(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {

			BikeSiteInfor bikeSiteInfor = (BikeSiteInfor) client.openProxy("bikeSiteInfor", BikeSiteInfor.class);
			try {
				result = bikeSiteInfor.loadBikeSitesReal(params[0],Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "服务端返回的异常";
				return false;
			} finally {
				client.closeProxy(bikeSiteInfor);
			}
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			if (flag) {
				listener.onSuccess(result);
			} else {
				listener.onFail(message);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}

	private class AsyLoadBikeSites extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		BikeSitesView result;
		String message;

		AsyLoadBikeSites(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			publishProgress(20);

			BikeSiteInfor bikeSiteInfor = (BikeSiteInfor) client.openProxy("bikeSiteInfor", BikeSiteInfor.class);
			try {
				result = bikeSiteInfor.loadBikeSites(params[0],Cons.getUserInfor());

				Dao<BikeSite_Lite, Integer> bikeDaos = DaoFactory.instant().getBikeSiteDao();
				List<BikeSite> sites = result.getBikeSites();

				try {
					int sizelong = sites.size();
					int init_num = 20;
					int page_num = (int) Math.ceil(sizelong / 70.0);
					for (int i = 0, size = sizelong; i < size; i++) {
						BikeSite bikeSite = sites.get(i);
						BikeSite_Lite site = new BikeSite_Lite();
						BeanUtils.copyProperties(site, bikeSite);
						if (site.getState().intValue() == 0) {
							bikeDaos.delete(site);
						} else {
							bikeDaos.createOrUpdate(site);
						}

						if ((i + 1) % page_num == 0) {
							publishProgress(init_num + (i + 1) / page_num);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteInfor);
			}
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			if (flag) {
				listener.onSuccess(result);
			} else {
				listener.onFail(message);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			listener.onProgressUpdate(values[0]);
			super.onProgressUpdate(values);
		}
	}
	
	private class AsyLoadBikeSitesUpdateCount extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		String result;
		String message;

		AsyLoadBikeSitesUpdateCount(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			publishProgress(20);
			BikeSiteInfor bikeSiteInfor = (BikeSiteInfor) client.openProxy("bikeSiteInfor", BikeSiteInfor.class);
			try {
				result = bikeSiteInfor.loadBikeSitesUpdateCount(params[0],Cons.getUserInfor());
				publishProgress(40);
				return true;
			} catch (Exception e) { // 改为rpc异常
				publishProgress(60);
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteInfor);
			}
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			if (flag) {
				listener.onSuccess(result);
			} else {
				listener.onFail(message);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}
}
