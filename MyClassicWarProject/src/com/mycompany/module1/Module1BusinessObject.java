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

import java.util.ArrayList;

public class Module1BusinessObject {

	public static ArrayList<Stock> getStocks() {

		ArrayList<Stock> list = new ArrayList<Stock>();
		list.add(new Stock("Bank of America", getStockPrice(false)));
		list.add(new Stock("AIG", getStockPrice(true)));
		return list;
	}

	protected static Double getStockPrice(boolean discounted) {

		return Math.random() * (discounted ? 1 : 10);
	}
}
