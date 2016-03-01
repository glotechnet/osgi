package com.mycompany.myapp.root;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rsp.http.HttpActivator;

public class Activator extends HttpActivator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		
		webXml.addContextParam(BUNDLE_URI_NAMESPACE, "");
		
		webXml.addWelcomeFile("/home.jsp");
		
		super.start(context);
		
	}

	public void stop(BundleContext context) throws Exception {
	}

}
