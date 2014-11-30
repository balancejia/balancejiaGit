package com.bb.dd.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bb.dd.R;
import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.service.LostAndFoundService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.DateTimePickerDialog;
import com.bb.dd.util.Util;
import com.topdt.application.entity.LostAndFoundView;

public class LostAndFoundFragment extends AbstractFragment implements OnClickListener{
	private static final String TAG = LostAndFoundFragment.class.getName();
	private Spinner lost_type;
	private EditText lost_contact;
	private EditText lost_xiangqing;
	private EditText lost_date;
	private Button lost_sub;
	private String type;
	private String contact;
	private String xiangqing;
	private String date;
	private ProgressDialog progress;
	private TextView title;
	private TextView gonggao;
	private FragmentView currentFragment;
	private LinearLayout container;
	private LinearLayout top_ll_right;
	private ImageView topRight;
	public LostAndFoundFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.lost_and_found);
		initView();
		return rootView;
	}

	private void initView() {
		
		container = (LinearLayout) ((Activity) _context).findViewById(R.id.container);
		title = (TextView) ((Activity) _context)
				.findViewById(R.id.top_tv_center);
		top_ll_right = (LinearLayout) ((Activity) _context)
				.findViewById(R.id.top_ll_right);
		topRight = (ImageView) ((Activity) _context)
				.findViewById(R.id.top_iv_right);
		
		
		gonggao=(TextView)rootView.findViewById(R.id.gonggao);
		gonggao.setText(Html.fromHtml("<font color=\"#0000ff\"><u>失物寻物公告</u></font>"));
		gonggao.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				title.setText("失物寻物公告");
				currentFragment = new LostGongGaoFragment(_context);
				container.removeAllViews();
				topRight.setVisibility(View.GONE);
				container.addView(currentFragment.getView(), LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
			}
		});
		
		// TODO Auto-generated method stub
		lost_type=(Spinner)rootView.findViewById(R.id.lost_type);
		lost_type.setPrompt("类型");
		lost_contact=(EditText)rootView.findViewById(R.id.lost_contact);
		lost_xiangqing=(EditText)rootView.findViewById(R.id.lost_xiangqing);
		lost_date=(EditText)rootView.findViewById(R.id.lost_date);
		lost_sub=(Button)rootView.findViewById(R.id.lost_sub);
		
		
		
		lost_date.setInputType(InputType.TYPE_NULL);  
		lost_date.setOnClickListener(new DateTimeOnClick());
		lost_sub.setOnClickListener(this);
		
	}
	private final class DateTimeOnClick implements OnClickListener {

		public void onClick(View v) {

			DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(

					(Activity) _context);

			dateTimePicKDialog.dateTimePicKDialog(lost_date, 0);

		}

	}
	@Override
	public void closed() {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//提交
		case R.id.lost_sub:
			type=lost_type.getSelectedItem().toString();
			contact=lost_contact.getText().toString().trim();
			xiangqing=lost_xiangqing.getText().toString().trim();
			date=lost_date.getText().toString().trim();
			if(contact==null||contact.equals("")){
				Toast.makeText(_context, "请输入联系方式!", 1).show();
				return;
			}
			if(xiangqing==null||xiangqing.equals("")){
				Toast.makeText(_context, "请输入经过详情!", 1).show();
				return;
			}
			if(date==null||date.equals("")){
				Toast.makeText(_context, "请选择日期!", 1).show();
				return;
			}
			if(xiangqing.length()>500){
				Toast.makeText(_context, "超过规定字数了，请做修改!", 1).show();
				return;
			}
			submit();
			break;

		default:
			break;
		}
	}
	// 创建提示框
		public ProgressDialog createProgress(Context context, String tip) {
			ProgressDialog progress = new ProgressDialog(context);
			progress.setMessage(tip);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return progress;
		}
	private void submit() {
		// TODO Auto-generated method stub
		final LostAndFoundView lostandfound=new LostAndFoundView();
		lostandfound.setSubmitFlag(type);
		lostandfound.setContactWay(contact);
		lostandfound.setAfterTheDetails(xiangqing);
		lostandfound.setHappendDate(date);
		lost_contact.setText("");
		lost_xiangqing.setText("");
		lost_date.setText("");
		progress = createProgress(_context, "提交中...");
		progress.show();
		/**
		 * 提交失物招领的方法
		 */
		new LostAndFoundService().lostAndFound(lostandfound,
				new AsyncOperator(){

					@Override
					public void onSuccess(Object obj) {
						// TODO Auto-generated method stub
						Boolean flagBoolean = (Boolean) obj;
						if (flagBoolean == true) {
							Util.t("提交成功");

						} else {
							Util.t("提交失败");
						}
						progress.dismiss();
						super.onSuccess(obj);
					}

					@Override
					public void onFail(String message) {
						// TODO Auto-generated method stub
						progress.dismiss();
						super.onFail(message);
					}
			
		});
	}
}
