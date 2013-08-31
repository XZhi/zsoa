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

/**
 * The programmer can implement this class to store a single connection,
 * can be access anywhere.
 * 
 * This class was build with singleton pattern and the programmer
 * can use it as an alternative to {@link ConnectionStore} optimizing
 * the runtime performance. But of course, the programmer can use
 * <tt>ConnectionStore</tt> and <tt>SingletonConnection</tt> together
 * by just storing the main connection in <tt>SingletonConnection</tt> and
 * the other ones in <tt>ConnectionStore</tt>.
 * 
 * As a first step the programmer has to set a connection, thereafter
 * it can be access anywhere by static method <tt>getInstance()</tt>.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public final class SingletonConnection {
    
    /**
     * The only one <tt>SingletonConnection</tt> instance.
     */
    private static SingletonConnection INSTANCE = null;
    
    /**
     * The connection stored.
     */
    private Connection connection;
    
    /**
     * Constructor used to prevent the creation of an instance
     * of this class out.
     */
    private SingletonConnection(){
        
    }
    
    /**
     * Method that creates the single instance if does not
     * exist. It has a synchronized container to prevent the access from
     * multiple threads.
     * 
     * @return The single instance of this class.
     */
    public static SingletonConnection getInstance(){
        if(INSTANCE == null){
            synchronized (SingletonConnection.class){
                if(INSTANCE == null){
                    INSTANCE = new SingletonConnection();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Retrieves the connection stored.
     * 
     * @return The connection stored.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Set the single connection.
     * 
     * @param connection The connection stored.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
}
