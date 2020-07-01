package com.wonders.framework.util.grid;

/**
 * 为接管前台数据，特定义了DataRequest类，此中的属性跟页面传递过来的属性一一对应
 * 
 * @author Augus.Gao
 * 
 */
public class JQGridRequest {
	// 是否是搜刮恳求
	private boolean _search;
	// 已经发送的恳求的次数
	private String nd;
	// 当前页码
	private int page;
	// 开始行数
	private int startIndex;
	// 页面可显示行数
	private int rows;
	// 用于排序的列名
	private String sidx;
	// 排序的体式格式desc/asc
	private String sord;

	public void set_search(boolean _search) {
		this._search = _search;
	}

	public void setNd(String nd) {
		this.nd = nd;
	}

	public String getNd() {
		return nd;
	}

	public boolean get_search() {
		return _search;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getRows() {
		return rows;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getSord() {
		return sord;
	}

	public int getStartIndex() {
		return (this.page - 1) * this.rows;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
}
