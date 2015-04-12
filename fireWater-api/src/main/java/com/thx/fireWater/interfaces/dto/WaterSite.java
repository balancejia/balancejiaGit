/**
 *
 */
package com.thx.fireWater.interfaces.dto;

/**
 * 水源
 *
 * @author balancejia
 *
 */
public class WaterSite extends MapSite {

	private boolean usable;// 可用

	private boolean wuShui;// 无水
	private boolean louShui;// 漏水
	private boolean yalibuzu;// 压力不足
	private boolean xiushi;// 锈蚀
	private boolean maiya;// 埋压
	private boolean quanzhan;// 圈占
	private boolean huihuai;// 毁坏
	private boolean tajing;// 塌井

	private boolean broke;// 部件损坏

	private boolean jinggai;// 井盖
	private boolean chushuigai;// 出水盖
	private boolean dinggai;// 顶盖
	private boolean zhafa;// 闸阀
	private boolean chushuikou;// 出水口
	private boolean fangshuifa;// 放水阀
	private boolean shangshuifa;// 上水阀

	private String comments;// 备注

	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	public boolean isWuShui() {
		return wuShui;
	}

	public void setWuShui(boolean wuShui) {
		this.wuShui = wuShui;
	}

	public boolean isLouShui() {
		return louShui;
	}

	public void setLouShui(boolean louShui) {
		this.louShui = louShui;
	}

	public boolean isYalibuzu() {
		return yalibuzu;
	}

	public void setYalibuzu(boolean yalibuzu) {
		this.yalibuzu = yalibuzu;
	}

	public boolean isXiushi() {
		return xiushi;
	}

	public void setXiushi(boolean xiushi) {
		this.xiushi = xiushi;
	}

	public boolean isMaiya() {
		return maiya;
	}

	public void setMaiya(boolean maiya) {
		this.maiya = maiya;
	}

	public boolean isQuanzhan() {
		return quanzhan;
	}

	public void setQuanzhan(boolean quanzhan) {
		this.quanzhan = quanzhan;
	}

	public boolean isHuihuai() {
		return huihuai;
	}

	public void setHuihuai(boolean huihuai) {
		this.huihuai = huihuai;
	}

	public boolean isTajing() {
		return tajing;
	}

	public void setTajing(boolean tajing) {
		this.tajing = tajing;
	}

	public boolean isBroke() {
		return broke;
	}

	public void setBroke(boolean broke) {
		this.broke = broke;
	}

	public boolean isJinggai() {
		return jinggai;
	}

	public void setJinggai(boolean jinggai) {
		this.jinggai = jinggai;
	}

	public boolean isChushuigai() {
		return chushuigai;
	}

	public void setChushuigai(boolean chushuigai) {
		this.chushuigai = chushuigai;
	}

	public boolean isDinggai() {
		return dinggai;
	}

	public void setDinggai(boolean dinggai) {
		this.dinggai = dinggai;
	}

	public boolean isZhafa() {
		return zhafa;
	}

	public void setZhafa(boolean zhafa) {
		this.zhafa = zhafa;
	}

	public boolean isChushuikou() {
		return chushuikou;
	}

	public void setChushuikou(boolean chushuikou) {
		this.chushuikou = chushuikou;
	}

	public boolean isFangshuifa() {
		return fangshuifa;
	}

	public void setFangshuifa(boolean fangshuifa) {
		this.fangshuifa = fangshuifa;
	}

	public boolean isShangshuifa() {
		return shangshuifa;
	}

	public void setShangshuifa(boolean shangshuifa) {
		this.shangshuifa = shangshuifa;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
