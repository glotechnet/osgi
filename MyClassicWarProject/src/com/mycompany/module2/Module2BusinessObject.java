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
package com.mycompany.module2;

import java.util.ArrayList;
import java.util.List;

public class Module2BusinessObject {

	public List<String> myBusinessMethod(){
		
		ArrayList<String> report = new ArrayList<String>();
		report.add("Data from Module2BusinessObject here");
		report.add("and here");
		
		return report;
	}
}
