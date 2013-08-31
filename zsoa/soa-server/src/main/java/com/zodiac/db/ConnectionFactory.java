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

import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * This class create a connection to a database.
 * 
 * {@link java.sql.DriverManager#getConnection(java.lang.String, java.lang.String, java.lang.String) }
 * is used to create a new connection to a database. The programmer has to consider what driver
 * is needed by the connection and it should be added as a dependency.
 * 
 * @author Brian Estrada <brianseg014@gmail.com>
 * @see DriverManager#getConnection(java.lang.String, java.lang.String, java.lang.String)
 */

public final class ConnectionFactory {
     
    /**
     * Attemp to established a new connection to a database given the connection parameters. This method used
     * {@link java.sql.DriverManager#getConnection(java.lang.String, java.lang.String, java.lang.String) }
     * to create the new connection, so the project must include the required driver for connection.
     * 
     * @param connectionParameter Parameters for connection to a database
     * @param connectionString URL to attemp connect the database
     * @return a connection to the database
     * @throws ClassNotFoundException if the driver does not exist
     * @throws SQLException if a database access error occurs
     * @exception IllegalArgumentException if connectionParameter or connectionString is null or empty
     */
    public static java.sql.Connection getConnection(ConnectionParameter connectionParameter, ConnectionString connectionString) 
            throws ClassNotFoundException, SQLException{
        if(!(connectionParameter instanceof ConnectionParameter)){
            throw new IllegalArgumentException("connectionParameter cannot be null or empty.");
        }
        if(!(connectionString instanceof ConnectionString)){
            throw new IllegalArgumentException("connectionString cannot be null or empty.");
        }
        
        Class.forName(connectionParameter.getDriver());
        
        java.sql.Connection connection = DriverManager.getConnection(
                connectionString.getConnectionString(), 
                connectionParameter.getUser(),
                connectionParameter.getPassword());

        return connection;
        
    }
    
}
