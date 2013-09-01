/*
 * Copyright (C) 2013 Zodiac Innovation
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
package com.zodiac.soa;

import java.util.Iterator;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class ArrayIterable<T> implements Iterator {

    private T[] array;
    
    private int cursor;
    
    public ArrayIterable(){
        this.cursor = -1;
    }
    
    public ArrayIterable(T[] array){
        this.array = array;
    }
    
    public boolean hasNext() {
        return cursor + 1 < array.length;
    }

    public Object next() {
        cursor++;
        return array[cursor];
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
