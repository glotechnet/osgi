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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;



public class WebXmlServlet
{
	String servletName;
	Class<? extends Servlet> servletClass;
	String loadOnStartup;
	Dictionary<String,String> initParams = new Hashtable<String,String>();
	ArrayList<String> servletMappings = new ArrayList<String>(); 
	ArrayList<String> filterNames = new ArrayList<String>();

	public ArrayList<String> getFilterNames() {
		return filterNames;
	}

	public Dictionary<String, String> getInitParams() {
		return initParams;
	}
	
	public String getLoadOnStartup() {
		return initParams.get("load-on-startup");
	}

	public WebXmlServlet setLoadOnStartup(String loadOnStartup) {
		if (loadOnStartup != null)
			initParams.put("load-on-startup", loadOnStartup);
		return this;
	}
	public WebXmlServlet addInitParam(String paramName, String paramValue)
	{
		if (paramName != null && paramValue != null)
			initParams.put(paramName, paramValue);
		return this;	
	}

	public ArrayList<String> getServletMappings() {
		return servletMappings;
	}
	
	public Class<? extends Servlet> getServletClass() {
		return servletClass;
	}

	@SuppressWarnings("unused")
	private WebXmlServlet(){}
	
	public WebXmlServlet(String servletName, Class<? extends Servlet> servlet){
		this.servletName = servletName;
		this.servletClass = servlet;
	}
	
	public WebXmlServlet addMapping(String mapping){
		
		if (mapping == null)
			return null;
		
		if (mapping != null && !mapping.startsWith("/"))
			mapping = "/"+mapping;
		
		servletMappings.add(mapping);
		return this;
	}
	
	public WebXmlServlet addFilter(String filterName){
		filterNames.add(filterName);
		return this;
		
	}
} 
