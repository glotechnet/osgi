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
package com.example.http.jsp2;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rsp.http.HttpActivator;

public class Activator extends HttpActivator implements BundleActivator{

	public void start(BundleContext context) throws Exception {
		
		webXml.addContextParam(HttpActivator.BUNDLE_URI_NAMESPACE, "/jsp2");
		webXml.addServlet(MyServlet.class).addMapping("/myservlet")
		      							  .addInitParam("myparamname", "myparamvalue")
		      							  .addInitParam("myparamname2", "myparamvalue2");
		webXml.addContextParam("mycontextparamname", "mycontextparamvalue")
			  .addContextParam("mycontextparamname2", "mycontextparamvalue2");
		
		webXml.addWelcomeFile("/index.jsp");
		
		super.start(context, webXml);
	}

}


