/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.tug;

import java.util.ArrayList;
import java.util.List;

import com.tugmodel.client.list.TList;
import com.tugmodel.client.mapper.Mapper;
import com.tugmodel.client.mapper.JacksonMapper;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.TugConfig;

/**
 * 
 *
 */
public abstract class AbstractTug<M extends Model<?>> implements Tug<M> {
	// Should it be part of the config defaults?.
	public static final Mapper PRETTY_PRINT_MAPPER = new JacksonMapper<Model>(); 
	protected TugConfig<?> config = new TugConfig();
	
	public AbstractTug() {
	}
	
	public TugConfig<?> getConfig() {
		return config;
	}
	
	public Tug<M> setConfig(TugConfig<?> config) {
		this.config = config;
		return this;
	}
	
	private Object notImplementedException() {
		throw new RuntimeException("You need to implement the inherited Tug method.");
	}
	
	@Override
	public M fetch(M model) {
		return (M)notImplementedException();
	}
	
	@Override
    public M create(M model) {
		return (M)notImplementedException();
    }

	@Override
    public M update(M model) {
		return (M)notImplementedException();
    }
    
	@Override
    public M delete(M model) {
		return (M)notImplementedException();
    }

	@Override
    public Object run(String operation, ArrayList<Object> params) {
    	return notImplementedException();
    }

	
		
	// Additional parameters provided for sending authorization token.
	@Override
    public M find(String id, Model... params) {
    	return (M)notImplementedException();
    }
    
	@Override
    public List<M> findAll() {
    	return (List<M>)notImplementedException();
    }

    // A lazy list similar to the one in JavaLite.
	@Override
    public TList<M> where(String query, Object... params) {
    	return (TList<M>)notImplementedException();
    }

}
