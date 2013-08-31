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
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is an easier way to implement the database transactions.
 * 
 * To implement this class, the programmer only need to call
 * <tt>execute(Query query)</tt> for each query he want to execute,
 * setting savepoints and to finish call <tt>commit()</tt>.
 * 
 * To know the result the programmer can use <tt>commited</tt> or
 * <tt>rollbacked</tt>.
 * 
 * This class keep the schema of the whole package by providing
 * the database connections simplify. So, in case the programmer
 * need advanced features, may need to extends this class or
 * work on directly the connection object.
 * 
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public final class Transaction extends java.lang.Object implements Executeable {
    
    /**
     * The connection to the dbms.
     */
    private Connection connection;
    
    /**
     * The list of queries have been executed.
     */
    private List<Query> queries;
    
    /**
     * State of the transaction.
     * 0 - Executing queries
     * 1 - Commited
     * 2 - Rollbacked
     */
    private int state;
    
    /**
     * Last savepoint to rollback.
     */
    private Savepoint savepoint;
    
    /**
     * Constructor with minimal objects for the class working. Establish
     * the autocommit connection to false.
     * 
     * @param connection The connection to the dbms.
     * @throws SQLException if database error occurs
     */
    public Transaction(Connection connection) throws SQLException {
        this.connection = connection;
        this.queries = new ArrayList();
        this.connection.setAutoCommit(false);
        
        setSavepoint();
        setState(0);
    }
    
    /**
     * Call the method {@link Query#execute() } and add
     * the query to list for the future. These queries can be
     * get calling <tt>getQuery</tt>.
     * 
     * @param query Query to be executed.
     * @return The position's query on the internal list.
     * @throws SQLException if a database error occurs
     * @see Transaction#getQuery(int) 
     */
    public int execute(Query query) throws SQLException {
        query.execute();
        if (this.queries.add(query)){
            return this.queries.indexOf(query);
        } else {
            return -1;
        }
    }
    
    /**
     * Makes all changes made since the previous commit/rollback permanent and 
     * releases any database locks currently held by this Connection object. In case
     * of error, undoes all changes made in the current transaction and releases any 
     * database locks currently held by this Connection object.
     * 
     * @return TRUE if the transaction was successfully or FALSE if make rollabck.
     * @throws SQLException if a database error occurs
     * @see Connection#commit() 
     * @see Connection#rollback() 
     */
    public boolean commit() throws SQLException {
        if(getConnection() == null){
            throw new NullPointerException("Connection cannot be null or empty.");
        }
        try {
            this.connection.commit();
            setState(1);
            return true;
        } catch (SQLException ex) {
            this.connection.rollback();
            setState(2);
            return false;
        } finally {
            this.connection.setAutoCommit(true);
        }
    }
    
    /**
     * Rollback the changes to a set savepoint
     * 
     * @param sp Savepoint obtained by <tt>setSavepoint()</tt>.
     * @throws SQLException if a database error occurs
     */
    public void rollback(Savepoint sp) throws SQLException{
        if(getConnection() == null){
            throw new NullPointerException("Connection cannot be null or empty.");
        }
        
        this.connection.rollback(sp);
    }
    
    /**
     * Establish a current savepoint for a rollback.
     * 
     * @return a new savepoint
     * @throws SQLException if database error occurs
     * @see Connection#setSavepoint() 
     */
    public Savepoint setSavepoint() throws SQLException{
        if(getConnection() == null){
            throw new NullPointerException("Connection cannot be null or empty.");
        }
        
        this.savepoint = this.connection.setSavepoint();
        return this.savepoint;
    }
    
    /**
     * Retrieves the committed state
     * 
     * @return TRUE if transaction's state is committed otherwise FALSE
     */
    public boolean committed(){
        return getState() == 1;
    }
    
    /**
     * Retrieves the rollbacked state
     * 
     * @return TRUE if transaction's state is rollbacked otherwise FALSE
     */
    public boolean rollbacked(){
        return getState() == 2;
    }

    /**
     * set the transaction's state
     * 
     * @param state State of the transaction
     */
    private void setState(int state) {
        this.state = state;
    }

    /**
     * Retrieves the transaction's state
     * 
     * @return State of the transaction
     */
    private int getState() {
        return state;
    }
    
    /**
     * Retrieve a query in a given position.
     * 
     * @param i Index position of the query
     * @return Query in the position
     */
    public Query getQuery(int i){
        return this.queries.get(i);
    }

    /**
     * Retrieves the current connection
     * 
     * @return a connection to dbms
     */
    private Connection getConnection() {
        return connection;
    }

    @Override
    public void execute() throws DataAccessException {
        try {
            commit();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
    
}
