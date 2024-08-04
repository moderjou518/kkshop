/**
 * 
 */
package com.hms.web;

import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HMS_WEBACTION {
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException,Exception;
    
    public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException,Exception;
    public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException,Exception;
    public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException,Exception;
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException,Exception;
}
