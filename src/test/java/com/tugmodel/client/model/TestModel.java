/**
 * 
 */
package com.tugmodel.client.model;

import com.tugmodel.client.model.config.DefaultConfig;
import com.tugmodel.client.model.config.TugConfig;

/**
 * @author cris
 *
 */
public class TestModel {

	
	public static void main(String args[]) {
		// Loading config.		
		TugConfig<DefaultConfig> config = new DefaultConfig().setPath("/config/tm-config.json");
		System.out.println(config.toString());
		config = config.fetch();
		System.out.println(config.toString()); 
		
		System.out.println("aaa");
	
	}
}
