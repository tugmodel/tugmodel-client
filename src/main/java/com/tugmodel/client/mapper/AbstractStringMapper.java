/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.mapper;

import com.tugmodel.client.model.Model;

/**
 * 
 *
 */
public abstract class AbstractStringMapper<M extends Model<?>> implements Mapper<M> {

	private Object notImplementedException() {
		throw new RuntimeException("You need to implement the inherited Mapper method.");
	}

	public Object serialize(M fromModel) {
		return notImplementedException();
	}
	
	public M deserialize(Object fromValue) {
		return (M)notImplementedException();
	}

	public void updateModel(Object fromValue, M toModel) {
		notImplementedException();
	}
	
	public <T> T convert(Object value, Class<T> dest) {
		return (T)notImplementedException();
	}

	@Override
	public String toPrettyString(Object fromValue) {
		return (String) notImplementedException();
	}


}
