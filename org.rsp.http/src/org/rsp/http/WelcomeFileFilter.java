package org.rsp.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class WelcomeFileFilter implements javax.servlet.Filter{

	public static final String WELCOME_FILES = "org.rsp.http.WELCOME_FILES"; 
	
	List<String> welcomeFileList = new ArrayList<String>();
	String appRoot = null;
	String bundleUriNamespace = null;
	
	
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest hreq = (HttpServletRequest) req;   
		String uri = hreq.getRequestURI();
		String contextPath = hreq.getContextPath();
		String servletPath = hreq.getServletPath();
		System.out.println("WelcomeFileFilter.URI"+uri);
		System.out.println("WelcomeFileFilter.contextPath"+contextPath);
        System.out.println("WelcomeFileFilter.servletPath"+servletPath);
        
		if ((appRoot+bundleUriNamespace).equals(uri))
		{
			System.out.println("is welcomeURL");
		}
			
        chain.doFilter(req, res); 
		
	}

	public void init(FilterConfig config) throws ServletException {

		String welcomeFilesDelimited = config.getInitParameter(WELCOME_FILES);
		if (welcomeFilesDelimited != null)
			welcomeFileList = Arrays.asList(StringUtils.split(welcomeFilesDelimited, ";"));
		appRoot = config.getInitParameter(HttpActivator.WEBAPP_CONTEXTROOT);
		if (appRoot == null || appRoot.equals("/"))
			appRoot = "";
		
		bundleUriNamespace = config.getInitParameter(HttpActivator.BUNDLE_URI_NAMESPACE);
		if (bundleUriNamespace == null || bundleUriNamespace.equals("/"))
			bundleUriNamespace = "";
		
		System.out.println("WelcomeFileFilter.init"+welcomeFilesDelimited);
        
		
	}

}
