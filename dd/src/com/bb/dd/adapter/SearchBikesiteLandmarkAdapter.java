package com.bb.dd.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bb.dd.R;
import com.topdt.application.entity.BikeSiteLandmarkView;

public class SearchBikesiteLandmarkAdapter extends BaseAdapter {

	private Context _context;
	private List<BikeSiteLandmarkView> _sites;
	public SearchBikesiteLandmarkAdapter(Context context,List<BikeSiteLandmarkView> sites){
		_context=context;
		_sites=sites;
	}
	@Override
	public int getCount() {
		return _sites.size();
	}

	@Override
	public Object getItem(int arg0) {
		return _sites.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BikeSiteLandmarkView site=(BikeSiteLandmarkView)getItem(position);
		Holder holder;
		if(null==convertView){
			convertView=View.inflate(_context, R.layout.search_listview_item, null);
			holder=new Holder();
			holder.textView1=(TextView)convertView.findViewById(R.id.main_search_list_name);
			holder.textView2=(TextView)convertView.findViewById(R.id.landmark_name);
			holder.textView2.setVisibility(View.VISIBLE);
			holder.textView3=(TextView)convertView.findViewById(R.id.main_search_list_id);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.textView1.setText(site.getBikesiteName());
		holder.textView2.setText("附近标志物:"+site.getBikeLandmarkName());
		holder.textView3.setText("站点编号:"+site.getBikesiteId());
		return convertView;
	}

	private static class Holder{
		public TextView textView1,textView2,textView3;
	}
}
