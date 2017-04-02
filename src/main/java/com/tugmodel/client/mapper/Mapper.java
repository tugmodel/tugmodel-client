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
import com.tugmodel.client.model.config.tugs.TugConfig;

/**
 * This is used by a tug to serialize/deserialize a model in a format that is required by the tug.   
 * For example when serializing a model before sending over the wire.
 * 
 * TODO: Must remove some methods.
 */
@SuppressWarnings("rawtypes")
public interface Mapper<M extends Model> {
    public void setTugConfig(TugConfig tc);

    public TugConfig getTugConfig();

    /**
     * Converts into RAW data that is suitable for communication. TODO: Leave only convert method and remove the M type
     * parameter.
     */
    public Object serialize(M src);

    public M deserialize(Object src);

    public Object serialize(Object src);

    public <T> T deserialize(Object src, Class<T> destClass);

    public void updateModel(Object src, M dest);

    /**
     * E.g. Jackson, 2 step conversion: Writing a POJO as JSON, and second, binding that JSON into another kind of POJO.
     * When using also provide type parameter, ((Mapper<Model>) mapper).convert.
     */
    public <T> T convert(Object src, Class<T> destClass);
    
    /**
     * Used in debug/development mode to have access to a pretty print of the actual model or object.
     */
    public String toPrettyString(Object src);
	
	
}
