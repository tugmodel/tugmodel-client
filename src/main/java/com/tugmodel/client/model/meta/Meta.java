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
package com.tugmodel.client.model.meta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.CrudTug;
import com.tugmodel.client.tug.TugFactory;


/**
 * Base Meta model that describes simple models where each model has a list of attribute and each attribute has several
 * properties(e.g. datatype).
 */
public class Meta extends Model<Meta> {
    // Avoids instrumentation and the need of duplicating static methods on each model class.
    public static final CrudTug<Meta> s = TugFactory.getCrud(Meta.class);

    /**
     * Returns the attributes. Necessary in order to promote the type information.
     */
    @SuppressWarnings("all")
    public List<Attribute> getAttributes() {
        return (List<Attribute>) get("attributes");
    }

    public Meta setAttributes(List<Attribute> value) {
        return set("attributes", value);
    }

    public Class<?> modelClass() {
        try {
            return Class.forName(asString("class"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    // Shortcut.
    public Map<String, Attribute> attrMap() {
        Map<String, Attribute> map = new HashMap<String, Attribute>();
        for (Attribute attr : getAttributes()) {
            map.put(attr.getId(), attr);
        }
        return map;
    }

}
