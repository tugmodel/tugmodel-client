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
package com.tugmodel.client.mapper.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugmodel.client.mapper.AbstractStringMapper;
import com.tugmodel.client.model.Model;

/**
 * Mapper implementation using Jackson. TODO: Configure Mapper via a
 * tm-mapper-defaults.json file under resources/config/.
 */
public abstract class JacksonMapper<M extends Model> extends AbstractStringMapper<M> {
	protected ObjectMapper mapper;
		
	public abstract ObjectMapper initMapper();
	
	public ObjectMapper getMapper() {
		if (mapper == null) {			
			mapper = initMapper();
		}
		return mapper;
	}
	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Object serialize(M fromModel) {
		try {
			return getMapper().writeValueAsString(fromModel);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public M deserialize(Object fromValue) {
		// new TypeReference<Map<String, Object>  http://stackoverflow.com/questions/11936620/jackson-deserialising-json-string-typereference-vs-typefactory-constructcoll
		// http://stackoverflow.com/questions/14362247/jackson-adding-extra-fields-to-an-object-in-serialization
		try {
			return (M)getMapper().readValue(fromValue.toString(), Model.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void updateModel(Object fromValue, M toModel) {
		//You can use destModel.getAttributes() and then setAttributes or https://www.google.ro/search?q=screw+him&oq=screw+him&aqs=chrome..69i57j0l5.3257j0j4&sourceid=chrome&ie=UTF-8#q=jackson+serialize+on+existing+object&*
		try {
			getMapper().readerForUpdating(toModel).readValue((String)fromValue);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}	
	
	@Override
	public <T> T convert(Object fromValue, Class<T> toValueType) {
		return (T) getMapper().convertValue(fromValue, toValueType);
	}

	/**
	 * Used in debug/development mode to have access to a pretty print of the
	 * actual model or object.
	 */
	public String toPrettyString(Object fromValue) {
		try {
			return getMapper().writeValueAsString(fromValue);  //fromValue.hashCode()
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e); //e.printStackTrace();
		}
	}

}
