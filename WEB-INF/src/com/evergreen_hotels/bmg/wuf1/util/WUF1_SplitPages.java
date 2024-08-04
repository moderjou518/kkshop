package com.evergreen_hotels.bmg.wuf1.util;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * (功能說明)分頁功能<br>
 * (內容說明)
 * @author B31678 (Kevin Chiu)
 *
 * <pre>
 * CREATE DATE : 2010/12/21
 * <b>NOTES</b>       :
 *     A:  EXTRA LIBRARY
 *
 *     B:  REMARK
 * 
 * </pre>
 */
public class WUF1_SplitPages {
	HashMap<Object, Object> ListPagesResults = new HashMap<Object,Object>();	
	int PageSize = 0; 
	String TotalPages = "";
	String TotalRecords = "";		
	
	/**
	 * 建構子
	 * @param aryList - DB撈出來的資料 
	 */
	public WUF1_SplitPages(ArrayList aryList){	
		this(aryList,10);
	}

	/**
	 * 建構子
	 * @param aryList - DB撈出來的資料 
	 * @param PageSize - 每頁顯示筆數
	 */
	public WUF1_SplitPages(ArrayList aryList, int PageSize){
		this.PageSize = PageSize;
		processResult(aryList);
	}
	
	/**
	 * 將DB撈出來的資料整理分頁
	 * @param aryList - DB撈出來的資料
	 */
	public void processResult(ArrayList aryList){		
		int arySize = aryList.size();		
		int idx=1;
		int pageIdx=1;
		ArrayList<String[]> tempAry = new ArrayList<String[]>();
		for(int index =0 ; index < arySize  ;index++){    			
			if ( idx < this.PageSize ){
				tempAry.add((String []) aryList.get(index));
				idx++;    													
			}else{				
				tempAry.add((String []) aryList.get(index));
				this.ListPagesResults.put("page"+pageIdx,tempAry);
				tempAry = new ArrayList<String[]>();
				idx=1;
				pageIdx++;	
			}
		}				
		this.ListPagesResults.put("page"+pageIdx,tempAry);
		this.ListPagesResults.put("totalRecords",String.valueOf(arySize));
		this.ListPagesResults.put("totalPages",String.valueOf((int)Math.ceil((double)arySize / (double)this.PageSize)));
	}
	
	public void setPageSize(int PageSize){
		this.PageSize = PageSize;
	}
	
	public int getPageSize(){
		return this.PageSize;
	}
	
	public void setTotalPages(String TotalPages){
		this.TotalPages = TotalPages;
	}
	
	public String getTotalPages(){
		return this.TotalPages;
	}
	
	public void setTotalRecords(String TotalRecords){
		this.TotalRecords = TotalRecords;
	}
	
	public String getTotalRecords(){
		return this.TotalRecords;
	}
	
	public HashMap getListPagesResults(){
		return this.ListPagesResults;
	}
	
}
