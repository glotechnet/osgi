/*
 * Copyright 2006-2009 Wolfgang Gehner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rsp.example.http.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	ServletConfig config;
	
	public void init(ServletConfig config){
		this.config = config;
	}
	
	@SuppressWarnings("unchecked")
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		
		
		res.getWriter().write("myservlet response<br>");
		Enumeration en = config.getInitParameterNames();
		while (en.hasMoreElements())
		{
			String paramName = (String) en.nextElement();
			res.getWriter().write(paramName+":"+config.getInitParameter(paramName)+"<br>");
		}
		
		en = config.getServletContext().getInitParameterNames();
		while (en.hasMoreElements())
		{
			String paramName = (String) en.nextElement();
			res.getWriter().write(paramName+":"+config.getServletContext().getInitParameter(paramName)+"<br>");
		}
		//res.getWriter().write(""+config.getServletContext().getInitParameter("org.apache.tiles.impl.BasicTilesContainer.DEFINITIONS_CONFIG"));
		//res.getWriter().write(""+config.getServletContext().getInitParameter("org.apache.tiles.impl.BasicTilesContainer.DEFINITIONS_CONFIG2"));
	}
}
