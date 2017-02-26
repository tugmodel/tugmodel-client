/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.mapper;

import com.tugmodel.client.model.Model;

/**
 * This is used by a tug to serialize/deserialize a model in a format that is required by the tug.   
 * For example when serializing a model before sending over the wire.
 * 
 * TODO: Must remove some methods.
 */
public interface Mapper<M extends Model<?>> {
	
//	public Mapper<M> setModelClass(Class<M> modelClass);
//	public Class<M> getModelClass();
	
	/**
	 * Converts into RAW data that is suitable for communication.  
	 * TODO: Leave only convert method and remove the M type parameter.
	 */
	public Object serialize(M fromModel); 
	
	public M deserialize(Object fromValue);

	public void updateModel(Object fromValue, M toModel);
	
	public <T> T convert(Object fromValue, Class<T> toValueType);
		
	/**
	 * Used in debug/development mode to have access to a pretty print of the actual model or object. 
	 */
	public String toPrettyString(Object fromValue);
	
}
