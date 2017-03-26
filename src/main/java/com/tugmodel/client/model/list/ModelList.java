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
package com.tugmodel.client.model.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.tug.Tug;
import com.tugmodel.client.tug.TugFactory;

/**
 * Lazy list used when querying data.
 * A list is driven by a tug also indirectly via the tug assigned to the model. 
 */
public class ModelList<M extends Model> extends ArrayList<M> {
	private static final long serialVersionUID = 1L;
	protected String where = "";
    protected Object[] params = new Object[]{};
    protected Integer offset = 1;
    protected Integer limit = 0;
    protected String orderBy = "";
    
    protected String childId;
	protected String modelId;
	
	public ModelList() {		
	}
    public ModelList(Collection<M> c) {
        super(c);
        fetched = true;
    }
    
    public ModelList<M> modelId(Class modelClass) {
        modelId = modelClass.getCanonicalName();
        return this;
    }
    public ModelList<M> modelId(String modelId) {
        this.modelId = modelId;
        return this;
    }
    
    public boolean isFetchAll() {
    	return limit == 0 && offset == 1 && "".equals(where) && "".equals(orderBy);
    }
    
    public boolean isFetchById() {
    	return "id=?".equals(where) && limit == 0 && offset == 1 && "".equals(orderBy) && params.length == 1;
    }

    public boolean isFetchFirst() {
    	return "".equals(where) && limit == 1 && offset == 1 && "".equals(orderBy);
    }

    public boolean isPaginated() {
        return limit != 0;
    }
    
    public String getModelId() {
        return modelId;
    }

    public ModelList<M> where(String val, Object... params) {
        where = val;
        this.params = params;
        return this;
    }

    public ModelList<M> setWhere(String val) {
        where = val;
        return this;
    }

    public String getWhere() {
        return where;
    }

    public ModelList<M> offset(int val) {
        offset = val;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public ModelList<M> limit(int val) {
        limit = val;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public ModelList<M> orderBy(String val) {
        orderBy = val;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public ModelList<M> params(Object[] val) {
        params = val;
        return this;
    }

    public Object[] getParams() {
        return params;
    }

    public ModelList<M> child(String val) {
        childId = val;
        return this;
    }
    
    public ModelList<M> child(Class val) {
        childId = val.getCanonicalName();
        return this;
    }

    public String getChild() {
        return childId;
    }

    
    protected boolean fetched = false;
    protected void fetchIfNeeded() {
        if (fetched)
            return;
		try {
			Tug<M> tug = TugFactory.get((Class<M>)Class.forName(modelId));
			List<M> list = tug.fetch(this);
	        this.addAll(list);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}        

        fetched = true;
    }

    ////////// Overwrite ArrayList methods to allow lazy behavior. //////////
	@Override     // In case a developer may want to take a look in debug.
	public String toString() {
		fetchIfNeeded();
		return super.toString();
	}

	@Override
	public int size() {
		fetchIfNeeded();
		return super.size();
	}

	@Override
	public boolean isEmpty() {
		fetchIfNeeded();
		return super.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		fetchIfNeeded();
		return super.contains(o);
	}

	@Override
	public Iterator<M> iterator() {
		fetchIfNeeded();
		return super.iterator();
	}

	@Override
	public Object[] toArray() {
		fetchIfNeeded();
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		fetchIfNeeded();
		return super.toArray(a);
	}

	@Override
	public M get(int index) {
		fetchIfNeeded();
		return super.get(index);
	}

	@Override
	public void sort(Comparator<? super M> c) {
		fetchIfNeeded();
		super.sort(c);
	}

	@Override
	public int indexOf(Object o) {
		fetchIfNeeded();
		return super.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		fetchIfNeeded();
		return super.lastIndexOf(o);
	}

	@Override
	public ListIterator<M> listIterator() {
		fetchIfNeeded();
		return super.listIterator();
	}

	@Override
	public ListIterator<M> listIterator(int index) {
		fetchIfNeeded();
		return super.listIterator(index);
	}

	@Override
	public List<M> subList(int fromIndex, int toIndex) {
		fetchIfNeeded();
		return super.subList(fromIndex, toIndex);
	}

}
