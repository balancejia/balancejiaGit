package com.bb.dd.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bb.dd.R;
import com.bb.dd.modle.BikeSite_Lite;

public class SearchBikesiteAdapter extends BaseAdapter {

	private Context _context;
	private List<BikeSite_Lite> _sites;
	public void setSites(List<BikeSite_Lite> _sites) {
		this._sites = _sites;
	}
	public SearchBikesiteAdapter(Context context,List<BikeSite_Lite> sites){
		_context=context;
		_sites=sites;
	}
	@Override
	public int getCount() {
		if(_sites==null){
			return 0;
		}
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
		BikeSite_Lite site=(BikeSite_Lite)getItem(position);
		Holder holder;
		if(null==convertView){
			convertView=View.inflate(_context, R.layout.search_listview_item, null);
			holder=new Holder();
			holder.textView1=(TextView)convertView.findViewById(R.id.main_search_list_name);
			holder.textView2=(TextView)convertView.findViewById(R.id.main_search_list_id);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.textView1.setText(site.getBikesiteName());
		holder.textView2.setText("站点编号："+site.getBikesiteId());
		return convertView;
	}

	private static class Holder{
		public TextView textView1,textView2;
	}
}
