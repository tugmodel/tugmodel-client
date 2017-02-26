/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.tug.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tugmodel.client.mapper.JacksonMapper;
import com.tugmodel.client.mapper.Mapper;
import com.tugmodel.client.model.config.DefaultConfig;
import com.tugmodel.client.tug.AbstractTug;

/**
 * new Config("config/config-defaults.json").fetch().get("tugs")
 *
 */
public class DefaultConfigTug<M extends DefaultConfig> extends AbstractTug<M> {
	public static final String PATH_SEPARATOR = ".";
	// You need a JSON mapper for reading files. And configuration files are always json.
	public final Mapper<M> CONFIG_MAPPER = new JacksonMapper<M>(); 
	
	public M fetch(M model) {
		String path = model.getPath();		
		String defaultsPath = path.replaceAll(".json", "-defaults.json");
		
		// Alternative is to use model.getAttributes map.
		CONFIG_MAPPER.updateModel(readFile(defaultsPath), model);
		CONFIG_MAPPER.updateModel(readFile(path), model);
		
		return model;
	}

	protected String readFile(String path) {
		// Take a look at https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/main/java/org/springframework/core/io/support/PathMatchingResourcePatternResolver.java
		StringBuilder sb = new StringBuilder();
		try {
			InputStream is = this.getClass().getResourceAsStream(path);
			if (is != null) {
				BufferedReader buf = new BufferedReader(new InputStreamReader(is));			
				String line = buf.readLine();
				while(line != null) {
					sb.append(line + "\n");
					line = buf.readLine();			
				}
				buf.close();
			} else {
				sb.append("{}");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
}
