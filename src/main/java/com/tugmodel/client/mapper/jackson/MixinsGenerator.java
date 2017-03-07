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
package com.tugmodel.client.mapper.jackson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.meta.Meta;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * Setup mixins based on the meta information.
 */
public class MixinsGenerator extends SimpleModule {
	
	/**
	 * This mixin does not use Meta information. Is only used to bootstrap the loading/config of the framework.
	 * Since it does not have access to meta information a model.toString will make fields appear twice.
	 */
	@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.ANY, setterVisibility = Visibility.ANY)
	@JsonPropertyOrder({ "id", "version", "tenant" })	
	public static abstract class BootstrapMixin {
		 @JsonAnyGetter
		 public abstract Map getExtraAttributes();
		 @JsonAnySetter
		 public abstract Model set(String name, Object value);
	}
	
	
	public MixinsGenerator(String id) {
		super(id);
	}

	protected void generateModelMixin(Meta meta, SetupContext context) {
		String pkgName = "com.tugmodel.client.mapper.";
		String mixinClassName = pkgName + getModuleName() + meta.getId();
		try {
			Class.forName(mixinClassName);
			System.out.println("Mixin " + mixinClassName + " already exists.");
			return;
		} catch( ClassNotFoundException e ) {
			//my class isn't there!			
		}
		
		// https://jboss-javassist.github.io/javassist/tutorial/tutorial3.html
		// http://blog.javaforge.net/post/31913732423/howto-create-java-pojo-at-runtime-with-javassist
		// Tools like https://github.com/OpenHFT/Java-Runtime-Compiler use
		// ToolProvider that is only bundled in JDK (javac is missing from JRE).
		// http://stackoverflow.com/questions/26117147/how-can-i-compile-source-code-with-javassist
		// The alternative is using javassist but we will not have access to the
		// source code.
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass(mixinClassName);
		// CtClass mixinClass = pool.get(pkgName + "ModelMixin"); // Means that
		// the class already exists.

		// Without the call to "makePackage()", package information is lost
		// pool.makePackage(pool.getClassLoader(), pkgName);
		ClassFile ccFile = cc.getClassFile();
		ConstPool constPool = ccFile.getConstPool();

		// @JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility =
		// Visibility.ANY, setterVisibility = Visibility.ANY)
		// @JsonPropertyOrder({ "id", "version", "tenant" })
		AnnotationsAttribute autoDetectAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		Annotation autoDetectAnnot = new Annotation("com.fasterxml.jackson.annotation.JsonAutoDetect", constPool);
		EnumMemberValue e1 = new EnumMemberValue(constPool);
		e1.setType(Visibility.NONE.getClass().getName());
		e1.setValue("NONE");
		autoDetectAnnot.addMemberValue("fieldVisibility", e1);

		EnumMemberValue e2 = new EnumMemberValue(constPool);
		e2.setType(Visibility.NONE.getClass().getName());
		e2.setValue("ANY");
		autoDetectAnnot.addMemberValue("getterVisibility", e2);

		EnumMemberValue e3 = new EnumMemberValue(constPool);
		e3.setType(Visibility.NONE.getClass().getName());
		e3.setValue("ANY");
		autoDetectAnnot.addMemberValue("setterVisibility", e3);

		autoDetectAttr.addAnnotation(autoDetectAnnot);
		ccFile.addAttribute(autoDetectAttr);

		// @JsonPropertyOrder({ "id", "version", "tenant" })
		AnnotationsAttribute orderAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		Annotation orderAnnot = new Annotation("com.fasterxml.jackson.annotation.JsonPropertyOrder", constPool);
		ArrayList<StringMemberValue> members = new ArrayList<StringMemberValue>();
		StringMemberValue member = new StringMemberValue(constPool);
		member.setValue("id");
		members.add(member);
		StringMemberValue member2 = new StringMemberValue(constPool);
		member2.setValue("version");
		members.add(member2);
		ArrayMemberValue arrayValue = new ArrayMemberValue(constPool);
		arrayValue.setValue(members.toArray(new MemberValue[0]));
		orderAnnot.addMemberValue("value", arrayValue); // The property name of JsonPropertyOrder#value().
		orderAttr.addAnnotation(orderAnnot);
		ccFile.addAttribute(orderAttr);

		// Add type info on specific class or set at mapper wide level for all classes.
		// @JsonTypeInfo(defaultImpl = MyModel.class, include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.MINIMAL_CLASS)
		
		try {
			// cc.addMethod(m);
			String extraMethodDef = "public abstract java.util.Map getExtraAttributes();";
			CtMethod extraMethod = CtNewMethod.make(extraMethodDef, cc); // CtMethod.make(extraMethodDef, cc);
			cc.addMethod(extraMethod);
			AnnotationsAttribute anyGetterAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
			Annotation anyGetterAnnotation = new Annotation("com.fasterxml.jackson.annotation.JsonAnyGetter", constPool);
			anyGetterAttribute.addAnnotation(anyGetterAnnotation);
			extraMethod.getMethodInfo().addAttribute(anyGetterAttribute);

			String anySetterMethodDef = "public abstract com.tugmodel.client.model.Model set(String name, Object value);";
			CtMethod anySetterMethod = CtNewMethod.make(anySetterMethodDef, cc);
			cc.addMethod(anySetterMethod);
			AnnotationsAttribute anySetterAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
			Annotation anySetterAnnotation = new Annotation("com.fasterxml.jackson.annotation.JsonAnySetter", constPool);
			anySetterAttribute.addAnnotation(anySetterAnnotation);
			anySetterMethod.getMethodInfo().addAttribute(anySetterAttribute);

			// cc.writeFile(); // Write file to disc. It works.
			Class mixinClass = cc.toClass();
			context.setMixInAnnotations(meta.modelClass(), mixinClass);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}

	@Override
	public void setupModule(SetupContext context) {
		
		List<Meta> metas = Meta.s.fetchAll();
		for (Meta meta : metas) {
			generateModelMixin(meta, context);
		}
	
	}
}
