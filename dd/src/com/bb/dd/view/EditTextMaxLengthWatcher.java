package com.bb.dd.view;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import com.bb.dd.util.Util;

public class EditTextMaxLengthWatcher implements TextWatcher {

	//最大长度
	private int maxLen;
	
	//监听改变的文本框
	private EditText editText;
	
	/**
	 * 构造函数
	 */
	public EditTextMaxLengthWatcher(int maxLen,EditText editText){
		this.maxLen = maxLen;
		this.editText = editText;
	}
	
	@Override
	public void onTextChanged(CharSequence ss, int start, int before, int count) {
		Editable editable = editText.getText();
		int len = editable.length();
		//大于最大长度
		if(len > maxLen){
			int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0, maxLen);
            editText.setText(newStr);
            editable = editText.getText();
            //新字符串长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if(selEndIndex > newLen){
            	selEndIndex = editable.length();
            }
            //设置新的光标所在位置
            Selection.setSelection(editable, selEndIndex);
            Util.t("你输入的字数已经超过了限制！");
		}
	}
	
	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {

	}

}
