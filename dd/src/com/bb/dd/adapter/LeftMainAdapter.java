package com.bb.dd.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bb.dd.BikeApplication;
import com.bb.dd.R;
import com.bb.dd.modle.LeftMainView;

public class LeftMainAdapter extends BaseAdapter {
	
	private Context _context;
	private List<LeftMainView> _leftMainViews;
	public LeftMainAdapter(Context context,List<LeftMainView> leftMainViews){
		_context=context;
		_leftMainViews=leftMainViews;
	}
	@Override
	public int getCount() {
		return _leftMainViews.size();
	}

	@Override
	public Object getItem(int arg0) {
		return _leftMainViews.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LeftMainView leftMainView=(LeftMainView)getItem(position);
		Holder holder;
		if(null==convertView){
			convertView=View.inflate(_context, R.layout.left_listview_item, null);
			holder=new Holder();
			holder.textView=(TextView)convertView.findViewById(R.id.left_item_tv);
			holder.imageView=(ImageView)convertView.findViewById(R.id.left_item_img);
			holder.textViewCount=(TextView)convertView.findViewById(R.id.left_item_synchron_count);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.textView.setText(leftMainView.getTextView());
		holder.imageView.setBackgroundResource(leftMainView.getDrawable());
		if(leftMainView.getTextView().equals("数据更新")&&BikeApplication.getInstance().getSynchroCount()>0){
			holder.textViewCount.setVisibility(View.VISIBLE);
			holder.textViewCount.setText(BikeApplication.getInstance().getSynchroCount()+"");
		}else{
			holder.textViewCount.setVisibility(View.GONE);
		}
		return convertView;
	}

	private static class Holder{
		public TextView textView,textViewCount;
		public ImageView imageView;
		
	}
}
