/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.model.config;

import com.tugmodel.client.mapper.Mapper;
import com.tugmodel.client.model.Model;

/**
 * Base class for all configurations (file based, classpath, db, ...).
 */
public class TugConfig<M extends TugConfig<?>> extends Model<M> {	


	public Mapper<M> getMapper() {
		return (Mapper<M>) get("mapper");
	}
	
	public M setMapper(Mapper<M> mapper) {
		return set("mapper", mapper);
	}
}
