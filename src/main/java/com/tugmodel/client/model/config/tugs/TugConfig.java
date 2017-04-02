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
package com.tugmodel.client.model.config.tugs;

import com.tugmodel.client.mapper.Mapper;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.meta.Meta;
import com.tugmodel.client.tug.CrudTug;
import com.tugmodel.client.tug.TugFactory;

/**
 * A tug configuration. Keeps the default tug configurations. Also tugs merge a default tug config with a corresponding
 * tow.
 */
@SuppressWarnings("all")
public class TugConfig extends Model<TugConfig> {
    public static final CrudTug<TugConfig> s = TugFactory.getCrud(TugConfig.class);

    public static final String KEY_MAPPER = "mapper";
    public static final String KEY_CACHED_MAPPER = "cachedMapper";
    public static final String KEY_MODEL_ID = "modelId";
    public static final String KEY_CLASS = "class";

    private Mapper cachedMapper;
    /**
     * TODO: Move the caching within a tug since it is business logic.
     * Returns cached mapper. If you do not need the cache then do getMapper().mapper().
     */
    public Mapper<Model> mapper() {
        if (cachedMapper == null) {
            cachedMapper = getMapper().mapper();
            cachedMapper.setTugConfig(this);
        }
        return cachedMapper;
    }
    public TugConfig mapper(Mapper val) {
        cachedMapper = val;
        cachedMapper.setTugConfig(this);
        return this;
    }

    public Meta getModel() {
        return get("model", Meta.class);
    }

    public TugConfig setModel(Meta meta) {
        return set("model", meta);
    }

    public MapperConfig getMapper() {
        return get(KEY_MAPPER, MapperConfig.class);
    }

    public TugConfig setMapper(MapperConfig val) {
        return set(KEY_MAPPER, val);
    }

    /**
     * Created at runtime by parsing all mappers and finding a candidate.
     */
    public Class<? extends Model<?>> modelClass() {
        String id = asString("modelId");
        // Meta<Meta<?>>.s.get
        try {
            return (Class<? extends Model<?>>) Class.forName(id);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}

