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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import com.tugmodel.client.model.Model;

/**
 * Model related utilities.
 */
public class ModelUtil {

    private static Object search(TreeSet treeset, Object key) {
        Object ceil = treeset.ceiling(key); // least elt >= key
        Object floor = treeset.ceiling(key); // highest elt <= key
        return ceil == floor ? ceil : null;
    }

    private static class MergeComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            if (o1 instanceof Map && o2 instanceof Map) {
                Map o1Map = (Map) o1, o2Map = (Map) o2;
                if (o1Map.get("id") != null && o2Map.get("id") != null) {
                    return ((String) o1Map.get("id")).compareTo((String) o2Map.get("id"));
                }
            } else if (o1 instanceof Model && o2 instanceof Model) {
                return (((Model) o1).getId().compareTo(((Model) o2).getId()));
            }
            return 1; // Greater.
        }
    }

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
                } else if (Collection.class.isAssignableFrom(destValueClass)) {
                    // Set intersection.
                    // HashSet set = new HashSet((Collection)destValue);
                    // set.addAll((Collection)srcValue);
                    TreeSet srcSet = new TreeSet(new MergeComparator());
                    TreeSet destSet = new TreeSet(new MergeComparator());
                    Collection srcCollection = (Collection) srcValue;
                    Collection destCollection = (Collection) destValue;
                    srcSet.addAll(srcCollection);
                    destSet.addAll(destCollection);
                    for (Object o : srcCollection) {
                        if (o instanceof Map && destSet.contains(o)) {
                            mergeDeeply(((Map) o), (Map) search(destSet, o));
                        } else if (o instanceof Model && destSet.contains(o)) {
                            Model destModel = ((Model) search(destSet, o));
                            if (destModel != null) {
                                mergeDeeply(((Model) o).data(), destModel.data());
                            } else {
                                destCollection.add(o);
                            }
                        } else {
                            destCollection.add(o);
                        }
                    }
                    continue;
                }
            }
            dest.put(key, srcValue);
        }
    }

    /**
     * Deep clone tentative.
     */
    public static Object deepClone(Object data) {
        if (data == null) {
            return null;
        } else if (data instanceof Map) {
            Map dataMap = (Map) data;
            Map cloneMap = new HashMap();
            for (Object key : dataMap.keySet()) {
                Object value = dataMap.get(key);
                cloneMap.put(key, deepClone(value));
            }
            return cloneMap;
        } else if (data instanceof Collection) {
            Collection valueCollection = (Collection) data;
            Collection cloneCollection = new ArrayList();
            for (Object el : valueCollection) {
                cloneCollection.add(deepClone(el));
            }
            return cloneCollection;
        } else if (data instanceof Cloneable) {
            try {
                Object clonedValue = data.getClass().getMethod("clone").invoke(data);
                return clonedValue;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return data;
        }
    }
}
