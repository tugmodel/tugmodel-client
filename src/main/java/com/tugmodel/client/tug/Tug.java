/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.tug;

import java.util.ArrayList;
import java.util.List;

import com.tugmodel.client.list.TList;
import com.tugmodel.client.mapper.Mapper;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.TugConfig;

/**
 * Base interface off all tugs. 
 * A tug is not a model because it contains business logic code. Instead the configuration for a Tug is a model.
 * 
 */
public interface Tug<M extends Model<?>> {

	public TugConfig<?> getConfig();
	
	public Tug<M> setConfig(TugConfig<?> config);
	
	//////////////////////////////////////////////////////////////////////
	// CRUD direct model operations.
	//////////////////////////////////////////////////////////////////////
	
	public M fetch(M model);
	
    public M create(M model);
    
    public M update(M model);

    public M delete(M model);

    public Object run(String operation, ArrayList<Object> params);

	
	//////////////////////////////////////////////////////////////////////
	// Servicer direct operations.
	//////////////////////////////////////////////////////////////////////
		
	// Additional parameters provided for sending authorization token.
    public M find(String id, Model<?>... params);
    
    public List<M> findAll();

    // A lazy list similar to the one in JavaLite.
    public TList<M> where(String query, Object... params);



	
}
