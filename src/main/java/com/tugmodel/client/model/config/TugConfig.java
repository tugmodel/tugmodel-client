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
package com.tugmodel.client.model.config;

import com.tugmodel.client.mapper.Mapper;
import com.tugmodel.client.model.Model;

/**
 *  
 */
@SuppressWarnings("all")
public class TugConfig extends Model<TugConfig> {

    public static final String KEY_MAPPER = "mapper";
    public static final String KEY_CACHED_MAPPER = "cachedMapper";

    /**
     * Returns cached mapper. If you do not need the cache then do getMapper().mapper(). 
     */
    public Mapper<Model> mapper() {
        Mapper mapper = get(KEY_CACHED_MAPPER, Mapper.class);
        if (mapper == null) {
            mapper = getMapper().mapper();
            set(KEY_CACHED_MAPPER, mapper);
        }
        return mapper;
    }

    public MapperConfig getMapper() {
        return get(KEY_MAPPER, MapperConfig.class);
    }

    public TugConfig setMapper(MapperConfig val) {
        return set(KEY_MAPPER, val);
    }
    
	public Class<? extends Model<?>> getModelClass() {
		String id = asString("modelId");
//		Meta<Meta<?>>.s.get
		try {
			return (Class<? extends Model<?>>)Class.forName(id);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
