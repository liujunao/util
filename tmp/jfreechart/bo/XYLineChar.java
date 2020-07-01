package com.wonders.framework.util.jfreechart.bo;

import java.util.Map;
/**
 * 时间曲线图
 * @author Administrator
 *
 */
public class XYLineChar {
	//标题
	private String title;
	//x轴标题
	private String xtitle;
	//y轴标题
	private String ytitle;
	//宽度
	private int width;
	//高度
	private int height;
	//数据
	private Map<String, Map<Object, Number>> dataMap;
	//是否显示图例
	private boolean islegend; 
	//格式 year/month/day
	private String format;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getXtitle() {
		return xtitle;
	}
	public void setXtitle(String xtitle) {
		this.xtitle = xtitle;
	}
	public String getYtitle() {
		return ytitle;
	}
	public void setYtitle(String ytitle) {
		this.ytitle = ytitle;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Map<String, Map<Object, Number>> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, Map<Object, Number>> dataMap) {
		this.dataMap = dataMap;
	}
	public boolean isIslegend() {
		return islegend;
	}
	public void setIslegend(boolean islegend) {
		this.islegend = islegend;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	
	
	

}
