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
public class MetadataConfig extends Model<MetadataConfig> {

	
	public List<Model> getMetadata() {
		return (List<Model>)get("metadata");
	}
	
	public Map<String, Model> metadataAsMap() {
		Map<String, Model> map = new HashMap();
		List<Model> metas = getMetadata();
		for (Model m : metas) {
			map.put(m.getId(), m);
		}
		return map;
	}
	public MetadataConfig setMetadata(List<Model> meta) {
		return set("metadata", meta);		
	}
	
	public List<Model> getDataTypes() {
		return (List<Model>)get("dataTypes");
	}
	
	public Map<String, Model> dataTypeAsMap() {
		Map<String, Model> map = new HashMap();
		List<Model> metas = getDataTypes();
		for (Model m : metas) {
			map.put(m.getId(), m);
		}
		return map;
	}
	public MetadataConfig setDataTypes(List<Model> meta) {
		return set("dataTypes", meta);		
	}
}
