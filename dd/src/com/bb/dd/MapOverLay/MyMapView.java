package com.bb.dd.MapOverLay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.IndexMainActivity;


public class MyMapView extends MapView{

	public MyMapView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		GeoPoint g = this.getProjection().fromPixels(MyIcon.w, MyIcon.h);
//		Log.i("============", "wwwwwwwwww======" + MyIcon.w);
//		Log.i("============", "hhhhhhhhhh======" + MyIcon.h);
		switch (event.getAction()){
	        case MotionEvent.ACTION_DOWN:
	        //MainActivity.popview.setVisibility(View.GONE);
	        break;
	        case MotionEvent.ACTION_MOVE:
	        	if (IndexMainActivity.itemClicked==null){
	        		IndexMainActivity.setMyIconVisibility("mi", View.VISIBLE);
	        		IndexMainActivity.setMyIconVisibility("popview", View.GONE);
	        	}else{
	        		IndexMainActivity.setMyIconVisibility("all", View.GONE);
	        	}
	        	//MainActivity.mi.setVisibility(View.VISIBLE);
	        	//MainActivity.popview.setVisibility(View.GONE);
		    break;
	        case MotionEvent.ACTION_UP:
	        	if (IndexMainActivity.itemClicked==null){
	        		IndexMainActivity.setMyIconVisibility("all", View.VISIBLE);
	        	}else{
	        		IndexMainActivity.setMyIconVisibility("all", View.GONE);
	        	}
	        	//MainActivity.mi.setVisibility(View.VISIBLE);
	        	//MainActivity.popview.setVisibility(View.VISIBLE);
	        break;
        }
		
		//IndexMainActivity.getPosition(g);
		return super.onTouchEvent(event);
	}
}
