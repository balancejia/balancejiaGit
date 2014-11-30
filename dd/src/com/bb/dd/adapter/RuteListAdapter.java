package com.bb.dd.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bb.dd.R;

public class RuteListAdapter extends BaseAdapter {
	
	private Context _context;
	private List<String> _ruteList;
	public void setRuteList(List<String> _ruteList) {
		this._ruteList = _ruteList;
	}
	public RuteListAdapter(Context context,List<String> ruteList){
		_context=context;
		_ruteList=ruteList;
	}
	@Override
	public int getCount() {
		if (_ruteList==null) {
			return 0;
		}
		return _ruteList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return _ruteList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String rute=_ruteList.get(position);
		Holder holder;
		if(null==convertView){
			convertView=View.inflate(_context, R.layout.rute_listview_item, null);
			holder=new Holder();
			holder.textViewCount=(TextView)convertView.findViewById(R.id.rute_count);
			holder.textViewRute=(TextView)convertView.findViewById(R.id.rute_item_tv);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		String[] s=rute.split(":");
		holder.textViewCount.setText(s[0]);
		holder.textViewRute.setText(s[1]);
		return convertView;
	}

	private static class Holder{
		public TextView textViewCount,textViewRute;
	}
}
