/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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
 */
public class Model<M extends Model<?>> implements Externalizable {	
	public static String ID = "id";   // Should be externalized in properties file.
	public static String VERSION = "version";
	public static String TENANT = "tenant";

	protected boolean isNew = false;  // This model has not been fetched.
	protected Map<String, Object> data = Collections.synchronizedMap(new LinkedHashMap<String, Object>()); // TODO: Consider data TugBean. 
	
	public Model() {
		setId(UUID.randomUUID().toString());
		setVersion("1.0");		
	}	
	
    public Object get(String attr) {
        return data.get(attr);
    }

    // Shortcut.
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
    
    public String getId() {
        return getString(ID);
    }
    
    public M setId(String value) {
        return set(ID, value);
    }
    
    public String getVersion() {
        return getString(VERSION);
    }
    
    public M setVersion(String value) {
        return set(VERSION, value);
    }
    
    public String getTenant() {
        return getString(TENANT);
    }
    
    public M setTenant(String value) {
        return set(TENANT, value);
    }
	
    protected Map<String, Object> attributes() {
    	return data;    	
    }

	// No need for setMy or separate map for unexpected attrs. Own attrs are derived from meta.
    public M set(String attr, Object value) {
        data.put(attr, value);
        return (M)this;
    }
    
    // Returns attributes that are set but were not listed in the metadata.
    public Map<String, ?> getExtraAttributes() {
    	// TODO: Iterate on Meta model.
    	return data;
    }
    
    
//    public Servicer<AM> getServicer() {
//        return ServicerFactory.get((Class<AM>) this.getClass());
//    }
    
    // No get in front to not hide a potential field with same name.
    protected Tug<M> tug() {
    	return TugFactory.getTug((Class<M>)this.getClass());
    }
    public M fetch() {
    	
    	M m = tug().fetch((M)this);
    	isNew = false;
    	return m;
    }
    
//    // Used for both create and save.
//    public void save() {    	
//    	getTug().fetch((M)this);
//    }
    
    // Use only for pretty print.
    @Override
    public String toString() {
    	return tug().getConfig().getMapper().toPrettyString(this);
    }
    
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}	
	
	
}
