package com.wonders.framework.util.jfreechart.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class PieChart  implements Serializable{
	
	//标题
	public String title="饼图";
	
	//宽度
	public int width=500;
	
	//高
	public int height=400;
	
	//数据
	public Map<String,Object> data;
	
	//list数据
	public List listData;
	
	//是否为3D效果
	public boolean is3D=false;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public boolean isIs3D() {
		return is3D;
	}

	public void setIs3D(boolean is3d) {
		is3D = is3d;
	}

	public List getListData() {
		return listData;
	}

	public void setListData(List listData) {
		this.listData = listData;
	}
	

}
