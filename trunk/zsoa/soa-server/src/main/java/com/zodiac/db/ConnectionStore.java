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

package com.zodiac.db;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Store multiple connections using an identifier.
 * 
 * The programmer must implement this class to have anywhere all the 
 * connections object instantiated. In this way there is no need to create
 * a new connection on every method or pass by reference the connection.
 * 
 * To implement this class the programmer register an object using
 * <tt>register(String identifier, Connection connection, ConnectionParameter 
 * connectionParameter)</tt> and access <tt>get(String identifier)</tt>.
 * 
 * Keeping alive the connection parameter objecto, let the programmer to create
 * new parallel connections for different operations.
 * 
 * A third-way to store a single connection is using {@link SingletonConnection}
 * and it is recommended to optimize the runtime performance.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public final class ConnectionStore {
    
    /**
     * Struct to keep alive the connections parameters for futures
     * connection creations.
     */
    private static HashMap<String,ConnectionParameter> params = new HashMap();
    
    /**
     * Struct to keep alive the connections object.
     */
    private static HashMap<String,Connection> connections = new HashMap();
    
    /**
     * Add to the register an object store by an identifier.
     * 
     * @param identifier References to an object registered by unique text.
     * @param connection Object to store.
     * @exception IllegalArgumentException if identifier is null or empty
     */
    public static void register(String identifier, Connection connection, 
            ConnectionParameter connectionParameter) {
        if(!(identifier instanceof Object)){
            throw new IllegalArgumentException("identifier cannot be null or empty");
        }
        
        params.put(identifier, connectionParameter);
        connections.put(identifier, connection);
    }
    
    /**
     * Retrieve the connection store identified by the unique text.
     * 
     * @param identifier References to an object registered by unique text.
     * @return Connection object or null if does not exist
     */
    public static Connection getConnection(String identifier){
        return connections.get(identifier);
    }
    
    /**
     * Retrieve the connection params to create a new connection.
     * 
     * @param identifier References to an object registered by unique text.
     * @return Connection params
     */
    public static ConnectionParameter getParameter(String identifier){
        return params.get(identifier);
    }

}
