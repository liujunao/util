package com.wonders.framework.util.page;

import java.util.List;

public class PaginationSupport {
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

	// 可显示的页数
	private int total;
	// 数据总数
	private int records;
	// 须要显示的数据集
	private List<?> items;

	public PaginationSupport() {
	}

	public PaginationSupport(int records, List<?> items) {
		this.records = records;
		this.items = items;
	}

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

	public int getTotal() {
		if (rows > 0) {
			this.total = this.records % rows == 0 ? this.records / rows
					: this.records / rows + 1; // 计算总页数
		} else {
			this.total = 1;
		}
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}

}
