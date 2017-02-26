/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.model.config;

/**
 * Read configuration files.
 *  
 * new ClasspathConfig().setPath("config/tm-config.json").fetch().get("tugs") 
 */
public class DefaultConfig extends TugConfig<DefaultConfig> {	
	
	public String getPath() {
		return getString("path");
	}
	public DefaultConfig setPath(String path) {
		return set("path", path);
	}

}
