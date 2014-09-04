package org.bc.sdak;

import java.util.Collections;
import java.util.List;

public class Page<T> {
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	public static int DEFAULT_PAGE_SIZE = 15;
	/**
	 * 当前页数，从1开始
	 */
	public int currentPageNo;
	public int pageSize;
	public List<T> result;
	public long totalCount;
	public boolean autoCount;
	public String pageUrl;
	
	public String order;
	
	public String orderBy;
	
	public boolean mergeResult = false;

	public Page(int pageSize) {
		this.currentPageNo = 1;

		this.pageSize = DEFAULT_PAGE_SIZE;

		this.result = Collections.emptyList();

		this.totalCount = -1L;

		this.autoCount = true;

		this.pageUrl = "errorPage.jsp";

		setPageSize(pageSize);
	}

	public Page(int pageSize, boolean autoCount) {
		this.currentPageNo = 1;

		this.pageSize = DEFAULT_PAGE_SIZE;

		this.result = Collections.emptyList();

		this.totalCount = -1L;

		this.autoCount = true;

		this.pageUrl = "errorPage.jsp";

		setPageSize(pageSize);
		setAutoCount(autoCount);
	}

	public Page() {
		this(DEFAULT_PAGE_SIZE);
	}

	public Page(long totalSize, int pageSize, List<T> data) {
		this.currentPageNo = 1;

		this.pageSize = DEFAULT_PAGE_SIZE;

		this.result = Collections.emptyList();

		this.totalCount = -1L;

		this.autoCount = true;

		this.pageUrl = "errorPage.jsp";

		this.pageSize = pageSize;
		this.totalCount = totalSize;
		this.result = data;
	}

	public long getTotalPageCount() {
		if (this.totalCount % this.pageSize == 0L) {
			return (this.totalCount / this.pageSize);
		}
		return (this.totalCount / this.pageSize + 1L);
	}

	public int getFirstOfPage() {
		return ((this.currentPageNo - 1) * this.pageSize + 1);
	}

	public int getLastOfPage() {
		return (this.currentPageNo * this.pageSize);
	}

	public static int getDEFAULT_PAGE_SIZE() {
		return DEFAULT_PAGE_SIZE;
	}

	public static void setDEFAULT_PAGE_SIZE(int dEFAULTPAGESIZE) {
		DEFAULT_PAGE_SIZE = dEFAULTPAGESIZE;
	}

	public int getCurrentPageNo() {
		return this.currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getResult() {
		return this.result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public long getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public boolean isAutoCount() {
		return this.autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	public String getPageUrl() {
		return this.pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public static String getAsc() {
		return "asc";
	}

	public static String getDesc() {
		return "desc";
	}
	
}
