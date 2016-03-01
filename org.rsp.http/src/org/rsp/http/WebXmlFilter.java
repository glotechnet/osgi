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


public class WebXmlFilter
{
	String filterName;
	public String getFilterName() {
		return filterName;
	}

	Class<?> filterClass;
	String loadOnStartup;
	Dictionary<String,String> initParams = new Hashtable<String,String>();
	ArrayList<String> filterMappings = new ArrayList<String>(); 
	

	public Dictionary<String, String> getInitParams() {
		return initParams;
	}
	
	public WebXmlFilter addInitParam(String paramName, String paramValue)
	{
		if (paramName != null && paramValue != null)
			initParams.put(paramName, paramValue);
		return this;	
	}

	public ArrayList<String> getFilterMappings() {
		return filterMappings;
	}
	
	public Class<?> getFilterClass() {
		return filterClass;
	}

	@SuppressWarnings("unused")
	private WebXmlFilter(){}
	
	public WebXmlFilter(String filterName, Class<?> filterClass){
		this.filterName = filterName;
		this.filterClass = filterClass;
	}
	
	public WebXmlFilter addMapping(String mapping){
		
		if (mapping == null)
			return null;
		
		if (mapping != null && !mapping.startsWith("/"))
			mapping = "/"+mapping;
		
		filterMappings.add(mapping);
		return this;
	}
} 
