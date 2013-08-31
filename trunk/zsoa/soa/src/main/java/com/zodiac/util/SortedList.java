/*
 * Copyright (C) 2012 Zodiac Innovation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.zodiac.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.OperationNotSupportedException;

/**
 *
 * @author brian
 */
public class SortedList<E extends Comparable> extends ArrayList<E>{

    public SortedList() {
    }

    public SortedList(Collection<? extends E> c) {
        super(c);
    }
    
    public SortedList(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size() + 1);
        
        Comparable element = (Comparable)e;
        
        int startSearchRange = 0;
        int endSearchRange = size() == 0 ? 0 : size() - 1;
                
        boolean inserted = false;
        Comparable elementList;
        int compare, pivot, range;
        do{
            range = endSearchRange - startSearchRange;
            if(range == 0) {
                super.add(e); inserted = true;
            } else if (range == 1) {
                elementList = (Comparable)get(startSearchRange);
                compare = element.compareTo(elementList);
                if(compare <= 0) {
                    super.add(startSearchRange, e); inserted = true;
                } else {
                    elementList = (Comparable)get(endSearchRange);
                    compare = element.compareTo(elementList);
                    if(compare <= 0){
                        super.add(endSearchRange, e); inserted = true;
                    } else {
                        super.add(e); inserted = true;
                    }
                }
            } else if (range > 1) {
                pivot = range / 2 + startSearchRange;
                elementList = (Comparable)get(pivot);
                compare = element.compareTo(elementList);
                if(compare == 0){
                    super.add(pivot, e);
                } else if (compare > 0) {
                    startSearchRange = pivot;
                } else if (compare < 0) {
                    endSearchRange = pivot;
                }
            } else {
                throw new IllegalStateException("Initial list is not sorted.");
            }
            
        }while(!inserted);
        
        return true;
    }
    
    @Override
    public void add(int index, E element) {
        try {
            throw new OperationNotSupportedException("Method not supported on a sorted list.");
        } catch (OperationNotSupportedException ex) {
            Logger.getLogger(SortedList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        try {
            throw new OperationNotSupportedException("Method not supported on a sorted list.");
        } catch (OperationNotSupportedException ex) {
            Logger.getLogger(SortedList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
