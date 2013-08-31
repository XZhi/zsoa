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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class simplify the execution of sql queries. It has a
 * minimal internal configuration for the connection and the 
 * programmer can use it if does not need advanced features.
 * 
 * To implement this class, the programmer at first call
 * <tt>setQueryString(String queryString)</tt> to create the internal 
 * <tt>PreparedStatement</tt>. The programmer needs to call 
 * <tt>addQueryParams(Object... param)</tt> to add the statement
 * with the given params. In this way, the programmer can multi insert 
 * lines.
 * 
 * For the other SQL Statements, call once <tt>addQueryParams</tt>.
 * 
 * At last, call <tt>execute()</tt> to run the query at dbms. The 
 * result of select can be access with <tt>getResultSet()</tt>. 
 * To get the count of the rows involved in any sql statement call
 * <tt>getRowSize()</tt>.
 * 
 * For example:
 * <tt>
 * public void getUser() {
 *      setQuery();
 *      String query = "SELECT user_id, name FROM user WHERE user_id = ? ";
 *      getQuery().setQueryString(query);
 *      getQuery().addQueryParams(getUser_id());
 *      getQuery().execute();
 *      ResultSet rs = getQuery().getResultSet();
 * }
 * </tt>
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class Query implements Executeable, Cancellable {
    
    /**
     * Connection used to all operations on DBMS.
     */
    private Connection connection;
    
    /**
     * Statement created by the query seted.
     */
    private PreparedStatement preparedStatement;
    
    /**
     * Constructor to get the connection.
     * 
     * @param connection Connection used to all operations on DBMS.
     */
    public Query(Connection connection) {
        setPreparedStatement(null);
        setConnection(connection);
    }

    /**
     * Set the statement.
     * 
     * @param preparedStatement PreparedStatement to be used in the whole class.
     */
    private void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }
    
    /**
     * Set the connection.
     * 
     * @param connection Connection used to all operations on DBMS.
     */
    public final void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Retrieve the connection.
     * 
     * @return Connection used to all operations on DBMS.
     */
    protected Connection getConnection() {
        return connection;
    }

    /**
     * Creates a PreparedStatement object for sending parameterized SQL statements to the database.
     * 
     * @param queryString an SQL statement that may contain one or more '?' IN parameter placeholders
     * @throws SQLException if a database access error occurs
     */
    public void setQueryString(String queryString) 
            throws SQLException {
        this.preparedStatement = getConnection().prepareStatement(queryString);
    }

    /**
     * Adds a set of parameters to this PreparedStatement object's batch of commands.
     * Do not use this method more than once unless your query is an INSERT.
     * 
     * @param param Sets the parameters in order of the queryString in <tt>setQueryString(String queryString)</tt>.
     * @throws SQLException if a database access error occurs
     */
    public void addQueryParams(Object... param) 
            throws SQLException {
        checkPreparedStatement();
        for(int i = 0; i < param.length; i++){
            this.preparedStatement.setObject(i + 1, param[i]);
        }
        this.preparedStatement.addBatch();
    }

    /**
     * Executes the given SQL statement, which may return multiple results. 
     * In some (uncommon) situations, a single SQL statement may return multiple 
     * result sets and/or update counts. Normally you can ignore this unless 
     * you are (1) executing a stored procedure that you know may return multiple 
     * results or (2) you are dynamically executing an unknown SQL string.
     * 
     * The execute method executes an SQL statement and indicates the form of the first 
     * result. You must then use the methods getResultSet or getUpdateCount to retrieve 
     * the result, and getMoreResults to move to any subsequent result(s).
     * 
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void execute() 
            throws DataAccessException {
        checkPreparedStatement();
        try {
            this.preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
    
    /**
     * Retrieves the current result as a ResultSet object.
     * 
     * @return the result as a ResultSet object or null if the result is an update count or there are no more results
     * @throws SQLException if a database access error occurs
     */
    public ResultSet getResultSet() 
            throws DataAccessException {
        checkPreparedStatement();
        try {
            return this.preparedStatement.getResultSet();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Retrieves the result as count of the rows involved.
     * 
     * @return
     * @throws SQLException 
     */
    public int getRowSize() throws DataAccessException {
        try {
            checkPreparedStatement();
            if(this.preparedStatement.getUpdateCount() > -1) {
                return this.preparedStatement.getFetchSize();
            } else {
                return this.preparedStatement.getUpdateCount();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Cancels the Statement object if both the DBMS and driver support aborting an SQL statement.
     * 
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void cancel() throws DataAccessException {
        try {
            checkPreparedStatement();
            this.preparedStatement.cancel();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
    
    /**
     * This use <tt>cancel()</tt> using a <tt>Thread</tt>.
     * 
     * @throws SQLException if a database access error occurs
     * @see Query#cancel() 
     */
    @Override
    public void cancel(CancelQueryListener cancelListener) throws DataAccessException {
        checkPreparedStatement();
        new CancelThread(this, cancelListener).start();
    }
    
    /**
     * A thread to cancel the execution of a query statement.
     */
    private class CancelThread extends Thread {
        
        /**
         * Query to be canceled.
         */
        private Query query;
        
        /**
         * The listener for the <tt>finished</tt> event.
         */
        private CancelQueryListener listener;
        
        /**
         * Constructor to set the inicial params.
         * 
         * @param query Query to be canceled.
         * @param listener The listener for the <tt>finished</tt> event
         */
        public CancelThread(Query query, CancelQueryListener listener) {
            this.query = query;
            this.listener = listener;
        }

        /**
         * Execute {@link Query#cancel()} and call {@link CancelQueryListener#finished(boolean) } when
         * the method has ended.
         */
        @Override
        public void run() {
            try {
                if(this.query == null){
                    throw new NullPointerException("Query cannot be null or empty");
                }
                
                this.query.cancel();
                
                if(this.listener != null){
                    this.listener.finished(true);
                }
            } catch (DataAccessException ex) {
                if(this.listener != null){
                    this.listener.finished(false);
                }
            }
        }
        
    }
    
    private void checkPreparedStatement() {
        if(this.preparedStatement == null){
            throw new NullPointerException("Query has not been set.");
        }
    }
    
        
}
