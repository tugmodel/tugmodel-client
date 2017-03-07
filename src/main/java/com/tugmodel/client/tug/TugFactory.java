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
package com.tugmodel.client.tug;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tugmodel.client.mapper.jackson.JacksonMappers;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.Config;
import com.tugmodel.client.model.config.TugConfig;
import com.tugmodel.client.tug.config.ConfigTug;

/**
 * TODO: 
 * 1. Consider a variable binding e.g. (modelType=T && modelId=X1, TugA), (modelType=T && modelId=X2, TugB).  
 * 2. The tug factory could become also a model. This way obtaining a tug will become itself tugable. 
 * 		 static Tug<MyModel> s = TugFactory.s.where("id=factory1").getTug(modelClass).
 * 
 */
public class TugFactory {
	
	// Using pair maps allows for lazy initialization of tugs and model classes.
	private static HashMap<String, String> MODEL_TO_TUG_ID = new HashMap<String, String>();
	private static HashMap<String, Tug<?>> TUG_ID_TO_TUG_INSTANCE = new HashMap<String, Tug<?>>();
	static {
		init(); // Bootstrap.
	}
	
	private static void init() {
		// Bootstraping starts and ends here. The reading of the default configuration file does not uses Meta or some other tugs.
		// Only after the bootstraping the Meta and other tugs can be used.
		ConfigTug tug = new ConfigTug();
		tug.getConfig().setMapper(JacksonMappers.getConfigReaderMapper());
		// Use this tug for all models until the bindings are initialized. We will only fetch Config model.
		MODEL_TO_TUG_ID.put(Model.class.getCanonicalName(), "configTug");
		MODEL_TO_TUG_ID.put(Config.class.getCanonicalName(), "configTug");
		TUG_ID_TO_TUG_INSTANCE.put("configTug", tug);

		
		Config config = new Config().fetch();  
		System.out.println(config.toString()); //config.hashCode()
			
		
		// NOTE: Save only config tug since the the config may come from a DB but the config deferal is done in the config file.
		List<Model> tugs = config.getTugsConfig().getTugs();
		for (Model tugModel : tugs) {
			String tugName = tugModel.asString("class");
			Model meta = config.getMetadataConfig().metadataAsMap().get(tugModel.get("modelId"));
			if ("Config".equals(tugModel.get("modelId"))) {
				MODEL_TO_TUG_ID.clear();
				MODEL_TO_TUG_ID.put(Model.class.getCanonicalName(), tugModel.getId());
				MODEL_TO_TUG_ID.put(meta.asString("class"), tugModel.getId());
				TUG_ID_TO_TUG_INSTANCE.clear();
				try {
					Tug newConfigTug = (Tug)Class.forName(tugName).newInstance();
					TugConfig tc = new TugConfig(config.getTugsConfig().mappersAsMap().get(tugModel.get("mapperId")));
					newConfigTug.setConfig(tc);
					TUG_ID_TO_TUG_INSTANCE.put(tugModel.getId(), newConfigTug);
					break;
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}				
			}
		}		
		
	}
	
	public static <M extends Model<?>> Tug<M> getTug(Class<M> modelClass) {
		
		Tug<M> tug = getNearestTug(modelClass);

		return tug;
	}
	
	private static volatile Config CONFIG = null; 
	private static Config parseConfig() {
		if (CONFIG != null) 
			return CONFIG;
		
		CONFIG = new Config().fetch();  
		for (Model tugModel : CONFIG.getTugsConfig().getTugs()) {
			String tugName = tugModel.asString("class");
			Model meta = CONFIG.getMetadataConfig().metadataAsMap().get(tugModel.get("modelId"));
			MODEL_TO_TUG_ID.put(meta.asString("class"), tugModel.getId());
			// No tug construction is done. Make it lazy.
		}
		return CONFIG;
	}
	
	protected static <M extends Model<?>> Tug<M> getNearestTug(Class<M> mClass) {
		String modelClass = mClass.getCanonicalName();
		String tugId = MODEL_TO_TUG_ID.get(modelClass);
		if (tugId != null) {
			Tug tug = TUG_ID_TO_TUG_INSTANCE.get(tugId);
			if (tug != null)
				return tug;			
		}
		
		Config config = parseConfig();
		
		if (tugId == null) {
			Class nearestParentClass = Model.class;
			for (String k : MODEL_TO_TUG_ID.keySet()) {
				try {
					Class c = Class.forName(k);
					if (c.isAssignableFrom(mClass) && nearestParentClass.isAssignableFrom(c)) {
						nearestParentClass = c;
					}
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
			tugId = MODEL_TO_TUG_ID.get(nearestParentClass.getCanonicalName());
			// Remember lookup.		
			MODEL_TO_TUG_ID.put(modelClass, tugId);
		}

		// Before constructing a tug let's check again. 
		Tug tug = TUG_ID_TO_TUG_INSTANCE.get(tugId);
		if (tug == null) {
			try {
				Model tugModel = config.getTugsConfig().tugsAsMap().get(tugId);
				tug = (Tug)Class.forName(tugModel.asString("class")).newInstance();
				Model mapper = config.getTugsConfig().mappersAsMap().get(tugModel.asString("mapperId"));
				TugConfig tc = new TugConfig(mapper);				
				tug.setConfig(tc);
				TUG_ID_TO_TUG_INSTANCE.put(tugId, tug);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return tug;
	}
}
