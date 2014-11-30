package com.bb.dd.MapOverLay;

import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;

public class MapPointOverLayItem extends ItemizedOverlay<OverlayItem> {

	MapView mapView;

	public MapPointOverLayItem(Drawable marker, MapView mapView) {
		super(marker, mapView);
		this.mapView = mapView;
	}
}
