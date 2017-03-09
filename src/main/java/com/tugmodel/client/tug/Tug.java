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
 * Base interface off all tugs. 
 * A tug is not a model because it contains business logic code. Instead the configuration for a Tug is a model.
 * 
 */
public interface Tug<M extends Model> {

	public TugConfig<M> getConfig();
	
	public Tug<M> setConfig(TugConfig<M> config);
	
	//////////////////////////////////////////////////////////////////////
	// Model direct operations(CRUD & Business).
	//////////////////////////////////////////////////////////////////////
	
	public M fetch(M model);
	
    public M create(M model);
    
    public M update(M model);

    public M delete(M model);
    
    public <C extends Model> List<C> add(M model, List<C> childs);
    
    // This method is for custom business API (SPI).
    public Object run(String operation, List<Object> params);

	
	//////////////////////////////////////////////////////////////////////
	// Tug direct operations.
	//////////////////////////////////////////////////////////////////////
		
	// Additional parameters provided for sending authorization token.
    public M fetchById(String id);

    public M fetchFirst();

    // ModelList is a lazy active list. Use with care since there may be many elements returned.
    public ModelList<M> fetchAll();

    public ModelList<M> where(String query, Object... params);
    
    public <C extends Model> ModelList<C> where(Class<C> child, String query, Object... params);

    // This sends the lazylist params(no elements) and receive a raw list with the values.
    public List<M> fetch(ModelList<M> query);

    // Provide a complete query. E.g. "select * from ...". This should return the results in front.
    public <C extends Model> List<C> fetchByRawQuery(Class<C> c, String query, Object... params);

    



	
}
