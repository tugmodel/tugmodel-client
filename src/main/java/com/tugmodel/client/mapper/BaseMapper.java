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
package com.tugmodel.client.mapper;

import com.tugmodel.client.model.Model;

/**
 * 
 *
 */
public abstract class AbstractStringMapper<M extends Model> implements Mapper<M> {

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

	public String toPrettyString(Object fromValue) {
		return (String) notImplementedException();
	}


}
