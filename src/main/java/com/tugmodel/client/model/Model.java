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
package com.tugmodel.client.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tugmodel.client.model.meta.Attribute;
import com.tugmodel.client.model.meta.Meta;
import com.tugmodel.client.tug.Tug;
import com.tugmodel.client.tug.TugFactory;

/**
 * Base class for all models. It is used by tug interface for various operations.
 * TODO: Consider the usage of a model interface.
 * 
 * INSPIRATION:
 * 	Similar to ideas found in JavaLite(http://javalite.io/getting_started), backbone.js and other active records frameworks (https://en.wikipedia.org/wiki/Active_record_pattern).
 * 	Will bound type parameter where the upper bound is the same class. Inspiration from here: http://stackoverflow.com/questions/19312641/passing-generic-subtype-class-information-to-superclass-in-java :
 * 	<pre>public abstract class Abstract<T extends Abstract<T>> { </pre>
 * 	Will use Factory pattern (http://docs.oracle.com/javase/tutorial/extra/generics/literals.html) for factory methods.
 * 
 * 	For keeping the actual data of the model:
 * 		1. Keep it in Maps: <pre>public class Model<MODEL extends Model<MODEL>> implements Externalizable {</pre>
 * 		2. Keep it in an abstract storage bean(TugBean): <pre> public class Model<M extends Model<M, DATA>, DATA extends TugBean<?>> implements Externalizable { </pre>
 *    		2 works and is more flexible since it allows substituting the underlying storage but it is mind blowing. Will try 1 first which is simpler and 2 later.
 *    
 * RULE: Tugs act as services, models act as smart/active POJO's which delegate operations to tugs. 
 * 		<b>The only thing that you should place inside models methods are calls to other models or tugs invocations.</b>
 *       Models need to be dumb(like POJO's) because <b>the models may be shared with clients</b>. Always place business code only inside tugs.
 * 
 * EXTENSIBILITY:
 *  Allows extensible documents/models like the ones in NoSQL. Thus any data can be stored under a model as long as the tug knows how to deliver/process it. 
 * 	For this use the generic "set" and "get" methods. 
 *  In order to differentiate between extra data and expected the metadata is used (no setMy, getMy) methods are required.    
 * 
 * CHAINING CALLS:
 * 	Calls are chainable: new Model().set("f", 1).save();
 * 
 * MAPPER:
 *        Jackson Mixin used to add annotations forJackson Mapper.
 *        The model must not decide how is to be serialized but the Mapper&Tug.
 *        
 * SENDING CALLER INFO (e.g. session id, client IP, authorization token):
 *     1. No dedicated method is provided you just set whatever you consider in an extra model attribute as a simple/complex value.
 * 		Model model = new Model().set("authToken", "x13123").set("attr1", "x").save();
 *     When querying using the Tug:     		
 *     		XModel.s.find(id, auth);
 *     2. Better: the Tug should obtain and send/process caller info.
 * 
 * 
 */
public class Model<M extends Model<?>> {	
	public static String ID = "id";   // Should be externalized in properties file.

	protected boolean isNew = false;  // This model has not been fetched.
	protected Map<String, Object> data = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); // TODO: Consider data TugBean. 
	
	public Model() {
		setId(UUID.randomUUID().toString());
	}	
	
    public Object get(String attr) {
        return data.get(attr);
    }

    public <T> T get(String attr, Class<T> valueType) {
        T value = (T)data.get(attr);
        return value;
    }
    public <T> T get(String attr, T defaultValue) {
        T value = (T)get(attr);
        if (value == null) {
        	return defaultValue;
    	}
        return value;
    }
    
    // Shortcut. TODO: Consider removing only the "get" part in order to be ignored by the mapper or add to ignore list. Still the Jackson mapper was did not include them.
    public String getString(String attr) {
    	return (String) data.get(attr);
    }
    
    public Byte getByte(String attr) {
    	return (Byte) data.get(attr);
    }
    
    public Integer getInteger(String attr) {
    	return (Integer) data.get(attr);
    }
    
    public Long getLong(String attr) {
    	return (Long) data.get(attr);
    }

    public Double getDouble(String attr) {
    	return (Double) data.get(attr);
    }
    
    public Boolean getBoolean(String attr) {
    	return (Boolean) data.get(attr);
    }    
    
    public Map getMap(String attr) {
    	return (Map) data.get(attr);
    }
    
    public List getList(String attr) {
    	return (List) data.get(attr);
    }

    public String getId() {
        return getString(ID);
    }
    
    public M setId(String value) {
        return set(ID, value);
    }
    
    protected Map<String, Object> data() {
    	return data;    	
    }

	// No need for setMy or separate map for unexpected attrs. Own attrs are derived from meta.
    public M set(String attr, Object value) {
        data.put(attr, value);
        return (M)this;
    }
    
    // Returns attributes that are set but were not listed in the metadata.
    public Map<String, ?> getExtraAttributes() {
    	Meta meta = Meta.s.fetchById(getId());
    	if (meta != null) {
	    	Map<String, Attribute> attrs = meta.attrMap();    	
	    	Map<String, Object> extra = new HashMap();
	    	for (Map.Entry<String, Object> entry : data.entrySet()) {
	    		if (!attrs.containsKey(entry.getKey())) {
	    			extra.put(entry.getKey(), entry.getValue());
	    		}
	    	}
    	}
    	return data;
    }
    
    
    // No get in front to not hide a potential field with same name.
    protected Tug<M> tug() {
    	return TugFactory.getTug((Class<M>)this.getClass());
    }
    
    public M fetch() {
    	
    	M m = tug().fetch((M)this);
    	isNew = false;
    	return m;
    }
    
    // Used for both create and save.
    public M save() {    	
    	if (isNew) {
    		return tug().create((M)this);
    	}
   		return tug().update((M)this);
    }
    
    public M delete() {    	
    	return tug().delete((M)this);
    }
    
    /**
     * Use for business actions(non CRUD). This should be called from a model subclass for a specific operation. E.g.:
     * 		myModel.enable(), myModel.makePublic(), ...
     * For business operations that affect multiple models use the 
     * @param operation
     * @return
     */
    public M run(String operation, List<Object> params) {    	
    	return tug().run(operation, params);
    }
    
    
    // Use only for pretty print.
    @Override
    public String toString() {
    	return tug().getConfig().getMapper().toPrettyString(this);   //tug().getConfig().getMapper().convert(this, String.class)
    }
    
}
