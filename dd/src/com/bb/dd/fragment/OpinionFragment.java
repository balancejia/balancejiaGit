package com.bb.dd.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bb.dd.R;
import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Util;
import com.topdt.coal.entity.Opinion;

public class OpinionFragment extends AbstractFragment implements
		OnClickListener {
	private static final String TAG = OpinionFragment.class.getName();
	private Spinner spinner;
	private String feedback;
	private EditText opinion;
	private EditText contact_way;
	private Button btn;
	private ProgressDialog progress;

	public OpinionFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.opinion);
		initView();
		return rootView;
	}

	@Override
	public void closed() {

	}

	private void initView() {
		spinner = (Spinner) rootView.findViewById(R.id.need);
		spinner.setPrompt("反馈类型");
		spinner.setOnItemSelectedListener(feedBackStyle);
		opinion = (EditText) rootView.findViewById(R.id.problem_opinion);
		contact_way = (EditText) rootView.findViewById(R.id.contact_way);
		btn = (Button) rootView.findViewById(R.id.button);
		btn.setOnClickListener(this);

	}

	private OnItemSelectedListener feedBackStyle = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String style = (String) arg0.getItemAtPosition(arg2);
			feedback = style;
		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

	// 创建提示框
	public ProgressDialog createProgress(Context context, String tip) {
		ProgressDialog progress = new ProgressDialog(context);
		progress.setMessage(tip);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		return progress;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			submit();
			break;
		default:
			break;

		}

	}

	// 提交意见
	public void submit() {
		if(opinion.getText().toString().trim()==null||opinion.getText().toString().trim().equals("")){
			Toast.makeText(_context, "请输入意见或建议!", 1).show();
			return;
		}
		if(contact_way.getText().toString().trim()==null||contact_way.getText().toString().trim().equals("")){
			Toast.makeText(_context, "请输入联系方式!", 1).show();
			return;
		}
		if(opinion.getText().toString().trim().length()>2000){
			Toast.makeText(_context, "超过规定字数了，请做修改!", 1).show();
			return;
		}
		final Opinion clientOpinion = new Opinion();
		clientOpinion.setOpinionContent(opinion.getText().toString().trim());
		clientOpinion.setOpinionStyle(feedback);
		clientOpinion.setContact(contact_way.getText().toString().trim());
		clientOpinion.setMobileType(android.os.Build.MODEL);
		opinion.setText("");
		contact_way.setText("");
		progress = createProgress(_context, "反馈中...");
		progress.show();
		/**
		 * 提交意见内容的方法
		 */
		new BikeSiteUserService().saveOpinion(clientOpinion,
				new AsyncOperator() {

					@Override
					public void onSuccess(Object obj) {
						Boolean flagBoolean = (Boolean) obj;
						if (flagBoolean == true) {
							Util.t("非常感谢你的意见");

						} else {
							Util.t("提交失败");
						}
						progress.dismiss();
						super.onSuccess(obj);
					}

					@Override
					public void onFail(String message) {
						progress.dismiss();
						super.onFail(message);
					}

				});
	}
}
