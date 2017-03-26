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
package com.tugmodel.client.model.meta.datatype;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.CrudTug;
import com.tugmodel.client.tug.TugFactory;

/**
 * Same DataType can be shared by multiple attributes. 
 * The datatype reference by an attribute can provide overwrites for that datatype so a new instance is created. 
 */
@SuppressWarnings("all")
public class DataType<DT extends DataType<?>> extends Model<DT> {
    // Assuming datatypes can be shared by multiple attributes.
    public static final CrudTug<DataType> s = TugFactory.getCrud(DataType.class);

    // If the datatype is a list of models.
    public boolean isOneToMany() {
        return getId().equals("oneToMany");
    }    
    public boolean isOneToOne() {
        return getId().equals("oneToOne");
    }
    // Needs association table.
    public boolean isManyToMany() {
        return getId().equals("manyToMany");
    }

}
