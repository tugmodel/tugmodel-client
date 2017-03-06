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
import com.tugmodel.client.model.config.DefaultConfig;
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
	
	// Using 2 allows for lazy initialisation of tugs and model classes.
	private static HashMap<String, String> MODEL_TO_TUG = new HashMap<String, String>();
	private static HashMap<String, Tug<?>> MODEL_TO_TUG_INSTANCE = new HashMap<String, Tug<?>>();
	static {
		// Needed initially to read files.
		ConfigTug tug = new ConfigTug<>();
		tug.getConfig().setMapper(JacksonMappers.getConfigReaderMapper());
		MODEL_TO_TUG_INSTANCE.put(DefaultConfig.class.getCanonicalName(), tug);
		// Config needs to be initially in the map. Others will be created with the help of it. 
		MODEL_TO_TUG = initBindings();
		MODEL_TO_TUG_INSTANCE.clear();
	}
	
	// Needs to be LAZY so that no tugs are instantiated until necessary!.
	private static HashMap<String, String>  initBindings() {
		HashMap<String, String> res = new HashMap();
		
		DefaultConfig config = new DefaultConfig();		
		config.fetch();
		System.out.println(config.toString());
		
		// TODO: The config should create Metadata, Attribute, TugConfig objects but this would imply that the tug for these are known before reading. 
		//		which cames down to the code bellow or the alternative would be to set a dummy metatug until the system is up and then replace it and 
		//      same a dummy for TugFactory.getTug and this would allow working with pojo classes but is dangerous a little bit!!!.
		
		List<Map> modelList = (List<Map>)(((Map)config.get("metadata")).get("models"));
		Map<String, Map> modelMap = new HashMap();
		for (Map m : modelList) {
			modelMap.put((String)m.get("id"), m);
		}
		List<Map<String, Object>> tugs = (List<Map<String, Object>>)(((Map)config.get("tugConfig")).get("tugs"));
		for (Map<String, Object> tug : tugs) {
			Map model = modelMap.get((String)tug.get("modelId"));
			TugConfig tc = new TugConfig();
			for (String k : tug.keySet()) {
				tc.set(k, tug.get(k));
			}
			try {
//				Tug tugInstance = (Tug) Class.forName((String)tug.get("class")).newInstance();
//				tugInstance.setConfig(tc);
				//res.put(Class.forName((String)model.get("class")), tugInstance);
				res.put((String)model.get("class"), (String)tug.get("class"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}			
		}
		
		return res;
	}
	
	public static <M extends Model<?>> Tug<M> getTug(Class<M> modelClass) {
		
		Tug<M> tug = getNearestTug(modelClass);

		return tug;
	}
	
	protected static <M extends Model<?>> Tug<M> getNearestTug(Class<M> mClass) {
		String modelClass = mClass.getCanonicalName();
		Tug tug = MODEL_TO_TUG_INSTANCE.get(modelClass);
		if (tug != null)
			return tug;
		
		
		// Find nearest.
		String tugName = MODEL_TO_TUG.get(modelClass);
		if (tugName == null) {
			Class nearestParentClass = Model.class;
			for (String k : MODEL_TO_TUG.keySet()) {
				try {
					Class c = Class.forName(k);
					if (c.isAssignableFrom(mClass) && nearestParentClass.isAssignableFrom(c)) {
						nearestParentClass = c;
					}
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
			tugName = MODEL_TO_TUG.get(nearestParentClass.getCanonicalName());
			// Remember lookup.		
			MODEL_TO_TUG.put(modelClass, tugName);
		}

		// Reuse same tug as parent.
		tug = MODEL_TO_TUG_INSTANCE.get(tugName);
		if (tug == null) {  // Should not happen if got from a parent, put the binding also for the parent!.
			try {
				tug = (Tug)Class.forName(tugName).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		MODEL_TO_TUG_INSTANCE.put(modelClass, tug);

		return tug;
	}
}
