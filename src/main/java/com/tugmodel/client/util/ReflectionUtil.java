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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Detect getters.
 */
public class ReflectionUtil {

	public static Set<String> findGetterProperties(Class c) {
		Set<String> list = new TreeSet();
		findGetterProperties(c, list);
		return list;
	}
	
	private static void findGetterProperties(Class c, Set<String> props) {
		Method[] methods = c.getDeclaredMethods();
		for (Method method : methods) {
			if (isGetter(method)) {
				String prop = method.getName().substring(3);
				prop = prop.substring(0, 1).toLowerCase() + prop.substring(1);
				props.add(prop);
			}
		}
		if (!c.getSuperclass().equals(Object.class)) {
			findGetterProperties(c.getSuperclass(), props);
		}		
	}
	
	public static boolean isGetter(Method method) {
		if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
			if (method.getName().matches("^get[A-Z].*") && !method.getReturnType().equals(void.class))
				return true;
//			if (method.getName().matches("^is[A-Z].*") && method.getReturnType().equals(boolean.class))
//				return true;
		}
		return false;
	}

	public static boolean isSetter(Method method) {
		return Modifier.isPublic(method.getModifiers()) && method.getReturnType().equals(void.class)
				&& method.getParameterTypes().length == 1 && method.getName().matches("^set[A-Z].*");
	}
}
