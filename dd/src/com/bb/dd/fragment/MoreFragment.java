package com.bb.dd.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bb.dd.BikeApplication;
import com.bb.dd.R;
import com.bb.dd.userdefined.CenterRadioButton;
import com.bb.dd.util.BadHandler;

public class MoreFragment extends AbstractFragment implements OnClickListener {
	private static final String TAG = MoreFragment.class.getName();
	private RelativeLayout message;
	private RelativeLayout myfocus;
	private RelativeLayout updateData;
	private RelativeLayout uodateVer;
	private RelativeLayout clientFenxiang;
	private RelativeLayout last;
	private FragmentView currentFragment;
	private TextView title;
	private LinearLayout top_ll_right;
	private ImageView topRight;
	private LinearLayout container;
	private RadioGroup radioGroup;
	private CenterRadioButton moreRadio;
	private TextView synchroCount;
	private RelativeLayout synchroCountLayout;
	private RelativeLayout helpb;

	public MoreFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.more);
		initView();
		return rootView;
	}

	private void initView() {
		synchroCount = (TextView) rootView.findViewById(R.id.synchron_count);
		synchroCountLayout = (RelativeLayout)rootView.findViewById(R.id.rl_synchron_count);
		moreRadio=(CenterRadioButton)((Activity) _context).findViewById(R.id.radioFourthPage);
		message = (RelativeLayout) rootView.findViewById(R.id.message);
		message.setOnClickListener(this);
		myfocus = (RelativeLayout) rootView.findViewById(R.id.myfocus);
		myfocus.setOnClickListener(this);
		updateData = (RelativeLayout) rootView.findViewById(R.id.updateData);
		updateData.setOnClickListener(this);
		uodateVer = (RelativeLayout) rootView.findViewById(R.id.uodateVer);
		uodateVer.setOnClickListener(this);
		clientFenxiang = (RelativeLayout) rootView
				.findViewById(R.id.clientFenxiang);
		clientFenxiang.setOnClickListener(this);
		last = (RelativeLayout) rootView.findViewById(R.id.last);
		last.setOnClickListener(this);
		helpb = (RelativeLayout) rootView.findViewById(R.id.helpb);
		helpb.setOnClickListener(this);
       //数据更新数量
		if(BikeApplication.getInstance().getSynchroCount()>0){
			synchroCountLayout.setVisibility(View.VISIBLE);
			synchroCount.setText(BikeApplication.getInstance()
					.getSynchroCount() + "");
		}else{
			synchroCountLayout.setVisibility(View.GONE);
		}
		
		radioGroup = (RadioGroup) ((Activity) _context).findViewById(R.id.radioGroupTab);
		
		container = (LinearLayout) ((Activity) _context).findViewById(R.id.container);
		title = (TextView) ((Activity) _context)
				.findViewById(R.id.top_tv_center);
		top_ll_right = (LinearLayout) ((Activity) _context)
				.findViewById(R.id.top_ll_right);
		topRight = (ImageView) ((Activity) _context)
				.findViewById(R.id.top_iv_right);
	}

	@Override
	public void closed() {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		radioGroup.clearCheck();
		moreRadio.setTextColor(_context.getResources().getColor(
				R.color.bottom_text_bg_hui));
		switch (v.getId()) {
		// 消息发布
		case R.id.message:
			title.setText("信息发布");
			currentFragment = new AnnouncementFragment(_context);
			topRight.setVisibility(View.GONE);
			/*Drawable drawable = _context.getResources().getDrawable(
					R.drawable.top_back);
			topRight.setBackgroundDrawable(drawable);
			top_ll_right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					currentFragment = new AnnouncementFragment(_context);
					infaltView();
				}
			});*/
			break;
		// 我的关注
		case R.id.myfocus:
            
			title.setText("我的关注");
			topRight.setVisibility(View.GONE);
			currentFragment = new FocusFragment(_context);
			
			break;
		// 数据更新
		case R.id.updateData:

			title.setText("数据更新");
			 topRight.setVisibility(View.GONE);
			 currentFragment = new UpdateDataFragment(_context);
			
			break;
		// 版本更新
		case R.id.uodateVer:

			 title.setText("版本更新");
			 topRight.setVisibility(View.GONE);
			 currentFragment = new UpdateVersionFragment(_context);
			
			break;
		// 客户端分享
		case R.id.clientFenxiang:
			title.setText("客户端分享");
		    topRight.setVisibility(View.GONE);
		    currentFragment = new ShareFragment(_context);
			break;
		// 最后一个
		case R.id.last:
			title.setText("帮助指南");
		    topRight.setVisibility(View.GONE);
		    currentFragment = new HelpFragment(_context);
			break;
			// 自行车帮助信息
		case R.id.helpb:
			title.setText("公租自行车查询箱");
			topRight.setVisibility(View.GONE);
			currentFragment = new HelpbFragment(_context);
			break;

		default:
			break;
		}
		infaltView();
	}
	private void infaltView() {

		container.removeAllViews();
		container.addView(currentFragment.getView(), LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}
}
