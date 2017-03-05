/*
 * Copyright (c) 2017- Cristian Donoiu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tugmodel.client.model.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tugmodel.client.model.Model;

/**
 * Configuration files.
 *  
 */
public class DefaultConfig extends Model<DefaultConfig> {
	// Since Meta may not be available while reading configuration file.
	public Map<String, ?> getExtraAttributes() {
		return data();
	}	
	
//	// No sense to complicate API since we are interested only in raw data here.  
//	public Map getTugs() {
//		List<Map> tugList = (List<Map>)(((Map)this.get("tugConfig")).get("tugs"));
//		Map <String, Map> tugs = new HashMap();
//		for (Map tug : tugList) {
//			tugs.put((String)tug.get("id"), tug);
//		}
//		return tugs;
//	}
}
