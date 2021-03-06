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
package com.tugmodel.client.model.sample;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.CrudTug;
import com.tugmodel.client.tug.TugFactory;

/**
 * Classical Oracle example.
 */
public class Employee extends Model<Employee> {
    public static final CrudTug<Employee> s = TugFactory.getCrud(Employee.class);
	
	public String getName() {
		return asString("name");
	}
	
	public Employee setName(String name) {
		return set("name", name);
	}
	
}
