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

import com.tugmodel.client.model.Model;

/**
 * Configuration files.
 *  
 */
public class Config extends Model<Config> {
	
	// Be careful for the method name to match the property in the JSON.
	public MetadataConfig getMetadataConfig() {
		return (MetadataConfig) get("metadataConfig");
	}
	
	public Config setMetadataConfig(MetadataConfig mc) {
		return set("metadataConfig", mc);
	}
	
	public TugsConfig getTugsConfig() {
		return (TugsConfig) get("tugsConfig");
	}
	
	public Config setTugsConfig(TugsConfig value) {
		return set("tugsConfig", value);
	}

}