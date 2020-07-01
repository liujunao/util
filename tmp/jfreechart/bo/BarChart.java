package com.wonders.framework.util.jfreechart.bo;

import java.io.Serializable;
import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;

@SuppressWarnings("serial")
public class BarChart  implements Serializable{
	
	public static RectangleEdge RIGHT = RectangleEdge.RIGHT;
	
	public static RectangleEdge LEFT = RectangleEdge.LEFT;
	
	public static RectangleEdge TOP = RectangleEdge.TOP;
	
	public static RectangleEdge BOTTOM = RectangleEdge.BOTTOM;
	
	
	//标题
	private String title="标题";

	//X轴标题
	private String xtitle="X轴标题";
	

	//Y轴标题
	private String ytitle="Y轴标题";
	
	//宽度
	private int width=500;
	
	//高
	private int height=400;
	
	//是否为3D效果
	private boolean is3D=false;
	
	//是否为单柱子
	private boolean isOne =true;
	
	// 是否显示图例(对于简单的柱状图必须是false)
	private boolean isLegend =false;
	
	//Y轴刻度是否为整数
	private boolean idInt = false;
	
	//图例位置，只有isLegend 为true时生效
	private RectangleEdge legendPosition = BOTTOM;
	
	//数据
	private CategoryDataset data;
	
	//数据list
	private List listData;

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

	public boolean isIs3D() {
		return is3D;
	}

	public void setIs3D(boolean is3d) {
		is3D = is3d;
	}

	public boolean isOne() {
		return isOne;
	}

	public void setOne(boolean isOne) {
		this.isOne = isOne;
	}

	public boolean isLegend() {
		return isLegend;
	}

	public void setLegend(boolean isLegend) {
		this.isLegend = isLegend;
	}

	public RectangleEdge getLegendPosition() {
		return legendPosition;
	}

	public void setLegendPosition(RectangleEdge legendPosition) {
		this.legendPosition = legendPosition;
	}

	public CategoryDataset getData() {
		return data;
	}

	public void setData(CategoryDataset data) {
		this.data = data;
	}

	public boolean isIdInt() {
		return idInt;
	}

	public void setIdInt(boolean idInt) {
		this.idInt = idInt;
	}

	public List getListData() {
		return listData;
	}

	public void setListData(List listData2) {
		this.listData = listData2;
	}
	
	
	
}
