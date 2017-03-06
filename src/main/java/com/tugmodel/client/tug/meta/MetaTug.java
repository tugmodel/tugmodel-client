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

import java.util.Collections;
import java.util.List;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.DefaultConfig;
import com.tugmodel.client.model.meta.Attribute;
import com.tugmodel.client.model.meta.Meta;
import com.tugmodel.client.tug.BaseTug;

/**
 * Provides metadata.
 */
public class MetaTug extends BaseTug<Meta> {

	@Override
    public List<Meta> fetchAll() {
    	
		
		DefaultConfig config = new DefaultConfig();
		config.fetch();
		
		// TODO: read from config. Be careful to not have an infinite loop.
		Meta m = new Meta();
		m.setId("Model");
		m.set("class", Model.class.getCanonicalName());
		Attribute a = new Attribute();
		a.setId("id");
		m.getAttributes().add(a);
		
		return Collections.singletonList(m);
    }
	
}
