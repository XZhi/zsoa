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

import java.lang.reflect.Array;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class ArrayCreator<T> {
    
    //<editor-fold defaultstate="collapsed" desc="Attributes">
    private T[] array;
    
    private Class<T> componentType;
    
    private int size;
    
    private int cursor;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    private ArrayCreator(){
        this.cursor = 0;
    }
    
    public ArrayCreator(Class<T> componentType, int size){
        this();
        this.componentType = componentType;
        this.size = size;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    private boolean isFull(){
        if(cursor == size){
            throw new IndexOutOfBoundsException(
                    "Maximum size(" + size + ") of objects has been reached.");
        }
        return false;
    }
    
    private boolean isValidIndex(int index){
        if(index  >= size){
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds (" + size + ").");
        }
        return true;
    }
    
    private void moveNext(){
        cursor = cursor + 1;
    }
    
    private void moveTo(int index){
        cursor = index;
    }
    
    private int cursor(){
        return cursor;
    }
    
    private synchronized void checkIfArrayInitialized(){
        if(array == null){
            array = (T[])Array.newInstance(componentType, size);
        }
    }
    
    public boolean add(T object){
        if(!isFull()){
            checkIfArrayInitialized();
            moveNext();
            array[cursor()] = object;
            return true;
        }
        return false;
    }
    
    public boolean insert(int index, T object){
        if(isValidIndex(index)){
            checkIfArrayInitialized();
            moveTo(index);
            array[cursor()] = object;
            return true;
        }
        return false;
    }
    
    public T[] toArray(){
        return array;
    }
    //</editor-fold>
    
}
