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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.tugmodel.client.model.Model;

/**
 * Reflection stuff.
 */
public class ReflectionUtil {
    public final static String KEY_CLASS = "class";
    public final static String KEY_FACTORY = "factory";
    public final static String KEY_FACTORY_METHOD = "method";
    public final static String KEY_FACTORY_ARGS = "args";

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

    /**
     * Spring like(but via json and with no need to specify args type).
     * For the moment only 2 techniques are available:
     * - If "class" property is present then it calls the no arg constructor of that class.
     * - If "factory" is present then it uses: factory.class, factory.method and factory.args array to call the static
     *   method  <factory.class>.<factory.method>(<factory.args[0], factory.args[1], ...>).
     */
    public static <T> T createInstance(Model model, Class<T> type) {
        try {
            if (model.contains(KEY_CLASS)) {
                Class<T> c = (Class<T>) Class.forName(model.asString(KEY_CLASS));
                return c.newInstance();
            } else if (model.contains(KEY_FACTORY)) {
                Model factory = model.asModel(KEY_FACTORY);
                // OF course expecting a mapper that keeps type information.
                List args = model.asList(KEY_FACTORY_ARGS);
                List<Class> argsTypes = new ArrayList();
                for (Object arg : args) {
                    argsTypes.add(arg.getClass());
                }
                Class<T> c = (Class<T>) Class.forName(model.asString(KEY_CLASS));
                Method method = c.getMethod(factory.asString(KEY_FACTORY_METHOD), argsTypes.toArray(new Class[] {}));
                return (T) method.invoke(null, args);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return null;
    }
}
