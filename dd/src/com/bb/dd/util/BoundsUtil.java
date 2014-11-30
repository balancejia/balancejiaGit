package com.bb.dd.util;


import java.math.BigDecimal;

public class BoundsUtil {
	private static double DEF_PI = 3.14159265359; // PI	
	private static double DEF_2PI = 6.28318530712; // 2*PI
	private static double DEF_PI180 = 0.01745329252; // PI/180
	private static double DEF_R = 6370693.5; // radius of earth
	private static final int DEFAULT_DIV_SCALE = 6;
    /**
     * 1000表示1公里，111表示同经度时，纬度相差一度，距离就相差111公里
     * 
     * @param lat 当前位置纬度
     * @param lag 当前位置经度
     * @param r 半径，单位M（附近rM）
     * @return Bounds
     */
    public static Bounds conversion(Double lat, Double lag, Integer r) {
        String l = String.valueOf(1000 * 111);
        String latx = new BigDecimal(String.valueOf(r)).divide(new BigDecimal(l), DEFAULT_DIV_SCALE,
                                     BigDecimal.ROUND_HALF_EVEN).toString();
        String lagx = new BigDecimal(latx).divide(new BigDecimal(String.valueOf(Math.cos(lat))), DEFAULT_DIV_SCALE,
                                     BigDecimal.ROUND_HALF_EVEN).toString();
        Double latN = lat + Math.abs(Double.valueOf(latx));
        Double latS = lat - Math.abs(Double.valueOf(latx));
        Double lagE = lag + Math.abs(Double.valueOf(lagx));
        Double lagW = lag - Math.abs(Double.valueOf(lagx));
        Bounds bounds = new Bounds();
        bounds.setLagE(lagE);
        bounds.setLagW(lagW);
        bounds.setLatN(latN);
        bounds.setLatS(latS);
        return bounds;
    }
    
	public static double GetShortDistance(double lon1, double lat1, double lon2,double lat2) {
		double ew1, ns1, ew2, ns2;
		double dx, dy, dew;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
		if (dew > DEF_PI)
			dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
			dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	public static double GetLongDistance(double lon1, double lat1, double lon2,double lat2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
				* Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
	}

    public static void main(String[] args) {
    	Double lon = 112.57566313912;
    	Double lat = 37.741824087348;
    	Bounds bounds = BoundsUtil.conversion(lat,lon,3200);
    	String str = "SELECT * FROM bike_site t where t.sign3 >= "+bounds.getLatS()+" and t.sign3 <= "+bounds.getLatN()
    				+" and t.sign4 >= "+bounds.getLagW()+" and t.sign4 <= "+bounds.getLagE()+"";
    	System.out.println(str);
    	
//    	double mLon1 = 112.567360277778; // point1经度
//    	double mLat1 = 37.871693611111; // point1纬度
//    	double mLon2 = 112.576528055556; // point2经度
//    	double mLat2 = 37.871709444444; // point2纬度
//    	double distance = GetShortDistance(mLon1, mLat1, mLon2, mLat2);
//    	System.out.println(distance);
//    	System.out.println(Double.valueOf(distance).intValue());
//    	distance = GetLongDistance(mLon1, mLat1, mLon2, mLat2);
//    	System.out.println(distance);
//    	System.out.println(Double.valueOf(distance).intValue());
	}
}
