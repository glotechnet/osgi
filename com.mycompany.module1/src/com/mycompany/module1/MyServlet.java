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
package com.mycompany.module1;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	Log log = LogFactory.getLog(MyServlet.class.getName());
	
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		log.debug("service() (OSGi)");
		
		for (Stock stock:Module1BusinessObject.getStocks())
		{	
			res.getWriter().write("- "+stock.getName() + " $" +new DecimalFormat("#0.00").format(stock.getPrice())+"<br>");
		}	
	}
}
