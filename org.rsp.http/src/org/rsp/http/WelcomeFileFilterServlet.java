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
package org.rsp.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


public class WelcomeFileFilterServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	public static final String WELCOME_FILES = "org.rsp.http.WELCOME_FILES"; 
	
	List<String> welcomeFileList = new ArrayList<String>();
	String appRoot = null;
	String bundleUriNamespace = null;
	
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{	
		
		if (req.getPathInfo() != null && req.getPathInfo().equals("/"))
		{
			if (welcomeFileList != null && !welcomeFileList.isEmpty())
			{
				//use the first one for now:
				for(String welcomeFile:welcomeFileList)
				{
					try
					{
						if (welcomeFile.startsWith("/")) welcomeFile = welcomeFile.substring(1);
						
						RequestDispatcher dispatcher = req.getRequestDispatcher(welcomeFile);
						//TODO: check if resource exists before forwarding
						dispatcher.forward(req, res);
						return;
					}	
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			else
			{	
				req.getRequestDispatcher(req.getServletPath()+"/resources/index.html").forward(req,res); //Tomcat also defaults to index.jsp
				return;
			}	
		}	
		else
		{
			//no welcome file, trying to forward to remapped resource: /resources"+req.getPathInfo()
			req.getRequestDispatcher(req.getServletPath()+"/resources"+req.getPathInfo()).forward(req,res);
		}	
 	}

	public void init(ServletConfig config) throws ServletException {

		String welcomeFilesDelimited = config.getInitParameter(WELCOME_FILES);
		if (welcomeFilesDelimited != null)
			welcomeFileList = Arrays.asList(StringUtils.split(welcomeFilesDelimited, ";"));
		
		appRoot = config.getInitParameter(HttpActivator.WEBAPP_CONTEXTROOT);
		if (appRoot == null || appRoot.equals("/"))
			appRoot = "";
		
		bundleUriNamespace = config.getInitParameter(HttpActivator.BUNDLE_URI_NAMESPACE);
		if (bundleUriNamespace == null || bundleUriNamespace.equals("/"))
			bundleUriNamespace = "";
	}

}
