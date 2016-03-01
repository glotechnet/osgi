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
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.Servlet;



import org.eclipse.equinox.http.helper.BundleEntryHttpContext;
import org.eclipse.equinox.http.helper.ContextInitParametersServletAdaptor;
import org.eclipse.equinox.http.helper.ContextPathServletAdaptor;
import org.eclipse.equinox.http.helper.FilterServletAdaptor;
import org.eclipse.equinox.jsp.jasper.JspServlet;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;


public abstract class HttpActivator {

	public static final String BUNDLE_URI_NAMESPACE = "org.rsp.http.HttpActivator.BUNDLE_URI_NAMESPACE";
	public static final String BUNDLE_ENTRY = "org.rsp.http.HttpActivator.BUNDLE_ENTRY";
	public static final String WEBAPP_CONTEXTROOT = "org.rsp.webapp.contextroot";
	public static final String BUNDLE_ENTRY_DEFAULT_PATH = "/WebContent";
	public static final String BUNDLE_NAME = "org.rsp.http.HttpActivator.BUNDLE_NAME";
	
	protected String bundleEntry = null;
	protected String bundleUriNamespace = null;
	protected String webappContextRoot = null;
	protected String bundleName = null;
	
	protected WebXml webXml = new WebXml();
	
	boolean debug = false;
	
	public String getBundleEntry() {
		return bundleEntry;
	}

	public void setBundleEntry(String bundleEntry) {
		this.bundleEntry = bundleEntry;
		if (bundleEntry != null & !bundleEntry.startsWith("/"))
			bundleEntry = "/"+bundleEntry;
	}

	
	public String getBundleUriNamespace() {
		return bundleUriNamespace;
	}

	public void setBundleUriNamespace(String bundleUriNamespace) {
		
		this.bundleUriNamespace = bundleUriNamespace;
		if (bundleUriNamespace != null & !bundleUriNamespace.startsWith("/"))
			bundleUriNamespace = "/"+bundleUriNamespace;
	}

	private ServiceTracker httpServiceTracker;

	public void start(BundleContext context) throws Exception {
		start(context, webXml);
	}	
		
	public void start(BundleContext context, WebXml webXml) throws Exception {
		
		//ff is for non-bridge registration
		webappContextRoot = System.getProperty(WEBAPP_CONTEXTROOT);
		if (webappContextRoot == null)
			webappContextRoot = "/";
		if (!webappContextRoot.startsWith("/"))
			webappContextRoot = "/" + webappContextRoot;
		if (webappContextRoot.equals("/"))
			webappContextRoot = "";
		
		if (webXml == null) 
			webXml = new WebXml();
		this.webXml = webXml;	
		
		webXml.addContextParam(BUNDLE_NAME, context.getBundle().getSymbolicName());
		bundleName = context.getBundle().getSymbolicName();
		
		httpServiceTracker = new HttpServiceTracker(context);
		httpServiceTracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		httpServiceTracker.close();
	}

	private class HttpServiceTracker extends ServiceTracker {

		TreeMap<String,Properties> filterInitParamsMap = new TreeMap<String, Properties>(); 
		TreeMap<String,Filter> filterNameInstances = new TreeMap<String, Filter>();
		TreeMap<String,ArrayList<String>> filterNameMappings = new TreeMap<String,ArrayList<String>>();
		
		
		public HttpServiceTracker(BundleContext context) {
			super(context, HttpService.class.getName(), null);
		}
		
		public Object addingService(ServiceReference reference) {
			final HttpService httpService = (HttpService) context.getService(reference);
			try {
				
				if (debug) System.out.println("HttpActivator.addingService() " + bundleUriNamespace);
				
				Properties applicationContextInitParams = buildApplicationContextInitParams(webXml);
				
				readBundleUriNameSpace(applicationContextInitParams);
				
				readBundleEntry(applicationContextInitParams);
				
				HttpContext commonContext = new BundleEntryHttpContext(context.getBundle(), bundleEntry);
				
				buildFilters(webXml);
								
				try
				{
					if (bundleUriNamespace.equals("/") && !webappContextRoot.equals(""))
						bundleUriNamespace = "";
					
					if (webXml.containsWelcomeFiles())
					{
						if (debug) System.out.println("registered WelcomeFileFilterServlet "+webappContextRoot+bundleUriNamespace+" for bundle "+context.getBundle().getSymbolicName());

						Dictionary<String,Object> welcomeFileInfo = webXml.getWebXmlWelcomeFilesAsDictionary();
						
						httpService.registerServlet(webappContextRoot+bundleUriNamespace, WelcomeFileFilterServlet.class.newInstance(), welcomeFileInfo, commonContext);
						
						if (debug) System.out.println("registering resource mapping:"+webappContextRoot+bundleUriNamespace+"/resources for bundle "+context.getBundle().getSymbolicName());
						//register static resources under /WebContent
						httpService.registerResources(webappContextRoot+bundleUriNamespace+"/resources", "/", commonContext); 
					}
					else
					{
						if (debug) System.out.println("registering resource mapping:"+webappContextRoot+bundleUriNamespace+" for bundle "+context.getBundle().getSymbolicName());
						//register static resources under /WebContent
						httpService.registerResources(webappContextRoot+bundleUriNamespace, "/", commonContext); 
					}	
				
				} catch (Exception e) {
					System.out.println("The alias '"+webappContextRoot+bundleUriNamespace+"' is already in use.");
				}
				
				//for servlets
				TreeMap<String,WebXmlServlet> webXmlServlets = webXml.getWebXmlServlets();
				for (String name:webXmlServlets.keySet())
				{
					WebXmlServlet servlet = webXmlServlets.get(name);
					ArrayList<String> servletMappings = servlet.getServletMappings();
					//if there are no mappings, we still want to register it, use class name
					if (servletMappings == null || servletMappings.isEmpty())
					{
						servlet.addMapping(webappContextRoot+bundleUriNamespace+"/"+servlet.getServletClass().getName());
					}
					
					for (String mapping:servletMappings)
					{
						Servlet servletInstance = (servlet.getServletClass()).newInstance();
						
						//apply namespace for bundle
						mapping = webappContextRoot + bundleUriNamespace + mapping;
						//new constructor
						//System.out.println("Applying contextPathServletAdaptor to "+servlet.getServletClass().getName());
						
						servletInstance = new ContextPathServletAdaptor(servletInstance, webappContextRoot +bundleUriNamespace);  
						
						//apply application context init parameters
						//TODO: this overwrites context-param from outer web.xml
						if (applicationContextInitParams != null 
								&& !applicationContextInitParams.isEmpty())
						{
							servletInstance = new ContextInitParametersServletAdaptor(servletInstance, applicationContextInitParams);
						}
						
						//apply filters
						for (String filterName:servlet.getFilterNames())
						{
							for (String filterNameMapping:filterNameMappings.get(filterName))
							{
								filterNameMapping = ""+filterNameMapping;
								servletInstance = new FilterServletAdaptor(filterNameInstances.get(filterName), filterInitParamsMap.get(filterName), servletInstance);
							}
						}
						
						if (debug)
							System.out.println("registered servlet mapping:"+mapping+" for bundle "+context.getBundle().getSymbolicName());
						
						//register the servlet
						httpService.registerServlet(mapping, servletInstance, servlet.getInitParams(), commonContext);
					}
						
				}
				
				//for JSPs under /WebContent
				Servlet jspServlet = (Servlet) new JspServlet(context.getBundle(), bundleEntry);
				
				
				//apply namespace for bundle
				jspServlet = new ContextPathServletAdaptor(jspServlet, webappContextRoot + bundleUriNamespace); 
				
				//apply application context init parameters
				if (applicationContextInitParams != null 
						&& !applicationContextInitParams.isEmpty())
				{
					jspServlet = new ContextInitParametersServletAdaptor(jspServlet, applicationContextInitParams);
				}
				
				
				if (bundleUriNamespace.equals("/"))
					bundleUriNamespace = "";
				
				
				//TODO: apply and test JSP filters
				/*for (String filterName:filterNameInstances.keySet())
				{
					for (String filterNameMapping:filterNameMappings.get(filterName))
					{
						filterNameMapping = ""+filterNameMapping;
						jspServlet = new FilterServletAdaptor(filterNameInstances.get(filterName), filterInitParamsMap.get(filterName), jspServlet);
					}
				}*/
				
				//register the jspServlet
				//System.out.println("registered jsp mapping:"+webappContextRoot+bundleUriNamespace+"/*.jsp");
				httpService.registerServlet(webappContextRoot+bundleUriNamespace+"/*.jsp", jspServlet, null, commonContext); 
				
				if (debug)
					System.out.println("registered jsp mapping:"+webappContextRoot+bundleUriNamespace+"/*.jsp for bundle "+context.getBundle().getSymbolicName());
				
				
				//block access to /WEB-INF resources and return a SC_FORBIDDEN
				httpService.registerServlet(webappContextRoot+bundleUriNamespace+"/WEB-INF", new WebInfFilterServlet(), null, commonContext);
				
					
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return httpService;
		}

		public void removedService(ServiceReference reference, Object service) {
			final HttpService httpService = (HttpService) service;
			httpService.unregister(webappContextRoot+bundleUriNamespace); //$NON-NLS-1$
			httpService.unregister(webappContextRoot+bundleUriNamespace+"/*.jsp"); //$NON-NLS-1$
			TreeMap<String,WebXmlServlet> webXmlServlets = webXml.getWebXmlServlets();
			for (String name:webXmlServlets.keySet())
			{
				for (String mapping:webXmlServlets.get(name).getServletMappings())
				{
					mapping = webappContextRoot+bundleUriNamespace + mapping;
					httpService.unregister(mapping);
				}
			}
			super.removedService(reference, service);
		}
		
		protected Properties buildApplicationContextInitParams(WebXml webXml){
			//build a list of application context initparams
			Properties applicationContextInitParams = null;
			ArrayList<WebXmlContextParam> contextParams =  webXml.getContextParams();
			if (contextParams != null && !contextParams.isEmpty())
			{
				applicationContextInitParams = new Properties();
				for (WebXmlContextParam contextParam:contextParams)
				{
					applicationContextInitParams.put(contextParam.getParamName(), contextParam.getParamValue());
				}	
			}
			return applicationContextInitParams;
		}
		
		protected void readBundleUriNameSpace(Properties applicationContextInitParams){
			if (bundleUriNamespace == null)
			{
				//try to get it from the application context (when set for the bundle)
				if (applicationContextInitParams != null && !applicationContextInitParams.isEmpty())
				{
					String namespace = (String) applicationContextInitParams.get(BUNDLE_URI_NAMESPACE);
					if (namespace != null)
					{
						bundleUriNamespace = namespace;
						if (!bundleUriNamespace.startsWith("/"))
							bundleUriNamespace = "/" + bundleUriNamespace;
					}
				}
				if (bundleUriNamespace == null)
				{	//use the plug-in name by default
					bundleUriNamespace = "/"+context.getBundle().getSymbolicName();
				}	
			}
		}
		protected void readBundleEntry(Properties applicationContextInitParams){ 
			if (bundleEntry == null)
			{
				//try to get it from the application context (when set for the bundle)
				if (applicationContextInitParams != null && !applicationContextInitParams.isEmpty())
				{
					String entry = (String) applicationContextInitParams.get(BUNDLE_ENTRY);
					if (entry != null)
					{
						bundleEntry = entry;
						if (!bundleEntry.startsWith("/"))
							bundleEntry = "/" + bundleEntry;
					}
				}
				if (bundleEntry == null)
				{	//use the default
					bundleEntry = BUNDLE_ENTRY_DEFAULT_PATH;
				}
			}
		}
		
		public void buildFilters(WebXml webXml) throws IllegalAccessException, InstantiationException{
			//build a list of filters
			TreeMap<String,WebXmlFilter> webXmlFilters = webXml.getWebXmlFilters();
			//TreeMap<String,Filter> filterInstances = new TreeMap<String, Filter>();
			
			for (String name:webXmlFilters.keySet())
			{
				WebXmlFilter filter = webXmlFilters.get(name);
				Filter filterInstance = (Filter) (filter.getFilterClass()).newInstance();
				
				filterNameInstances.put(filter.getFilterName(), filterInstance);
				
				ArrayList<String> filterMappings = filter.getFilterMappings();
				if (filterMappings != null && ! filterMappings.isEmpty())
				{
					for (String mapping:filterMappings)
					{
						//filterInstances.put(mapping, filterInstance);
						
						if (!filterNameMappings.containsKey(filter.getFilterName()))
						{
							filterNameMappings.put(filter.getFilterName(), new ArrayList<String>());
						}
						filterNameMappings.get(filter.getFilterName()).add(mapping);
						
						//build a list of filterInitParams
						Dictionary<String,String> filterInitParams = filter.getInitParams();
						Properties filterInitParamProperties = null;
						
						if (filterInitParams != null && !filterInitParams.isEmpty()){
							filterInitParamProperties = new Properties();
							Enumeration<String> en = filterInitParams.keys();
							while (en.hasMoreElements())
							{
								String key = en.nextElement();
								filterInitParamProperties.put(key, filterInitParams.get(key));
								
							}
							filterInitParamsMap.put(filterInstance.getClass().getName(), filterInitParamProperties);
							//System.out.println(filterInitParamsMap);
						}
					}
				}
			}
		}

	}
	
	

}
