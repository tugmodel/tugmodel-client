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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.tugmodel.client.util.ModelUtil;


/**
 * Test Model Util.
 */
public class TestModelUtil {


    @Test
    public void testMerge() {
        Map dest = new HashMap();
        dest.put("a", 1);
        dest.put("b", 1);

        Map src = new HashMap();
        src.put("a", 2);
        src.put("c", 1);

        ModelUtil.mergeDeeply(src, dest);

        assertTrue((Integer) src.get("b") == null);
        assertTrue((Integer) dest.get("b") == 1);
        assertTrue((Integer) dest.get("a") == 2);
    }

    @Test
    public void testMergeSubMap() {
        Map dest = new HashMap();
        dest.put("a", 1);
        dest.put("b", 1);

        Map src = new HashMap();
        src.put("a", 2);
        src.put("c", 1);

        Map destSubMap = new HashMap();
        destSubMap.put("sa", 1);
        destSubMap.put("sb", 1);
        dest.put("subMap", destSubMap);

        Map srcSubMap = new HashMap();
        srcSubMap.put("sa", 2);
        srcSubMap.put("sc", 1);
        src.put("subMap", srcSubMap);

        ModelUtil.mergeDeeply(src, dest);
        assertTrue((Integer) ((Map) dest.get("subMap")).get("sa") == 2);
        assertTrue((Integer) ((Map) dest.get("subMap")).get("sc") == 1);
    }

    @Test
    public void testMergeSubModel() {
        Map dest = new HashMap();
        dest.put("a", 1);
        dest.put("b", 1);

        Map src = new HashMap();
        src.put("a", 2);
        src.put("c", 1);

        Model destSubModel = new Model();
        destSubModel.set("sa", 1);
        destSubModel.set("sb", 1);
        dest.put("subModel", destSubModel);

        Model srcSubModel = new Model();
        srcSubModel.set("sa", 2);
        srcSubModel.set("sc", 1);
        src.put("subModel", srcSubModel);

        ModelUtil.mergeDeeply(src, dest);
        assertTrue((Integer) ((Model) dest.get("subModel")).get("sa") == 2);
        assertTrue((Integer) ((Model) dest.get("subModel")).get("sc") == 1);
    }

    @Test
    public void testClone() {
        List l1 = new ArrayList();
        Model m = new Model().set("a", l1).clone();
        List l2 = m.clone().asList("a");

        assertTrue(l1 != l2);
    }

    @Test
    public void testMergeSubArray() {

        ArrayList srcList = new ArrayList();
        ArrayList destList = new ArrayList();

        Map dest = new HashMap();
        dest.put("a", 1);
        dest.put("list", destList);

        Map src = new HashMap();
        src.put("a", 2);
        src.put("list", srcList);

        srcList.add(new Model().setId("random").set("x", "3"));
        srcList.add(new Model().setId("same").set("x", "1"));

        destList.add(new Model().setId("same").set("x", "2")); // After merging should be 1.

        ModelUtil.mergeDeeply(src, dest);
        List mergedList = (List) dest.get("list");
        assertTrue(((Model) mergedList.get(0)).get("x").equals("1"));

    }

}
