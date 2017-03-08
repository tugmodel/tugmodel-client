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
package com.tugmodel.client.list;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.Tug;

/**
 * Lazy list used when querying data.
 */
public class ModelList<M extends Model<?>> extends ArrayList<M> {
	protected String where = null;
	protected Object[] params = null;
	protected Integer offset = null;
	protected Integer limit = null;
	protected String orderBy = null;

	protected Tug<M> tug;
	
	public ModelList() {
		
	}
	public ModelList(Tug<M> tug) {
		this.tug = tug;
	}

	public ModelList<M> where(String val, Object... values) {
		where = val;
		params = values;
		return this;
	}

	public ModelList<M> offset(int val) {
		offset = val;
		return this;
	}

	public ModelList<M> limit(int val) {
		limit = val;
		return this;
	}

	public ModelList<M> orderBy(int val) {
		offset = val;
		return this;
	}

	protected boolean populated = false;

	protected void populate() {
		if (populated)
			return;
		System.out.println("populate");
		String query = where; // Construct query from internal data.
		List list = tug.fetchByQuery(query, params); 
		this.addAll(list);

		populated = true;
	}

	@Override
	public int size() {
		populate();
		return super.size();
	}

	@Override
	public boolean isEmpty() {
		populate();
		return super.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		populate();
		return super.contains(o);
	}

	@Override
	public Iterator<M> iterator() {
		populate();
		return super.iterator();
	}

	@Override
	public Object[] toArray() {
		populate();
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		populate();
		return super.toArray(a);
	}

	@Override
	public M get(int index) {
		populate();
		return super.get(index);
	}

	@Override
	public void sort(Comparator<? super M> c) {
		populate();
		super.sort(c);
	}

	@Override
	public int indexOf(Object o) {
		populate();
		return super.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		populate();
		return super.lastIndexOf(o);
	}

	@Override
	public ListIterator<M> listIterator() {
		populate();
		return super.listIterator();
	}

	@Override
	public ListIterator<M> listIterator(int index) {
		populate();
		return super.listIterator(index);
	}

	@Override
	public List<M> subList(int fromIndex, int toIndex) {
		populate();
		return super.subList(fromIndex, toIndex);
	}

}
