package com.bb.dd;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

import com.bb.dd.adapter.GuideViewPagerAdapter;

public class ViewPagerActivity extends Activity {
	private Context context;
	private ViewPager mPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	LayoutInflater mInflater;
	private Button button;
	private RadioGroup viewPagerPoint;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ViewPagerActivity.this;
		setContentView(R.layout.view_pager);
		initViewPager();
		button=(Button) findViewById(R.id.go_login);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent_index = new Intent(context, IndexMainActivity.class);
				startActivity(intent_index);
				finish();
			}
		});
	}
	private void initViewPager() {
		mInflater = getLayoutInflater();
		mPager = (ViewPager) findViewById(R.id.vPager);
		viewPagerPoint=(RadioGroup)findViewById(R.id.view_pager_show);
		listViews = new ArrayList<View>();
		listViews.add(mInflater.inflate(R.layout.guide_one, null));
		listViews.add(mInflater.inflate(R.layout.guide_two, null));
		listViews.add(mInflater.inflate(R.layout.guide_three, null));
		mPager.setAdapter(new GuideViewPagerAdapter(context, listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new PageChangeListener());
	}
	/**
	 * ViewPager页面变化监听
	 */

	public class PageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				button.setVisibility(View.GONE);
				viewPagerPoint.check(R.id.view_pager_point_one);
			}
			if (arg0 == 1) {
				button.setVisibility(View.GONE);
				viewPagerPoint.check(R.id.view_pager_point_two);
			}
			if (arg0 == 2) {
				button.setVisibility(View.VISIBLE);
				viewPagerPoint.check(R.id.view_pager_point_three);
			}
		}

	}

}
