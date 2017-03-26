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
package com.tugmodel.client.util;

import java.util.Map;

import com.tugmodel.client.model.Model;

/**
 * Model related utilities.
 */
public class ModelUtil {

    public static void mergeDeeply(Map src, Map dest) {
        for (Object key : src.keySet()) {
            Object srcValue = src.get(key);
            if (srcValue == null) {
                dest.put(key, null);
                continue;
            }
            Class srcValueClass = srcValue.getClass();

            if (!dest.containsKey(key)) {
                dest.put(key, srcValue);
                continue;
            }
            Object destValue = dest.get(key);
            if (destValue == null) {
                dest.put(key, srcValue);
                continue;
            }

            Class destValueClass = destValue.getClass();
            if (srcValueClass == destValueClass) {
                if (Model.class.isAssignableFrom(destValueClass)) {
                    mergeDeeply(((Model) srcValue).data(), ((Model) destValue).data());
                    continue;
                } else if (Map.class.isAssignableFrom(destValueClass)) {
                    mergeDeeply(((Map) srcValue), ((Map) destValue));
                    continue;
                }
            }
            dest.put(key, srcValue);
        }
    }
}
