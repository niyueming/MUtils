/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.entity;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 类Entities
 * <p>实体s
 *
 * @author nym
 * @version 2013-6-20
 * @time 下午6:16:15
 * @see Entity
 */
public class Entities<T extends Entity> implements Collection<T> {
    ArrayList<T> list;

    public Entities() {
        list = new ArrayList<T>();
    }


    public ArrayList<T> getList() {
        return list;
    }

    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(Object object) {
        return list.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return list.contains(collection);
    }

    @Override
    public boolean isEmpty() {
        return list.size() == 0;
    }

    @Override
    public boolean add(T object) {
        return list.add(object);
    }

    public void add(int index,T object) {
        list.add(index,object);
    }

    @Override
    public boolean addAll(Collection<? extends T> object) {

        return list.addAll(object);
    }

    public T get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(T1[] array) {
        return list.toArray(array);
    }

    public boolean removeEntity(T object) {
        return list.remove(object);
    }

    public void removeEntity(int index) {
        list.remove(index);
    }

    public T remove(int position) {
        return list.remove(position);
    }

//    public boolean remove(T object) {
//        return list.remove(object);
//    }

    @Override
    public boolean remove(Object object) {
        return list.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return list.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return list.retainAll(collection);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).toString()).append(",");
        }
        if (sb.length() > 1)
            sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }


    @Override
    public Iterator<T> iterator() {
        return new SimpleListIterator();
    }

    private class SimpleListIterator implements Iterator<T> {
        int pos = -1;

        int expectedModCount;

        int lastPosition = -1;

        SimpleListIterator() {
            expectedModCount = Entities.this.size();
        }

        public boolean hasNext() {
            return pos + 1 < size();
        }

        public T next() {
            if (expectedModCount == Entities.this.size()) {
                try {
                    T result = get(pos + 1);
                    lastPosition = ++pos;
                    return result;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }
            throw new ConcurrentModificationException();
        }

        public void remove() {
            if (this.lastPosition == -1) {
                throw new IllegalStateException();
            }

            if (expectedModCount != Entities.this.size()) {
                throw new ConcurrentModificationException();
            }

            try {
                Entities.this.remove(lastPosition);
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }

            expectedModCount = Entities.this.size();
            if (pos == lastPosition) {
                pos--;
            }
            lastPosition = -1;
        }
    }
}
