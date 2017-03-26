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

import com.tugmodel.client.model.Model;

/**
 * Basic configuration of the entire tugmodel system. 
 */
public class Config<M extends Config> extends Model<M> {
    public static final String KEY_METADATA = "metadata";
    public static final String KEY_TUGS = "tugs";

    // Be careful for the method name to match the property in the JSON.
    public Model getMetadata() {
        return asModel(KEY_METADATA);
    }

    public Config setMetadata(Model mc) {
        return set(KEY_METADATA, mc);
    }

    public Model getTugs() {
        return asModel(KEY_TUGS);
    }

    public Config setTugs(Model mc) {
        return set(KEY_TUGS, mc);
    }
}
