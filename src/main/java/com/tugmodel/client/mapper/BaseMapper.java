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
 * Base mapper used when creating custom mappers.
 */
@SuppressWarnings("all")
public class BaseMapper<M extends Model> implements Mapper<M> {

	private Object notImplementedException() {
		throw new RuntimeException("You need to implement the inherited Mapper method.");
	}

    public Object serialize(M src) {
		return notImplementedException();
	}
	
    public M deserialize(Object src) {
		return (M)notImplementedException();
	}

    @Override
    public Object serialize(Object src) {
        return notImplementedException();
    }

    @Override
    public <T> T deserialize(Object src, Class<T> destClass) {
        return (T) notImplementedException();
    }

    public void updateModel(Object src, M destClass) {
		notImplementedException();
	}
	
    public <T> T convert(Object src, Class<T> dest) {
		return (T)notImplementedException();
	}

    public String toPrettyString(Object src) {
		return (String) notImplementedException();
	}

    private TugConfig tc;

    @Override
    public void setTugConfig(TugConfig tc) {
        this.tc = tc;
    }

    @Override
    public TugConfig getTugConfig() {
        return tc;
    }

}
