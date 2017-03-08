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

import java.util.List;

import com.tugmodel.client.list.ModelList;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.TugConfig;

/**
 * You can use this as base class when implementing tugs.
 */
public class BaseTug<M extends Model<?>> implements Tug<M> {
	// Should it be part of the config defaults?.
//	public static final Mapper PRETTY_PRINT_MAPPER = new JacksonMapper<Model>(); 
	protected TugConfig<M> config = new TugConfig<M>();
	
	public BaseTug() {
	}
	
	public TugConfig<M> getConfig() {
		return config;
	}
	
	public Tug<M> setConfig(TugConfig<M> config) {
		this.config = config;
		return this;
	}
	
	private Object notImplementedException() {
		throw new RuntimeException("You need to implement the inherited Tug method.");
	}
	
	public M fetch(M model) {
		return (M)notImplementedException();
	}
		
    public M create(M model) {
		return (M)notImplementedException();
    }

	public M update(M model) {
		return (M)notImplementedException();
    }
    	
    public M delete(M model) {
		return (M)notImplementedException();
    }
	
	public M run(String operation, List<Object> params) {
		// TODO Auto-generated method stub
		return null;
	}	
		
	// Additional parameters provided for sending authorization token.	
    public M fetchById(String id) {
    	return (M)notImplementedException();
    }    
	
    // A lazy list similar to the one in JavaLite.
    public ModelList<M> where(String query, Object... params) {
    	return (ModelList<M>)notImplementedException();
    }

	@Override
	public M fetchFirst() {
		return (M)notImplementedException();
	}

	@Override
	public List<M> fetchAll() {
		return (ModelList<M>)notImplementedException();
	}

	@Override
	public ModelList<M> fetchByQuery(String query, Object... params) {
		return (ModelList<M>)notImplementedException();
	}

}
