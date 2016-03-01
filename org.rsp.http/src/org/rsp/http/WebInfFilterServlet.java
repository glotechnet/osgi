package org.rsp.http;

import java.io.IOException;


import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebInfFilterServlet extends HttpServlet {

private static final long serialVersionUID = 1L;
	
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		res.sendError(HttpServletResponse.SC_FORBIDDEN);	
	}
	
}
