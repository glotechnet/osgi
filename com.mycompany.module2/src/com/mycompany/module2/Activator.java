package com.mycompany.module2;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rsp.http.HttpActivator;

public class Activator extends HttpActivator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		
		webXml.addContextParam(HttpActivator.BUNDLE_URI_NAMESPACE, "/module2");
		webXml.addServlet(MyServlet.class).addMapping("/myservlet");
		
		super.start(context);
	}
}
