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
package com.zodiac.db;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class DAODriver {
    
    private static Map<String, DAO> drivers;
    
    private static String defaultConnector;
        
    static {
        drivers = new HashMap();
    }
    
    protected DAODriver() {
    }
    
    public static void setDefaultConnector(String defaultConnector){
        DAODriver.defaultConnector = defaultConnector;
    }
    
    public static <T extends DAO>T getDAODriver(Class<T> daoClass)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return getDAODriver(daoClass, null) ;
    }
    
    public static <T extends DAO>T getDAODriver(Class<T> daoClass, String connector) 
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        DAOPackage daoPackage = 
                (DAOPackage)daoClass.getAnnotation(DAOPackage.class);
        if(daoPackage == null){
            throw new IllegalArgumentException("The dao driver does not have an implementation package defined. "
                    + "Use annotation DAOPackage.");
        }
        
        String className = 
                daoPackage.implementation() + "." + daoClass.getSimpleName() + 
                (connector != null ? connector : DAODriver.defaultConnector);
        
        DAO dao = drivers.get(className);
        if(dao == null){
            dao = (DAO)Class.forName(className).newInstance();
            drivers.put(className, dao);
        }
        return (T)dao;
    }
    
}
