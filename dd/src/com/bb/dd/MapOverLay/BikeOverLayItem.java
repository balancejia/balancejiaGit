package com.bb.dd.MapOverLay;

import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.IndexMainActivity;

public class BikeOverLayItem extends ItemizedOverlay<OverlayItem> {

	MapView mapView;
	IndexMainActivity mainActivity;
	boolean flag;
	public BikeOverLayItem(Drawable marker, MapView mapView,IndexMainActivity mainActivity,boolean flag) {
		super(marker, mapView);
		this.mapView = mapView;
		this.mainActivity = mainActivity;
		this.flag=flag;
	}

	@Override
	public boolean onTap(int index) {
		if(flag){
			OverlayItem item = this.getItem(index);
			if (item instanceof MyOverlayItem) {
				MyOverlayItem myOverlayItem = (MyOverlayItem) item;
				mainActivity.showDetail(myOverlayItem);
			}
		}
		
		return false;
	}
	
	@Override
	public boolean onTap(GeoPoint pt , MapView mMapView){
		if(flag){
			mainActivity.closeDetail();
		}
		return false;
	}

}
