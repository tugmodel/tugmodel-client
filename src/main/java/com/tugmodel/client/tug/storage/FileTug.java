/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.tug.storage;

import java.io.File;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.AbstractTug;

/**
 * 
 *
 */
public class FileTug<M extends Model<?>> extends AbstractTug<M> {
	protected File folder;
	

//	   public List<TM> find(String like) {
//	        List<TM> list = new ArrayList<>();
//	        try {
//	            if (like == "*") {
//	                for (String name : folder.list()) {
//	                    File file = new File(folder, name);
//	                    if (file.isFile())
//	                        list.add(JsonFileServicer.MAPPER.readValue(new File(folder, name), amClass));
//	                }
//	            } else {
//	                File file = new File(folder, like + ".json");
//	                if (file.exists())
//	                    list.add(JsonFileServicer.MAPPER.readValue(file, amClass));
//	            }
//	        } catch (IOException e) {
//	            throw new RuntimeException(e);
//	        }
//
//	        return list;
//	    }
	
}
