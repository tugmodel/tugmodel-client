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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.tugs.TugConfig;

public class TestMapper {

    @Test
    public void testConvert() {
        Mapper<Model> mapper = new Mapper<Model>() {

            public Object serialize(Model src) {
                // TODO Auto-generated method stub
                return null;
            }

            public Model deserialize(Object src) {
                // TODO Auto-generated method stub
                return null;
            }

            public void updateModel(Object src, Model dest) {
                // TODO Auto-generated method stub

            }

            public <T> T convert(Object src, Class<T> destClass) {
                // TODO Auto-generated method stub
                return (T) String.valueOf(src);
            }

            public String toPrettyString(Object src) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setTugConfig(TugConfig tc) {
                // TODO Auto-generated method stub

            }

            @Override
            public TugConfig getTugConfig() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Object serialize(Object src) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <T> T deserialize(Object src, Class<T> destClass) {
                // TODO Auto-generated method stub
                return null;
            }

        };

        String res = mapper.convert(3, String.class);
        assertTrue("3".equals(res));
    }
}