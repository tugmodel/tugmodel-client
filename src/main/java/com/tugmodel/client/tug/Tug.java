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
package com.tugmodel.client.tug;

import com.tugmodel.client.model.config.TugConfig;

/**
 * Base interface off all tugs.
 * A tug can serve one or more model types. 
 * A tug is not a model because it contains business logic code. Instead the configuration for a Tug is a model.
 */
@SuppressWarnings("rawtypes")
public interface Tug<TUG extends Tug> {

    public TugConfig getConfig();

    public TUG setConfig(TugConfig config);
	
}
