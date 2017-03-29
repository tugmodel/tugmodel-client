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

import java.util.List;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.tugs.MapperConfig;
import com.tugmodel.client.model.config.tugs.TowConfig;
import com.tugmodel.client.model.config.tugs.TugConfig;
import com.tugmodel.client.model.meta.Meta;
import com.tugmodel.client.model.meta.datatype.DataType;
import com.tugmodel.client.tug.CrudTug;
import com.tugmodel.client.tug.TugFactory;

/**
 * Basic configuration of the entire tugmodel system.
 * Use <code>Config.s.fetchById("defaults")</code> or
 *     <code>new Config().setId("defaults").set("tenant", tenantId).fetch()</code> 
 * to fetch configuration.
 *  
 */
@SuppressWarnings("all")
// public class Config<M extends Config> extends Model<M> {
public class Config extends Model<Config> {
    public static final CrudTug<Config> s = TugFactory.getCrud(Config.class);

    public static final String KEY_MODELS = "models";
    public static final String KEY_DATATYPES = "dataTypes";
    public static final String KEY_TUGS = "tugs";
    public static final String KEY_MAPPERS = "mappers";
    public static final String KEY_TOWS = "tows";

    // USE the Meta.s to obtain by id or condition.
    public List<Meta> getModels() {
        return (List<Meta>) get(KEY_MODELS);
    }

    public Config setModels(List<Meta> val) {
        return set(KEY_MODELS, val);
    }

    public List<DataType> getDataTypes() {
        return (List<DataType>) get(KEY_DATATYPES);
    }

    public Config setDataTypes(List<DataType> val) {
        return set(KEY_DATATYPES, val);
    }

    public List<TugConfig> getTugs() {
        return (List<TugConfig>) get(KEY_TUGS);
    }

    public Config setTugs(List<TugConfig> val) {
        return set(KEY_TUGS, val);
    }

    public List<MapperConfig> getMappers() {
        return (List<MapperConfig>) get(KEY_MAPPERS);
    }

    public Config setMappers(List<MapperConfig> val) {
        return set(KEY_MAPPERS, val);
    }

    public List<TowConfig> getTows() {
        return (List<TowConfig>) get(KEY_TOWS);
    }

    public Config setTows(List<TowConfig> val) {
        return set(KEY_TOWS, val);
    }

}
