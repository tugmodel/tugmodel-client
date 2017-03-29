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

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.CrudTug;
import com.tugmodel.client.tug.TugFactory;

/**
 * A towing configuration. Can be used instead of TugFactory.
 */
@SuppressWarnings("all")
public class TowConfig extends Model<TowConfig> {
    // TuggingConfig.s.where("modelId=?", <modelId>) can be used instead of TugFactory.
    public static final CrudTug<TowConfig> s = TugFactory.getCrud(TowConfig.class);

    public static final String KEY_MODEL = "model";
    public static final String KEY_MODEL_ID = "modelId";
    public static final String KEY_TUG = "tug";
    public static final String KEY_TENANT = "tenant";
    public static final String KEY_CONDITION = "condition";

    public String getModelId() {
        return asString(KEY_MODEL_ID);
    }

    public TowConfig setModelId(String val) {
        return set(KEY_MODEL_ID, val);
    }

    public Model getModel() {
        return asModel(KEY_MODEL);
    }

    public TowConfig setModel(Model val) {
        return set(KEY_MODEL, val);
    }

    public Model getTug() {
        return asModel(KEY_TUG);
    }

    public TowConfig setTug(Model val) {
        return set(KEY_TUG, val);
    }

}
