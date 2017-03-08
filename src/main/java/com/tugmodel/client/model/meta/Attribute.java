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

import com.tugmodel.client.mapper.Mapper;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.Config;
import com.tugmodel.client.tug.TugFactory;

/**
 * 
 *
 */
public class Attribute extends Model<Attribute> {

	
	
	public DataType getDataType() {
		return get("dataType", DataType.class);
	}

	// When saving datatype a merging is done using the default datatypes.
	public Attribute setDataType(DataType dt) {
		Config config = new Config().fetch();
	    // Use the Model copy constructor or use the mapper. Let's try the mapper.
		Mapper mapper = TugFactory.getTug(DataType.class).getConfig().getMapper();
		Model defaultDTModel = config.getMetadataConfig().dataTypeAsMap().get(dt.getId());
		if (defaultDTModel != null) {
			DataType defaultDT = (DataType)mapper.convert(defaultDTModel, DataType.class);
			mapper.updateModel(defaultDT.toString(), dt);
		}
		return this;
	}
}
