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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class pretend to be a model of the information required by a database connection.
 * 
 * When a programmer implement a new way to connect a database, must ask for a <tt>ConnectionParameter</tt>
 * to know all the data that the connection may need.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class ConnectionParameter {
    
    /**
     * Name or ip address to the database server. For example localhost or 127.0.0.1. Make sure
     * the server can be reached.
     */
    private String host;
    
    /**
     * Port that is listening in the database server for the connection. For example the default
     * ports for dbms mysql (3306), postgresql (5432).
     */
    private String port;
    
    /**
     * Database name on the database server.
     */
    private String database;
    
    /**
     * A user that has privileges to do the required operations like connect.
     */
    private String user;
    
    /**
     * The password for the selected user.
     */
    private String password;
    
    /**
     * Driver class used to connect the database;
     */
    private String driver;
    
    /**
     * DBMS listening in the database server For example mysql, postgresql.
     */
    private String dbms;
    
    /**
     * Aditional args for the connection string.
     */
    private Map<String, String> args;

    /**
     * SOLE constructure.
     */
    public ConnectionParameter() {
        this.args = new HashMap();
    }

    /**
     * Constructure to initialize all the connection information.
     * 
     * @param host Name or ip address to the database server. For example localhost or 127.0.0.1. Make sure
     * the server can be reached.
     * @param port Port that is listening in the database server for the connection. For example the default
     * ports for dbms mysql (3306), postgresql (5432).
     * @param database Database name on the database server.
     * @param user A user that has privileges to do the required operations like connect.
     * @param password The password for the selected user.
     * @param driver Driver class used to connect the database;
     * @param dbms DBMS listening in the database server For example mysql, postgresql.
     */
    public ConnectionParameter(String host, String port, String database, String user, String password,
            String driver, String dbms) {
        this();
        setHost(host);
        setPort(port);
        setDatabase(database);
        setUser(user);
        setPassword(password);
        setDriver(driver);
        setDBMS(dbms);
    }
    
    /**
     * Retrieve the host.
     * 
     * @return a hostname or ip address.
     */
    public final String getHost() {
        return host;
    }

    /**
     * Set the host.
     * 
     * @param host Name or ip address to the database server. For example localhost or 127.0.0.1. Make sure
     * the server can be reached.
     */
    protected final void setHost(String host) {
        this.host = host;
    }
    
    /**
     * Retrieve the port number.
     * 
     * @return Port that is listening in the database server for the connection. For example the default
     * ports for dbms mysql (3306), postgresql (5432).
     */
    public final String getPort() {
        return port;
    }

    /**
     * Set the port.
     * 
     * @param port Port that is listening in the database server for the connection. For example the default
     * ports for dbms mysql (3306), postgresql (5432).
     */
    protected final void setPort(String port) {
        this.port = port;
    }

    /**
     * Retrieve the database name.
     * 
     * @return Database name on the database server.
     */
    public final String getDatabase() {
        return database;
    }

    /**
     * Set the database name.
     * 
     * @param database Database name on the database server.
     */
    protected final void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Retrieve the user.
     * 
     * @return A user that has privileges to do the required operations like connect.
     */
    public final String getUser() {
        return user;
    }

    /**
     * Set the user.
     * 
     * @param user A user that has privileges to do the required operations like connect.
     */
    protected final void setUser(String user) {
        this.user = user;
    }

    /**
     * Retrieve the password of the user.
     * 
     * @return The password for the selected user.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Set the password user.
     * 
     * @param password The password for the selected user.
     */
    protected final void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrive the driver class.
     * 
     * @return Driver class used to connect a database.
     */
    public final String getDriver() {
        return driver;
    }

    /**
     * Set the driver.
     * 
     * @param driver Driver class used to connect a database.
     */
    protected final void setDriver(String driver) {
        this.driver = driver;
    }
    
    /**
     * Retrieve the dbms name.
     * 
     * @return DBMS listening in the database server For example mysql, postgresql.
     */
    public final String getDBMS() {
        return dbms;
    }

    /**
     * Set the dbms name.
     * 
     * @param dbms DBMS listening in the database server For example mysql, postgresql.
     */
    protected final void setDBMS(String dbms) {
        this.dbms = dbms;
    }

    /**
     * Retrieve the addional args for the connection strings.
     * 
     * @return Aditional args for the connection string.
     */
    public final Set<String> getArgs() {
        return args.keySet();
    }

    /**
     * Set the addiotional string args.
     * 
     * @param key Identifier of argument
     * @param value argument
     */
    protected void addArgs(String key, String value) {
        this.args.put(key, value);
    }
    
    /**
     * Get a argument
     * 
     * @param key Identifier of argument
     */
    public final String getArg(String key){
        return args.get(key);
    }
    
}
