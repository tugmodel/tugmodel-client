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

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.Tug;
import com.tugmodel.client.tug.TugFactory;

/**
 * 
 *
 */
public class DataType<DT extends DataType> extends Model<DT> {
	// Tug over default datatypes.
	public static final Tug<DataType> s = TugFactory.get(DataType.class);
	    

}
