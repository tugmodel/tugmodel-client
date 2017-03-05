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
package com.tugmodel.client.model;

import java.io.IOException;

import com.tugmodel.client.model.config.DefaultConfig;

import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * @author cris
 *
 */
public class TestModel {

	public static void main(String args[]) throws ClassNotFoundException, NotFoundException, IOException, CannotCompileException {
		// Loading tugmodel configuration necessary as first step for every kind of config models.		
		// Put this in a static block inside TugFactory { loadConfig };
		
		
//		Model m = new Model();
//		m.save();  // Should throw not implemented exception.
		
		DefaultConfig config = new DefaultConfig();
		
		
		System.out.println(config.toString());
		config = config.fetch();
		System.out.println(config.toString()); 
		
		System.out.println("aaa");

		

				
		System.out.println("aaaaaa");
		

		
	}
}
