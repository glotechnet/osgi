package org.rsp.example.resource.css;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rsp.resource.ResourceActivatorBase;

public class Activator extends ResourceActivatorBase implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		
		this.setBundleUriNamespace("/css");
		super.start(context);
	}
	

}
