package com.bb.dd.MapOverLay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.bb.dd.R;

public class BikeSiteIcon extends View {

	public static int w;
	public static int h;
	public int fix_x = 0;
	public int fix_y = 12;

	public static  Bitmap mBitmap;

	public BikeSiteIcon(Context context) {
		super(context);
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable._low);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		w = this.getWidth() / 2 - mBitmap.getWidth() / 2 + fix_x;
		h = this.getHeight() / 2 - mBitmap.getHeight() / 2 + fix_y;
		canvas.drawBitmap(mBitmap, w, h, null);
	}

}
