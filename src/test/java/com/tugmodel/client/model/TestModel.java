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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.tugmodel.client.model.config.Config;
import com.tugmodel.client.model.meta.Meta;

import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * @author cris
 *
 */
public class TestModel {
	public static void main(String args[]) throws ClassNotFoundException, NotFoundException, IOException, CannotCompileException {
		
//		Model m = new Model();
//		m.save();  // Should throw not implemented exception.

//		Meta meta = new Meta();

				
//		Config config = new Config();
//		
//		
//		System.out.println(config.toString());
//		config = config.fetch();
//		System.out.println(config.toString()); 
//		
//		System.out.println("aaa");
//
//		List<Meta> metas = Meta.s.fetchAll();
//		Meta modelMeta = Meta.s.fetchById("Model");
//				
//		System.out.println("aaaaaa");
	}
    
    
    @Test
    public void modelSetter() {
        assertTrue(new Model().set("a", 1).asInteger("a") == 1);
    }

    
    private static class XModel extends Model<XModel> {
        public int getX() {
            return asInteger("x");
        }
        public XModel setX(int val) {
            return set("x", 1);
        }
    };

    @Test
    public void nullValueSupport() {
        assertTrue(new Model().set("a", null).contains("a") == true);
        assertTrue(new Model().set("a", null).contains("b") == false);
        assertTrue(new Model().set("a", null).get("a") == null);        
    }
    
    @Test
    public void modelExtraAttributesUsingReflection() {
        XModel m = new XModel();
        m.set("y", 0);
        m.setX(1);
        
        assertTrue(m.getX() == 1);
        assertTrue(m.getExtraAttributes().size() == 1);
        assertTrue(((Integer)m.getExtraAttributes().get("y")) == 0);
        
    }

}
