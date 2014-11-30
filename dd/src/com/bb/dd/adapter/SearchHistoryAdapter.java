package com.bb.dd.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bb.dd.R;
import com.bb.dd.dao.DaoFactory;
import com.bb.dd.modle.SearchHistory;
import com.j256.ormlite.dao.Dao;

public class SearchHistoryAdapter extends BaseAdapter {

	private Context _context;
	private List<SearchHistory> _sites;
	private Dao<SearchHistory, Integer> searchHistoryDaos = DaoFactory.instant().getSearchHistoryDao();
	public SearchHistoryAdapter(Context context,List<SearchHistory> sites){
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
		final SearchHistory site=(SearchHistory)getItem(position);
		Holder holder;
		if(null==convertView){
			convertView=View.inflate(_context, R.layout.search_history_list, null);
			holder=new Holder();
			holder.textView1=(TextView)convertView.findViewById(R.id.main_search_list_name);
			/*holder.button=(Button)convertView.findViewById(R.id.search_history_delete_bt);
			holder.button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						Util.l("删除"+site.getSearchHistoryId());
						searchHistoryDaos.deleteById(site.getSearchHistoryId());
						_sites.remove(site);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					
					
				}
			});*/
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.textView1.setText(site.getSearchHistoryName());
		return convertView;
	}

	private static class Holder{
		public TextView textView1;
		/*public Button button;*/
	}
}
