/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.mapper;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tugmodel.client.model.Model;

/**
 * Mapper implementation using Jackson. TODO: Configure Mapper via a
 * tm-mapper-defaults.json file under resources/config/.
 */
public class JacksonMapper<M extends Model<?>> extends AbstractStringMapper<M> {

	protected ObjectMapper mapper;
	protected ObjectMapper prettyMapper;

	// TODO: Next annotation classes will have to be generated dynamically with javassist based on the metainformation.
	@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.ANY, setterVisibility = Visibility.ANY)
	//@JsonIgnoreProperties(/*ignoreUnknown = true,*/ value = { "tug" })  // ignoreUnknown set globally in getMapper().
	@JsonPropertyOrder({ "id", "version", "tenant" })
	public abstract static class ModelMixin {
		// Or alternative using filters: http://www.baeldung.com/jackson-ignore-properties-on-serialization
		@JsonIgnore
		public abstract Object getTug();

	    @JsonAnyGetter
	    public abstract Map<String, ?> getExtraAttributes();

	    /**
	     * NOTE: any setter does not serialize type info so do not place expected classes fields in common any setter.
	     */
	    @JsonAnySetter
	    public abstract Model set(String name, Object value);
	}
	
	@JsonPropertyOrder({ "id", "version", "tenant", "attributes" })
	public abstract static class MetaMixin extends ModelMixin {
	}
	
	public class MixinsSetupModule extends SimpleModule {
		public MixinsSetupModule() {
	        super("MixinsSetupModule");
	      }

		@Override
		public void setupModule(SetupContext context) {
			context.setMixInAnnotations(Model.class, ModelMixin.class);
			//context.setMixInAnnotations(Meta.class, MetaMixin.class);
		}
	}

	public ObjectMapper getMapper() {
		if (mapper == null) {			
			mapper = initMapper();
		}
		return mapper;
	}
	public ObjectMapper getPrettyMapper() {
		if (prettyMapper == null) {
			prettyMapper = initMapper();
			prettyMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		}
		return prettyMapper;
	}
	private ObjectMapper initMapper() {
			ObjectMapper mapper = new ObjectMapper();
			// TODO: All these should come from config-defaults.json.
	        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Don't include nulls.
	        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
	        mapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
	        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	        //mapper.enableDefaultTyping();   // Will add type information.
	        //mapper.configure(SerializationFeature.INDENT_OUTPUT, true); 
	        
	        mapper.registerModule(new MixinsSetupModule());
		return mapper;
	}
	
	@Override
	public Object serialize(M fromModel) {
		try {
			return getMapper().writeValueAsString(fromModel);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public M deserialize(Object fromValue) {
		// new TypeReference<Map<String, Object>  http://stackoverflow.com/questions/11936620/jackson-deserialising-json-string-typereference-vs-typefactory-constructcoll
		// http://stackoverflow.com/questions/14362247/jackson-adding-extra-fields-to-an-object-in-serialization
		return convert(fromValue, (Class<M>)Model.class);
	}
	
	public void updateModel(Object fromValue, M toModel) {
		//You can use destModel.getAttributes() and then setAttributes or https://www.google.ro/search?q=screw+him&oq=screw+him&aqs=chrome..69i57j0l5.3257j0j4&sourceid=chrome&ie=UTF-8#q=jackson+serialize+on+existing+object&*
		try {
			getMapper().readerForUpdating(toModel).readValue((String)fromValue);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}	
	
	@Override
	public <T> T convert(Object fromValue, Class<T> toValueType) {
		return (T) getMapper().convertValue(fromValue, toValueType);
	}

	/**
	 * Used in debug/development mode to have access to a pretty print of the
	 * actual model or object.
	 */
	public String toPrettyString(Object fromValue) {
		try {
			return getPrettyMapper().writeValueAsString(fromValue);  //((Model)fromValue).setTug(null);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e); //e.printStackTrace();
		}
	}

}
