package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_PrepareAccess;

public class testDate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("go");
		//getDatesBetweenDate("2020-03-01", "2020-03-10");
		getDatesBetweenDate("20200301", "20200310");
		System.out.println("end");
	}
	
	public static List<LocalDate> getDatesBetweenDate(String s, String e) {
		//s = "2014-05-01";
		//e = "2014-05-10";
		String sDate = s.substring(0,4) + "-" + s.substring(4,6) + "-" + s.substring(6,8);
		String eDate = e.substring(0,4) + "-" + e.substring(4,6) + "-" + e.substring(6,8);
		
		System.out.println("sDate: " + sDate);
		System.out.println("eDate: " + eDate);
		
		
		LocalDate start = LocalDate.parse(sDate);
		LocalDate end = LocalDate.parse(eDate);
		System.out.println("1");
		List<LocalDate> totalDates = new ArrayList<LocalDate>();
		while (!start.isAfter(end)) {			
			System.out.println(start.toString() + ":" + start.getDayOfWeek().getValue());
			//System.out.println();
			
		    totalDates.add(start);
		    start = start.plusDays(1);   		    
		}
		
		
		
		System.out.println("2");
		
		return totalDates;
	}
}
