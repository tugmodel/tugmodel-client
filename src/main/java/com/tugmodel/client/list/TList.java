/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.list;

import java.util.ArrayList;

import com.tugmodel.client.model.Model;

/**
 * Laxy list used when querying data. 
 */
public class TList<M extends Model<?>> extends ArrayList<M> {
	private int pageSize;
	private int start;
	
	private String where;
	private String orderBy;
	private Object[] values;

	
}
