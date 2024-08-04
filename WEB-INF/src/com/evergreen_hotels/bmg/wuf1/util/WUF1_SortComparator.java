package com.evergreen_hotels.bmg.wuf1.util;

import java.util.Comparator;

/**
 * (功能說明)Menu 排序<br>
 * (內容說明)
 * @author B31678 (Kevin Chiu)
 * <pre>
 * CREATE DATE : 2010/12/21
 * <b>NOTES</b>       :
 *     A:  EXTRA LIBRARY
 * 
 *     B:  REMARK
 * </pre>
 */
public class WUF1_SortComparator implements Comparator {
	
	private int colIndex = 0;

	public WUF1_SortComparator(int colIndex) {
		this.colIndex = colIndex;
	}

	public int compare(Object element1, Object element2) {
		String[] tmpAry1 = (String[]) element1;
		String[] tmpAry2 = (String[]) element2;

		String lower1 = tmpAry1[this.colIndex].toLowerCase();
		String lower2 = tmpAry2[this.colIndex].toLowerCase();
		
		return lower1.compareTo(lower2);
	}
	
}