package org.bc.sdak;

import java.util.Collections;
import java.util.List;

public class Page<T> {
	public static final String ASC = "asc";
	  public static final String DESC = "desc";
	  public static int DEFAULT_PAGE_SIZE = 10;
	  /**
	   * 当前页数，从1开始
	   */
	  protected int currentPageNo;
	  protected int pageSize;
	  protected List<T> result;
	  protected long totalCount;
	  protected boolean autoCount;
	  protected String pageUrl;
	  protected String formName;
//	  private long start;
	  protected String orderBy;
	  protected String order;

	  public Page(int pageSize)
	  {
	    this.currentPageNo = 1;

	    this.pageSize = DEFAULT_PAGE_SIZE;

	    this.result = Collections.emptyList();

	    this.totalCount = -1L;

	    this.autoCount = true;

	    this.pageUrl = "errorPage.jsp";

	    this.orderBy = null;
	    this.order = null;

	    setPageSize(pageSize);
	  }

	  public Page(int pageSize, boolean autoCount)
	  {
	    this.currentPageNo = 1;

	    this.pageSize = DEFAULT_PAGE_SIZE;

	    this.result = Collections.emptyList();

	    this.totalCount = -1L;

	    this.autoCount = true;

	    this.pageUrl = "errorPage.jsp";

	    this.orderBy = null;
	    this.order = null;

	    setPageSize(pageSize);
	    setAutoCount(autoCount);
	  }

	  public Page()
	  {
	    this(DEFAULT_PAGE_SIZE);
	  }

	  public Page(long totalSize, int pageSize, List<T> data)
	  {
	    this.currentPageNo = 1;

	    this.pageSize = DEFAULT_PAGE_SIZE;

	    this.result = Collections.emptyList();

	    this.totalCount = -1L;

	    this.autoCount = true;

	    this.pageUrl = "errorPage.jsp";

	    this.orderBy = null;
	    this.order = null;

	    this.pageSize = pageSize;
	    this.totalCount = totalSize;
	    this.result = data;
	  }

	  public long getTotalPageCount()
	  {
	    if (this.totalCount % this.pageSize == 0L) {
	      return (this.totalCount / this.pageSize);
	    }
	    return (this.totalCount / this.pageSize + 1L);
	  }

	  public int getFirstOfPage()
	  {
	    return ((this.currentPageNo - 1) * this.pageSize + 1);
	  }

	  public int getLastOfPage()
	  {
	    return (this.currentPageNo * this.pageSize);
	  }

	  public static int getDEFAULT_PAGE_SIZE()
	  {
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

//	  public long getStart()
//	  {
//	    return this.start;
//	  }

//	  public void setStart(long start) {
//	    this.start = start;
//	  }

	  public String getOrderBy() {
	    return this.orderBy;
	  }

	  public void setOrderBy(String orderBy) {
	    this.orderBy = orderBy;
	  }

	  public String getOrder() {
	    return this.order;
	  }

	  public void setOrder(String order) {
	    this.order = order;
	  }

	  public static String getAsc() {
	    return "asc";
	  }

	  public static String getDesc() {
	    return "desc";
	  }

	  public String getFormName() {
	    return this.formName;
	  }

	  public void setFormName(String formName) {
	    this.formName = formName;
	  }
}
