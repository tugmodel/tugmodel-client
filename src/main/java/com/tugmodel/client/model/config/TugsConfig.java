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
@SuppressWarnings("all")
public class TugsConfig extends Model<TugsConfig> {

	
	public List<Model> getTugs() {
		return (List<Model>)get("tugs");
	}
	
	public Map<String, Model> tugsAsMap() {
		Map<String, Model> map = new HashMap();
		List<Model> list = getTugs();
		for (Model m : list) {
			map.put(m.getId(), m);
		}
		return map;
	}
	
	public TugsConfig setTugs(List<Model> value) {
		return set("tugs", value);		
	}
	
	public List<Model> getMappers() {
		return (List<Model>)get("mappers");
	}
	
	public Map<String, Model> mappersAsMap() {
		Map<String, Model> map = new HashMap();
		List<Model> list = getMappers();
		for (Model m : list) {
			map.put(m.getId(), m);
		}
		return map;
	}
	
	public TugsConfig setMappers(List<Model> value) {
		return set("mappers", value);		
	}
}
