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
package com.zodiac.soa;

/**
 * Structure required for dynamic invocation.
 * 
 * There are two groups of parameters-agurments. The first one corresponds to the contructor
 * and the second one corresponds to the method. In both groups the size of parameters
 * and arguments should be the same.
 * 
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class Request extends AbstractDTO {
   
    /**
     * Class name to be called.
     */
    protected String clazz;
    
    /**
     * The aguments' type of the contructor.
     */
    protected Class[] parametersConstructor;
    
    /**
     * The constructor's aguments.
     */
    protected Object[] argumentsConstructor;
    
    /**
     * A method in the class (clazz) to be called.
     */
    protected String method;
    
    /**
     * The aguments' type of the method.
     */
    protected Class[] parametersMethod;
    
    /**
     * The method's arguments.
     */
    protected Object[] argumentsMethod;
    
    /**
     * Force the server to return the trace exception in string format
     */
    private boolean textModeException;
    
    public Request() {
    }
    
    public Request(String clazz, Object[] argumentsConstructor, String method, Object[] argumentsMethod){
        this(clazz, method, argumentsMethod);
        this.argumentsConstructor = argumentsConstructor;
        if(argumentsConstructor != null){
            Class[] parametersContructor = new Class[argumentsConstructor.length];
            int i = 0;
            for(Object o : argumentsConstructor){
                parametersContructor[i] = o.getClass();
            }
            this.parametersConstructor = parametersContructor;
        } else {
            this.parametersConstructor = null;
        }
    }
    
    public Request(String clazz, String method, Object[] argumentsMethod){
        this.clazz = clazz;
        this.method = method;
        this.argumentsMethod = argumentsMethod;
        if(argumentsMethod != null){
            Class[] parametersMethod = new Class[argumentsMethod.length];
            int i = 0;
            for(Object o : argumentsMethod){
                parametersMethod[i] = o.getClass();
            }
            this.parametersMethod = parametersMethod;
        } else {
            this.parametersMethod = null;
        }
    }
    
    public Request(String clazz, String method, Class[] parametersMethod, Object[] argumentsMethod) {
        this.clazz = clazz;
        this.method = method;
        this.parametersMethod = parametersMethod;
        this.argumentsMethod = argumentsMethod;
    }
    
    public Request(String clazz, Class[] parametersConstructor, Object[] argumentsConstructor, String method, Class[] parameters, Object[] arguments) {
        this(clazz, method, parameters, arguments);
        this.parametersConstructor = parametersConstructor;
        this.argumentsConstructor = argumentsConstructor;
    }
        
    public void setTextModeException(boolean textModeException) {
        this.textModeException = textModeException;
    }

    public boolean isTextModeException() {
        return textModeException;
    }
    
    public Object[] getArgumentsMethod() {
        return argumentsMethod;
    }

    public String getClazz() {
        return clazz;
    }

    public String getMethod() {
        return method;
    }

    public Class[] getParametersMethod() {
        return parametersMethod;
    }

    public Object[] getArgumentsConstructor() {
        return argumentsConstructor;
    }

    public Class[] getParametersConstructor() {
        return parametersConstructor;
    }
        
}
