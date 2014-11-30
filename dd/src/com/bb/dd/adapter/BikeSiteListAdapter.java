package com.bb.dd.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bb.dd.R;
import com.bb.dd.modle.BikeSite_LiteView;
import com.topdt.application.entity.BikeSiteRealView;

public class BikeSiteListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private List<String> mGroupList;
	private ArrayList<ArrayList<BikeSite_LiteView>> mChildList;
	ProgressBar progressBar;
	BikeSiteRealView bsv;
	
	public void setmGroupList(List<String> mGroupList) {
		this.mGroupList = mGroupList;
	}
	public void setmChildList(ArrayList<ArrayList<BikeSite_LiteView>> mChildList) {
		this.mChildList = mChildList;
	}
	public BikeSiteListAdapter(Context mContext, List<String> groupList,
			ArrayList<ArrayList<BikeSite_LiteView>> childList) {
		super();
		this.mContext = mContext;
		this.mGroupList = groupList;
		this.mChildList = childList;
	}

	@Override
	public int getGroupCount() {
		if(mGroupList==null){
			return 0;
		}
		return mGroupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(mChildList==null){
			return 0;
		}
		return mChildList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mChildList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// 实例化布局文件
		RelativeLayout glayout = (RelativeLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.bikesitelist_group_layout, null);
		TextView tv = (TextView) glayout.findViewById(R.id.gtv);
		ImageView imageView=(ImageView)glayout.findViewById(R.id.gimg);
		// 判断分组是否展开，分别传入不同的图片资源
		if (isExpanded)
			imageView.setImageResource(R.drawable.bikesite_list_up);
		else
			imageView.setImageResource(R.drawable.bikesite_list_down);
		
		tv.setText(this.getGroup(groupPosition).toString());
		return glayout;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// 实例化布局文件
		LinearLayout clayout = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.bikesitelist_child_layout, null);
		TextView tvName = (TextView) clayout.findViewById(R.id.bike_site_name);
		TextView tvDistance = (TextView) clayout.findViewById(R.id.bike_site_distance);
		TextView bsUserBike = (TextView)clayout. findViewById(R.id.bike_site_can_borrow_num);
		TextView bsUserPos = (TextView)clayout. findViewById(R.id.bike_site_can_send_num);
		progressBar = (ProgressBar)clayout. findViewById(R.id.progress_sitedetail);
		BikeSite_LiteView bikeSite_LiteView=(BikeSite_LiteView)getChild(groupPosition, childPosition);
		bsv=bikeSite_LiteView.getBikeSiteRealView();
		tvName.setText(bikeSite_LiteView.getBikeSite_Lite().getBikesiteName());
		tvDistance.setText(bikeSite_LiteView.getDistance()+"千米");
		bsUserBike.setText(bsv.getCanusebikecount() + "");
		bsUserPos.setText(bsv.getCanuseposcount() + "");
		initProgress();
		return clayout;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	/**
	 * 初始化站点详情进度条
	 */
	private void initProgress() {
		Double useBikecount = Double.valueOf(bsv.getCanusebikecount());
		Double usePosCount = Double.valueOf(bsv.getCanuseposcount());
		Double totalCount = useBikecount + usePosCount;
		if(totalCount!=0){
			Double count = (useBikecount / totalCount) * 100;
			DecimalFormat df = new DecimalFormat("0");
			int useBikePro = Integer.parseInt(df.format(count));
			progressBar.setProgress(useBikePro);
		}
		
	}

}