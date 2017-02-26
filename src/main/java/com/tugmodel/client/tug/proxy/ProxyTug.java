/**
 * Copyright (c) 2017- Cristian Donoiu
 * Licensed under the License specified in file LICENSE, included with the source code and binary code bundles.
 */
package com.tugmodel.client.tug.proxy;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.AbstractTug;

/**
 * Wrapper tug used for stuff like error handling and delegation to proxied tug.
 */
public class ProxyTug<M extends Model<?>> extends AbstractTug<M> {

	String proxiedTug;
	
	public String getProxiedTug() {
		return proxiedTug;
	}
}
