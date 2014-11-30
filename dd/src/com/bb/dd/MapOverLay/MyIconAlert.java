package com.bb.dd.MapOverLay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.bb.dd.R;

public class MyIconAlert extends View{

	public static int w;
	public static int h;
	public int fix_x = 3;
	public int fix_y = 15;

	public static  Bitmap mBitmap;

	public MyIconAlert(Context context) {
		super(context);
		//R.layout.pop_view
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.login_bt_bg);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		w = this.getWidth() / 2 - mBitmap.getWidth() / 2 + fix_x;
		h = this.getHeight() / 2 - MyIcon.mBitmap.getHeight() - mBitmap.getHeight() + fix_y;
		canvas.drawBitmap(mBitmap, w, h, null);
	}
}
