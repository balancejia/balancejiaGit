package com.bb.dd.fragment;

import android.content.Context;
import android.view.View;

import com.bb.dd.R;
import com.bb.dd.util.BadHandler;

public class NullFragment extends AbstractFragment{
	private static final String TAG = NullFragment.class.getName();
	public NullFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.nullfragment);
		return rootView;
	}

	@Override
	public void closed() {

	}
}
