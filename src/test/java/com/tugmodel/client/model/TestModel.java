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
import java.util.List;

import com.tugmodel.client.model.config.Config;
import com.tugmodel.client.model.meta.Meta;

import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * @author cris
 *
 */
public class TestModel {
	public static void main(String args[]) throws ClassNotFoundException, NotFoundException, IOException, CannotCompileException {
		
//		Model m = new Model();
//		m.save();  // Should throw not implemented exception.

//		Meta meta = new Meta();

				
		Config config = new Config();
		
		
		System.out.println(config.toString());
		config = config.fetch();
		System.out.println(config.toString()); 
		
		System.out.println("aaa");

		List<Meta> metas = Meta.s.fetchAll();
		Meta modelMeta = Meta.s.fetchById("Model");
		
		
		

				
		System.out.println("aaaaaa");
		

		
	}
}
