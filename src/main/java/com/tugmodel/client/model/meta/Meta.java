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
package com.tugmodel.client.model.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.Tug;
import com.tugmodel.client.tug.TugFactory;

/**
 * Metadata model.
 */
public class Meta extends Model<Meta> {
	public static final Tug<Meta> s = TugFactory.get(Meta.class); 
	
	public String getDescription() {
		return asString("description");
	}
	
	public Meta setDescription(String description) {
		return set("description", description);
	}
	
	public Class<?> modelClass() {
		try {
			return Class.forName(asString("class"));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
//	// Shortcut method.
//	public Attribute attr(String id) {
//		for (Attribute attr : getAttributes()) {  // TODO: cache some map?.
//			if (id.equals(attr.getId()))
//				return attr;
//		}
//		return null;
//	}
	
	public Map<String, Attribute> attrMap() {
		Map<String, Attribute> map = new HashMap();
		for (Attribute attr : getAttributes()) {
			map.put(attr.getId(), attr);
		}
		return map;
	}
	
    public List<Attribute> getAttributes() {
         return get("attributes", new ArrayList<Attribute>());
    }

    public Meta setAttributes(List<Attribute> value) {
        return set("attributes", value);
    }
}
