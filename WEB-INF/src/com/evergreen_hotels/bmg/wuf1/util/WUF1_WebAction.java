package com.evergreen_hotels.bmg.wuf1.util;

import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WUF1_WebAction {
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException,Exception;
}
