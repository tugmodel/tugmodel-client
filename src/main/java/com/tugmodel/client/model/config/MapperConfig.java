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
import com.tugmodel.client.tug.Tug;
import com.tugmodel.client.tug.TugFactory;
import com.tugmodel.client.util.ReflectionUtil;

/**
 * Keeps a mapper configuration which can be anything.
 * When a tug specifies a mapper it refers to an existing mapper. The tug can also provide custom properties
 * that overwrite the default mapper properties within the model served by MapperConfig.   
 */
public class MapperConfig<M extends MapperConfig> extends Model<M> {
    // This tug returns the list of possible mappers.
    public static final Tug<MapperConfig> s = TugFactory.get(MapperConfig.class);

    /**
     * Creates/returns a mapper from the MapperConfig. 
     * NOTE: If you need a more custom creation then:
     *   1. Create CustomMapperConfig class that extends this class and overwrites "mapper" method.
     *   2. Extend/implement a custom tug for serving CustomMapperConfig instances.  
     */
    public Mapper mapper() {
        MapperConfig mapper = MapperConfig.s.fetchById(asString(Model.KEY_ID));
        mapper.merge(this); // Merge over default mapper.

        return ReflectionUtil.createInstance(mapper, Mapper.class);
    }
}
