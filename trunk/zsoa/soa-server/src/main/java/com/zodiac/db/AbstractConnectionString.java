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

/**
 * This class provides a skeletal implementation of the <tt>ConnectionString</tt> 
 * interface, to minimize the effort required to implement this interface.
 * 
 * To implement a new connection string for database, the programmer needs to
 * extends this class and override <tt>getConnectionString()</tt> method.
 * 
 * @author Brian Estrada <brianseg014@gmail.com>
 * @see ConnectionString
 */

public abstract class AbstractConnectionString implements ConnectionString {
    /**
     * The parameters used to build the connection string for a database connection.
     */
    private ConnectionParameter connectionParameter;
    
    /**
     * Construct a connection string with parameters.
     * 
     * @param connectionParameter Object to be set on <tt>connectionParameter</tt>.
     */
    public AbstractConnectionString(ConnectionParameter connectionParameter){
        setConnectionParameter(connectionParameter);
    }
    
    /**
     * Set the connection string parameters.
     * 
     * @param connectionParameter Object to be set on <tt>connectionParameter</tt>.
     */
    private void setConnectionParameter(ConnectionParameter connectionParameter) {
        this.connectionParameter = connectionParameter;
    }

    /**
     * Retrieves the parameters used in the connection string.
     * 
     * @return Parameters that have been set on <tt>connectionParameter</tt>.
     */
    protected final ConnectionParameter getConnectionParameter() {
        return connectionParameter;
    }
    
}
