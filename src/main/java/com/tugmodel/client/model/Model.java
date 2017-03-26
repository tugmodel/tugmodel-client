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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.tugmodel.client.model.meta.Attribute;
import com.tugmodel.client.model.meta.Meta;
import com.tugmodel.client.tug.Tug;
import com.tugmodel.client.tug.TugFactory;
import com.tugmodel.client.util.ModelUtil;
import com.tugmodel.client.util.ReflectionUtil;

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
public class Model<M extends Model> {	
    public static final String KEY_ID = "id";
    public static final String KEY_VERSION = "version";
    public static final String KEY_CLASS = "@c";
    protected boolean usesMeta = false;   // If meta should be used when obtaining extra attributes or just reflection.
    protected Map<String, Object> data = Collections.synchronizedMap(new LinkedHashMap<String, Object>());

    public Model() {
        setId(UUID.randomUUID().toString());
    }

    // Copy constructor.
    public Model(Model other) {
        data = other.data();
    }

    // Deep merge.
    public M merge(Model src) {
        ModelUtil.mergeDeeply(data(), src.data());
        return (M)this;
    }

    public Object get(String attr) {
        return data().get(attr);
    }
    
    public <T> T get(String attr, Class<T> valueType) {
        T value = (T) data().get(attr);
        return value;
    }

    public <T> T get(String attr, T defaultValue) {
        T value = (T) get(attr);
        if (value == null) {
            // set(attr, defaultValue);
            return defaultValue;
        }
        return value;
    }

    // Shortcuts.
    public String asString(String attr) {
        return (String) data().get(attr);
    }

    public Byte asByte(String attr) {
        return (Byte) data().get(attr);
    }

    public Integer asInteger(String attr) {
        return (Integer) data().get(attr);
    }

    public Long asLong(String attr) {
        return (Long) data().get(attr);
    }
    
    public BigInteger asBigInteger(String attr) {
        return (BigInteger) data().get(attr);
    }
    
    public Float asFloat(String attr) {
        return (Float) data().get(attr);
    }

    public Double asDouble(String attr) {
        return (Double) data().get(attr);
    }
    
    public BigDecimal asBigDecimal(String attr) {
        return (BigDecimal) data().get(attr);
    }

    public Boolean asBoolean(String attr) {
        return (Boolean) data().get(attr);
    }

    public List asList(String attr) {
        return (List) data().get(attr);
    }
    
    public Map asMap(String attr) {
        return (Map) data().get(attr);
    }

    public Model asModel(String attr) {
        return (Model) data().get(attr);
    }

    public String getId() {
        return asString(KEY_ID);
    }

    public M setId(String value) {
        return set(KEY_ID, value);
    }

    // Use with care, use getters/setters to alter content. It is public just for ModelUtil.
    public Map<String, Object> data() {
        return data;
    }
    // Because null can a be a model attribute value.
    public boolean contains(String attr) {
        return data().containsKey(attr);
    }

    // No need for setMy or separate map for unexpected attrs. Own attrs are identified using reflection or meta.
    public M set(String attr, Object value) {
        data().put(attr, value);
        return (M) this;
    }

    // Returns attributes that are set but do not have dedicated getters nor listed in the metadata.
    public Map<String, ?> getExtraAttributes() {
        Map<String, Object> extra = new HashMap();  // this.hashCode()

        if (!usesMeta) {
            // Else use getters. TODO: For performance obtain getters only once not each time.
            Set<String> myProps = ReflectionUtil.findGetterProperties(this.getClass());
            myProps.remove("extraAttributes");
            for (Map.Entry<String, Object> entry : data().entrySet()) {
                if (!myProps.contains(entry.getKey())) {
                    extra.put(entry.getKey(), entry.getValue());
                }
            }
        } else {
            Meta meta = Meta.s.fetchById(getId());
            if (meta != null) {
                Map<String, Attribute> attrs = meta.attrMap();
                for (Map.Entry<String, Object> entry : data().entrySet()) {
                    if (!attrs.containsKey(entry.getKey())) {
                        extra.put(entry.getKey(), entry.getValue());
                    }
                }
                return extra;
            }
        }
        return extra;
    }

    // No get in front to not hide a potential field with same name.
    protected Tug<M> tug() {
        return TugFactory.get((Class<M>) this.getClass());
    }

    public M fetch() {
        M m = tug().fetch((M) this);
        this.data = m.data(); // Save the content.

        return m;
    }

    public M create() {
        M m = tug().create((M) this);
        return m;
    }

    public M delete() {
        M m = tug().delete((M) this);
        return m;
    }

    public M update() {
        M m = tug().update((M) this);
        return m;
    }

    /**
     * Use for business actions. This should be called from a model subclass for a specific operation. E.g.:
     *      myModel.enable(), myModel.makePublic(), ...
     * The alternative is to call directly a tug. Also sometimes an operation is for multiple models.
     * When calling directly a tug a tug interface that extends Tug is needed. TugModel will create
     * a service implementation at runtime as a dynamic proxy class.
     * A workflow can be made also of generic service invocations via the run method.
     * 
     * @param operation
     * @return
     */
    public Object run(String operation, Object... params) {
        return tug().run(operation, new ArrayList(Arrays.asList(params)));
    }

    /**
     * One to many.
     */
    public void add(Model child) {
        List list = tug().add((M) this, Collections.singletonList(child));
    }

    // Use only for pretty print.
    @Override
    public String toString() {
        //return tug().getConfig().getMapper().toPrettyString(this);   // this.hashCode()
        return null;
    }
}