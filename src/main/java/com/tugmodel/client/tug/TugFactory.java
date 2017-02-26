/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.tug;

import com.tugmodel.client.mapper.JacksonMapper;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.config.DefaultConfigTug;

/**
 * 
 *
 */
public class TugFactory {

	
	public static <M extends Model<?>> Tug<M> getTug(Class<M> modelClass) {
		//return new FileTug<M>();
		
		// TODO: Allow the setting of config from 
		DefaultConfigTug configTug = new DefaultConfigTug();
		configTug.getConfig().setMapper(new JacksonMapper<>());
		
		return configTug;
	}
	
}
