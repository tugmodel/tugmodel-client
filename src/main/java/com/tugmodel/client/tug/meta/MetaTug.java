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
package com.tugmodel.client.tug.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tugmodel.client.list.ModelList;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.Config;
import com.tugmodel.client.model.meta.Attribute;
import com.tugmodel.client.model.meta.Meta;
import com.tugmodel.client.tug.BaseTug;

/**
 * Provides metadata.
 */
public class MetaTug extends BaseTug<Meta> {
	private Map<String, Meta> metas = null;
	
	private void loadMeta() {
		if (metas != null)
			return;
		metas = new HashMap();
		Config config = new Config();
		config.fetch();

		List<Model> models = config.getMetadataConfig().getMetadata();
		for (Model m : models) {
			Meta meta = this.config.getMapper().convert(m, Meta.class);
			metas.put(meta.getId(), meta);
		}
		
		// Now all meta's inherit attributes from their parents.
		for (Meta meta : metas.values()) {
			Class modelClass = meta.modelClass();
			inheritAttributes(modelClass, meta, metas);
		}
	}
	
	private static void inheritAttributes(Class c, Meta meta, Map<String, Meta> metas) {
		Class parentClass = c.getSuperclass();
		if (parentClass.equals(Object.class))
			return;
		Meta parent = metas.get(parentClass.getCanonicalName());
		Map <String, Attribute> metaAttributes = meta.attrMap();
		if (parent != null) {
			for (Attribute a : parent.getAttributes()) {
				if(!metaAttributes.containsKey(a.getId())) {
					meta.getAttributes().add(a);
				}
			}
		}
		
		inheritAttributes(parentClass, meta, metas);//meta.getAttributes()
	}
	
	@Override
	public List<Meta> fetch(ModelList<Meta> query) {		
		loadMeta();
		
		
		List<Meta> list = new ArrayList<Meta>(metas.values());
		if (query.getLimit() == 1) {
			return list.subList(0, 1);
		} 
		if (query.getLimit() == 0) {
			return list;
		}
		// TODO: Need a parser, predicate tool.
		// https://www.google.ro/search?q=sql+parser&oq=sql+parser&aqs=chrome..69i57j0l5.1519j0j4&sourceid=chrome&ie=UTF-8#q=sql+java+parser&*
		String splits[] = query.getWhere().split("=");
		List<Meta> res= new ArrayList<Meta>();
		for (Meta meta : list) {
			if (meta.get(splits[0]).equals(query.getParams()[0])) {
				res.add(meta);
			}
			
		}
		return res;
	}
	
}
