package com.bb.dd.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.BikeApplication;
import com.bb.dd.R;
import com.bb.dd.RuteMapChoosePtActivity;
import com.bb.dd.RuteSearchActivity;
import com.bb.dd.adapter.RuteSearchAdapter;
import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.modle.RuteView;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Util;

public class RuteFragment extends AbstractFragment {
	private static final String TAG = RuteFragment.class.getName();

	private EditText et_start, et_end;
	private LinearLayout ivStartSet, ivEndSet;
	private String start, end;
	private LinearLayout ll_counterchange;
	private ListView lv_rute;
	private DoSearch doSearch;
	boolean isExit = false;
	private List<BikeSite_Lite> searchSites = new ArrayList<BikeSite_Lite>();
	private RuteSearchAdapter searchAdapter;
	private static final int SEARCH_ET_IS_NULL = 0;
	private static final int SEARCH_FINISH = 1;
	private static final int SEARCH_FINISH_ERROR = 2;
	private static final int SEARCH_FOR_NULL = 3;
	private String startAddr,endAddr,address;
	private Double startlatitude,startlongitude,endlatitude,endlongitude,latitude,longitude;
	private int etId = 1;
	private Button ruteSearch;
	private int startflag = 4;
	private int endFlag = 4;
	private int ruteFlag = 4;
	private int exchangeFlag=0;
	private int changePlaceFlag = 0;
	private ImageView ivPointStart,ivPointEnd;
	private RuteView ruteView;
	private MKSearch mSearch = null; 
	private String city="太原市";
	public RuteFragment(Context context) {
		super(context);
	}
	public RuteFragment(Context context,RuteView _ruteView) {
		super(context);
		ruteView=_ruteView;
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.rute);
		initView();
		initBaiDuPioSearch();
		return rootView;
	}
	@Override
	public void closed() {

	}

	private void initView() {
		et_start = (EditText) rootView.findViewById(R.id.rute_et_start);
		et_end = (EditText) rootView.findViewById(R.id.rute_et_end);
		ivStartSet = (LinearLayout) rootView
				.findViewById(R.id.rute_iv_left_start_set);
		ivEndSet = (LinearLayout) rootView.findViewById(R.id.rute_iv_left_end_set);
		ll_counterchange = (LinearLayout) rootView
				.findViewById(R.id.rute_counterchange);
		ivPointStart=(ImageView)rootView.findViewById(R.id.rute_location_start);
		ivPointEnd=(ImageView)rootView.findViewById(R.id.rute_location_end);
		lv_rute = (ListView) rootView.findViewById(R.id.rute_listview);
		searchAdapter = new RuteSearchAdapter(_context, searchSites);
		lv_rute.setAdapter(searchAdapter);
		if(ruteView!=null){
			if(ruteView.getFlag()==1){
				startflag=2;
				startAddr=ruteView.getStartAddress();
				startlatitude=ruteView.getStartlatitude();
				startlongitude=ruteView.getStartlongitude();
				et_start.setText(startAddr);
				if(Cons.ruteMapChoosePt==2){
					endAddr=Cons.ruteAddress;
					endlatitude=Cons.ruteLatitude;
					endlongitude=Cons.ruteLongitude;
					Cons.ruteMapChoosePt=0;
					et_end.setText(endAddr);
				}else{
					Cons.ruteMapChoosePt=1;
					Cons.ruteAddress=startAddr;
					Cons.ruteLatitude=startlatitude;
					Cons.ruteLongitude=startlongitude;
				}
			}else if(ruteView.getFlag()==2){
				endFlag=2;
				endAddr=ruteView.getEndAddress();
				endlatitude=ruteView.getEndlatitude();
				endlongitude=ruteView.getEndlongitude();
				et_end.setText(endAddr);
				
				if(Cons.ruteMapChoosePt==1){
					startAddr=Cons.ruteAddress;
					startlatitude=Cons.ruteLatitude;
					startlongitude=Cons.ruteLongitude;
					Cons.ruteMapChoosePt=0;
					et_start.setText(startAddr);
				}else{
					Cons.ruteMapChoosePt=2;
					Cons.ruteAddress=endAddr;
					Cons.ruteLatitude=endlatitude;
					Cons.ruteLongitude=endlongitude;
					
				}
			}else{
				startAddr=ruteView.getStartAddress();
				startlatitude=ruteView.getStartlatitude();
				startlongitude=ruteView.getStartlongitude();
				et_start.setText(startAddr);
				endAddr=ruteView.getEndAddress();
				endlatitude=ruteView.getEndlatitude();
				endlongitude=ruteView.getEndlongitude();
				et_end.setText(endAddr);
			}
			
		}
		ll_counterchange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exchangeFlag=1;
				searchSites.clear();
				searchAdapter.set_sites(searchSites);
				searchAdapter.notifyDataSetChanged();
				start = et_start.getText().toString();
				end = et_end.getText().toString();
				et_start.setText(end);
				et_end.setText(start);
				latitude=startlatitude;
				longitude=startlongitude;
				startlatitude=endlatitude;
				startlongitude=endlongitude;
				endlatitude=latitude;
				endlongitude=longitude;
				address=startAddr;
				startAddr=endAddr;
				endAddr=address;
				ruteFlag=startflag;
				startflag=endFlag;
				endFlag=ruteFlag;
				
				//TODO 切换起终点逻辑测试
				/*String msg = "latitude:"+latitude+"\n"
						+"longitude:"+longitude+"\n"
						+"startlatitude:"+startlatitude+"\n"
						+"startlongitude:"+startlongitude+"\n"
						+"endlatitude:"+endlatitude+"\n"
						+"endlongitude:"+endlongitude+"\n"
						+"address:"+address+"\n"
						+"startAddr:"+startAddr+"\n"
						+"endAddr:"+endAddr+"\n"
						+"ruteFlag:"+ruteFlag+"\n"
						+"startflag:"+startflag+"\n"
						+"endFlag:"+endFlag+"\n";
				Log.i("ttt", msg);*/
				
			}
		});
		lv_rute.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int postion, long arg3) {
				isExit = false;
				BikeSite_Lite site = (BikeSite_Lite) adapter
						.getItemAtPosition(postion);
				Double latitude = Double.parseDouble(site.getSign4().trim()) * 1E6;
				Double longitude = Double.parseDouble(site.getSign3().trim()) * 1E6;
				GeoPoint point = new GeoPoint(longitude.intValue(), latitude
						.intValue());
				if (etId == 0) {
					startlatitude = Double.valueOf(point.getLatitudeE6());
					startlongitude = Double.valueOf(point.getLongitudeE6());
					startflag=1;
					et_start.setText(site.getBikesiteName());
					startAddr = site.getBikesiteName();
				} else {
					endlatitude =Double.valueOf(point.getLatitudeE6());;
					endlongitude = Double.valueOf(point.getLongitudeE6());
					endFlag=1;
					et_end.setText(site.getBikesiteName());
					endAddr = site.getBikesiteName();
				}
				searchSites.clear();
				searchAdapter.set_sites(searchSites);
				searchAdapter.notifyDataSetChanged();
			}
		});
		ruteSearch = (Button) rootView.findViewById(R.id.rute_search);
		ruteSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String startEt = getEditValue(et_start);
				String endEt = getEditValue(et_end);
				if ("".equalsIgnoreCase(startEt) || "".equalsIgnoreCase(endEt)) {
					handler.sendEmptyMessage(SEARCH_ET_IS_NULL);

				} else {
					RuteView ruteView=new RuteView();
					ruteView.setStartAddress(startAddr);
					ruteView.setEndAddress(endAddr);
					ruteView.setStartlatitude(startlatitude);
					ruteView.setStartlongitude(startlongitude);
					ruteView.setEndlatitude(endlatitude);
					ruteView.setEndlongitude(endlongitude);
					Intent intent = new Intent(_context,
							RuteSearchActivity.class);
					intent.putExtra("ruteView", ruteView);
					//TODO 测试
//					Log.i("ttt", ruteView.toString());
					_context.startActivity(intent);
				}

			}
		});
		ivStartSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(1);
			}
		});
		ivEndSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(2);
			}
		});
		et_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (startflag == 4 || startflag == 1 || startflag == 3) {
					et_start.setText("");
				}
				searchSites.clear();
				searchAdapter.set_sites(searchSites);
				searchAdapter.notifyDataSetChanged();

			}
		});
		et_end.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (endFlag == 4 || endFlag == 1 || endFlag == 3) {
					et_end.setText("");
				}
				searchSites.clear();
				searchAdapter.set_sites(searchSites);
				searchAdapter.notifyDataSetChanged();

			}
		});
		et_start.addTextChangedListener(new TextWatch(etId));
		et_end.addTextChangedListener(new TextWatch());
		ivPointStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startflag=2;
				
				String endEt = getEditValue(et_end);
				if(!("".equalsIgnoreCase(endEt))){
					Cons.ruteMapChoosePt=2;
					Cons.ruteAddress=endAddr;
					Cons.ruteLatitude=endlatitude;
					Cons.ruteLongitude=endlongitude;
				}
				Intent intent=new Intent();
				intent.setClass(_context, RuteMapChoosePtActivity.class);
				intent.putExtra("flag", "start");
				_context.startActivity(intent);
				
			}
		});
		ivPointEnd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String startEt = getEditValue(et_start);
				if(!("".equalsIgnoreCase(startEt))){
					Cons.ruteMapChoosePt=1;
					Cons.ruteAddress=startAddr;
					Cons.ruteLatitude=startlatitude;
					Cons.ruteLongitude=startlongitude;
				}
				endFlag=2;
				Intent intent=new Intent();
				intent.setClass(_context, RuteMapChoosePtActivity.class);
				intent.putExtra("flag", "end");
				_context.startActivity(intent);
			}
		});

	}

	private void initBaiDuPioSearch() {
	   BikeApplication app =BikeApplication.getInstance();
		 mSearch = new MKSearch();
		 mSearch.init(app.mBMapManager, new MKSearchListener(){
	            //在此处理详情页结果
	            @Override
	            public void onGetPoiDetailSearchResult(int type, int error) {
	            }
	            /**
	             * 在此处理poi搜索结果
	             */
	            public void onGetPoiResult(MKPoiResult res, int type, int error) {
	                // 错误号可参考MKEvent中的定义
	                if (error != 0 || res == null) {
	                    return;
	                }
	                ArrayList<MKPoiInfo> mkPois = res.getAllPoi();
	                if(null!=mkPois&&mkPois.size()>0){
	                	for(MKPoiInfo info:mkPois){
		                	BikeSite_Lite bikeSite_Lite=new BikeSite_Lite();
		                	bikeSite_Lite.setBikesiteName(info.name);
		                	bikeSite_Lite.setSign3(String.valueOf(info.pt.getLatitudeE6()/1e6));
		                	bikeSite_Lite.setSign4(String.valueOf(info.pt.getLongitudeE6()/1e6));
		                	bikeSite_Lite.setBikesiteId("");
		                	searchSites.add(bikeSite_Lite);
		                	searchAdapter.set_sites(searchSites);
		    				searchAdapter.notifyDataSetChanged();
		                }
	                	
	                }
	            }
	            public void onGetDrivingRouteResult(MKDrivingRouteResult res,
	                    int error) {
	            }
	            public void onGetTransitRouteResult(MKTransitRouteResult res,
	                    int error) {
	            }
	            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
	                    int error) {
	            }
	            public void onGetAddrResult(MKAddrInfo res, int error) {
	            }
	            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
	            }
	           
	            @Override
	            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
	            	
	            }
				@Override
				public void onGetShareUrlResult(MKShareUrlResult arg0,
						int arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
	            	
	        });
		 
		 
	}
	private void showDialog(final int ivflag){
		new AlertDialog.Builder(_context)
		.setTitle("请选择")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setSingleChoiceItems(
				new String[] { "我的位置", "站点编号或名称", "地图上选点",
						"地点名称" }, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						if(ivflag==1){
							startflag = which;	
							switch (startflag) {
							case 0:
								et_start.setText("我的位置");
								startlatitude=Double.valueOf(Cons.locPoint.getLatitudeE6());
								startlongitude=Double.valueOf(Cons.locPoint.getLongitudeE6());
								startAddr="我的位置";
								break;
							case 1:
								et_start.setText("");
								et_start.setHint("请输入站点编号或名称");
								break;
							case 2:
								et_start.setText("");
								et_start.setHint("请点击后面位置图标,在地图上选点");
								break;
							case 3:
								et_start.setText("");
								et_start.setHint("请输入地点名称");
								break;

							}
						}else{
							endFlag = which;
							switch (endFlag) {
							case 0:
								et_end.setText("我的位置");
								endlatitude=Double.valueOf(Cons.locPoint.getLatitudeE6());
								endlongitude=Double.valueOf(Cons.locPoint.getLongitudeE6());
								endAddr="我的位置";
								break;
							case 1:
								et_end.setText("");
								et_end.setHint("请输入站点编号或名称");
								break;
							case 2:
								et_end.setText("");
								et_end.setHint("请点击后面位置图标,在地图上选点");
								break;
							case 3:
								et_end.setText("");
								et_end.setHint("请输入地点名称");
								break;
							}
						}
						
						dialog.dismiss();
					}
				}).setNegativeButton("取消", null).show();
	}

	private class TextWatch implements TextWatcher {
		private int id;

		public TextWatch() {
			super();
		}

		public TextWatch(int etId) {
			super();
			this.id = etId;
		}

		@Override
		public void afterTextChanged(Editable s) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			isExit = true;
			
			/*if (doSearch == null) {
				doSearch = new DoSearch();
			}*/
			if (id == 1) {
				if (startflag == 4 || startflag == 1 || startflag == 3) {
					new DoSearch(s.toString());
				}else{
					//doSearch.excute("");
				}
				etId = 0;
			} else {
				if (endFlag == 4 || endFlag == 1 || endFlag == 3) {
					new DoSearch(s.toString());
				}else{
					//doSearch.excute("");
				}
				etId = 1;
			}

		}

	}

	private class DoSearch extends Thread {
		//Object key = new Object();
		private String value;
		private BikeOverlayDao dao;
		String[] colums = { "bikesite_id", "bikesite_name" };

		private DoSearch(String values) {
			value=values;
			dao = new BikeOverlayDao();
			start();
		}

		@Override
		public void run() {
			/*while (isExit) {
				synchronized (key) {
					try {
						key.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}*/
				if (null != value) {
					if ("".equals(value.trim())) {
						searchSites.clear();
						handler.sendEmptyMessage(SEARCH_FOR_NULL);
					} else {
						searchSites.clear();
						if(startflag==3&&etId==0){
							  mSearch.poiSearchInCity(city, 
					                  value);
						}else if(endFlag==3&&etId==1){
							mSearch.poiSearchInCity(city, 
					                  value);
						}
						String likeValue = "%" + value + "%";
						List<BikeSite_Lite> sites = dao.query4Like(colums,
								likeValue, likeValue);
						if (null != sites && 0 != sites.size()) {
							searchSites.addAll(sites);
							sites.clear();
							sites = null;
							handler.sendEmptyMessage(SEARCH_FINISH);
						} else {
							if(startflag==3||endFlag==3){
								handler.sendEmptyMessage(SEARCH_FINISH);
							}else{
								handler.sendEmptyMessage(SEARCH_FINISH_ERROR);
								if(exchangeFlag==1){
									exchangeFlag=0;
								}else{
									//TODO 注释清空代码
									/*if (etId == 0) {
										startAddr = null;
										startlatitude=null;
										startlongitude=null;
									} else {
										endAddr = null;
										endlatitude=null;
										endlongitude=null;
								   }*/
									
								}
								
							}
							
								
							
						}
					}

				}
			//}
		}

		/*public void excute(String var) {
			value = var;
			//Util.l("-------startflag-----"+startflag+"------endFlag-----"+endFlag+"------etId-----"+etId);
			synchronized (key) {
				key.notify();
			}
		}*/
	}

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SEARCH_ET_IS_NULL:
				Util.t("请输入搜索路线的起点和终点");
				searchSites.clear();
				searchAdapter.set_sites(searchSites);
				searchAdapter.notifyDataSetChanged();
				break;
			case SEARCH_FINISH:
				searchAdapter.set_sites(searchSites);
				searchAdapter.notifyDataSetChanged();
				break;
			case SEARCH_FINISH_ERROR:
				searchAdapter.set_sites(searchSites);
				searchAdapter.notifyDataSetChanged();
				break;
			case SEARCH_FOR_NULL:
				searchAdapter.set_sites(searchSites);
				searchAdapter.notifyDataSetChanged();
				break;
			}
		}
	};

	public String getEditValue(EditText ed) {
		return ed.getText().toString().trim();
	}

}
