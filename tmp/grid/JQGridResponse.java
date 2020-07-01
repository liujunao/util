package com.wonders.framework.util.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * 给前台发送数据也定义了响应的DataResponse类
 * 
 * @author Augus.Gao
 * 
 */
public class JQGridResponse<T> {
	// 可显示的页数
	private int total;
	// 当前页数
	private int page;
	// 数据总数
	private int records;
	// 须要显示的数据集
	private List<T> items;

	public JQGridResponse() {
		// total = 0;
		// page = 0;
		// records = 0;
		// rows = new ArrayList<T>();
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getTotal() {
		// return (this.records+this.page-1)/this.page;
		return this.total;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public int getRecords() {
		return records;
	}

	public void setItems(List<T> items, int row) {
		this.items = items;
		if (row > 0) {
			this.total = this.records % row == 0 ? this.records / row
					: this.records / row + 1; // 计算总页数
		} else {
			this.total = 1;
		}
	}

	// public void setRows(List<T> list) {
	// this.rows = list;
	// }

	public List<T> getItems() {
		return items;
	}
}
