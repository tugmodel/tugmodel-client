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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tugmodel.client.mapper.Mapper;
import com.tugmodel.client.mapper.jackson.JacksonMappers;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.DefaultConfig;
import com.tugmodel.client.model.config.TugConfig;
import com.tugmodel.client.model.meta.Attribute;
import com.tugmodel.client.model.meta.Meta;
import com.tugmodel.client.tug.config.ConfigTug;
import com.tugmodel.client.tug.meta.MetaTug;

/**
 * TODO: 
 * 1. Consider a variable binding e.g. (modelType=T && modelId=X1, TugA), (modelType=T && modelId=X2, TugB).  
 * 2. The tug factory could become also a model. This way obtaining a tug will become itself tugable. 
 * 		 static Tug<MyModel> s = TugFactory.s.where("id=factory1").getTug(modelClass).
 * 
 */
public class TugFactory {
	
	// For the moment keep them in memory for fast retrieval altohugh a Tug could be added to fetch them from a DB.
	private static HashMap<Class<?>, Tug<?>> MODEL_CLASS_TO_TUG = new HashMap<Class<?>, Tug<?>>();
//	private static HashMap<Class<?>, Tug<?>> TEMPORARY = new HashMap<Class<?>, Tug<?>>();
	static {
		Mapper configMapper = JacksonMappers.getConfigReaderMapper();
		Tug baseTug = new BaseTug();
		baseTug.getConfig().setMapper(configMapper);
		MODEL_CLASS_TO_TUG.put(Model.class, baseTug);
		
		Tug configTug = new ConfigTug();
		configTug.getConfig().setMapper(configMapper);
		MODEL_CLASS_TO_TUG.put(DefaultConfig.class, configTug);

		final Map<String, Meta> metas = new HashMap();
		Meta m = new Meta();
		m.setId("Model");
		m.set("class", Model.class.getCanonicalName());
		Attribute a = new Attribute();
		a.setId("id");
		m.getAttributes().add(a);
		metas.put("Model", m);
		
		Tug metaTug = new MetaTug() {			
			public Meta fetchById(String id) {
				return metas.get(id);
			}
			public List<Meta> fetchAll() {
				return new ArrayList(metas.values());
			}
		};
		metaTug.getConfig().setMapper(configMapper);
		MODEL_CLASS_TO_TUG.put(Meta.class, metaTug);
		Meta.s = metaTug;  // TODO: Use reflection to set final field.
		
		// Config needs to be initially in the map. Others will be created with the help of it. 
		MODEL_CLASS_TO_TUG = initBindings();
	}
	
	public static <M extends Model<?>> Tug<M> getTug(Class<M> modelClass) {
		//return new FileTug<M>();
		
		
		Tug<M> tug = getNearestTug(modelClass);

		return tug;
	}
	
	private static HashMap<Class<?>, Tug<?>>  initBindings() {
		HashMap<Class<?>, Tug<?>> res = new HashMap();
		
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
				Tug tugInstance = (Tug) Class.forName((String)tug.get("class")).newInstance();
				tugInstance.setConfig(tc);
				res.put(Class.forName((String)model.get("class")), tugInstance);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}			
		}
		
		return res;
	}
	protected static <M extends Model<?>> Tug<M> getNearestTug(Class<M> modelClass) {
		Tug tug = MODEL_CLASS_TO_TUG.get(modelClass);
		if (tug != null)
			return tug;
		
		
		// Find nearest
		Class nearestParentModel = Model.class;
		for (Class c : MODEL_CLASS_TO_TUG.keySet()) {
			if (c.isAssignableFrom(modelClass) && nearestParentModel.isAssignableFrom(c)) {
				nearestParentModel = c;
			}
		}
		// TODO: Use config reader to search for stuff.
		tug = MODEL_CLASS_TO_TUG.get(nearestParentModel);
		// Remember lookup.		
		MODEL_CLASS_TO_TUG.put(modelClass, tug);
		return tug;
	}
}
