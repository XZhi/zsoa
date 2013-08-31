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

import com.zodiac.db.CancelQueryListener;
import com.zodiac.db.Cancellable;
import com.zodiac.db.Query;
import com.zodiac.db.Transaction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class provide a skeletal implementation of the <tt>DAO</tt>
 * interface to minimize the effort required to implement this interface.
 * 
 * To implement data access entities, the programmer should extend this
 * class and add the methods to execute queries by calling <tt>setQuery()</tt>
 * to create a new query, <tt>getQuery()</tt> to set the required attributes
 * or by calling <tt>setTransaction()</tt> to create a new transaction with
 * multiple queries. The programmer must call <tt>setQuery()</tt> as many
 * times as he need.
 * 
 * Each entity class should refer a table of the database, and the params
 * for the query are attributes defined in the entity class. If the method
 * need an extra param, the programmer have to declare it as method params.
 * 
 * The defined methods should have the minimum operations. For exmple:
 * 
 * <tt>
 * public class User extends AbstractDataBaseDAO<UserDTO> {
 * 
 *  private int user_id;
 * 
 *  private String name;
 * 
 *  public User(Connection connection){
 *      super(connection);
 *  }
 * 
 *  public void getUser() {
 *      setQuery();
 *      String query = "SELECT user_id, name FROM user WHERE user_id = ? ";
 *      getQuery().setQueryString(query);
 *      getQuery().addQueryParams(getUser_id());
 *      getQuery().execute();
 *      setResultSet(getQuery().getResultSet());
 *  }
 * }
 * </tt>
 * 
 * And to access the ResultSet information:
 * 
 * <tt>
 * public void test(Connection c) {
 *      User u = new User(c);
 *      u.setUser_id(123);
 *      u.getUser();
 *      while(u.next()){
 *          UserDTO name = u.getDTO();
 *      }
 * }
 * </tt>
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public abstract class AbstractDataBaseDAO<T> implements DAO<T>, Cancellable {
        
    private List<DAOListener> listeners;
    
    /**
     * Current query for the database access methods supported by 
     * <tt>PreparedStatement</tt>. This instance is used for both
     * individual operations and transactions.
     * 
     * @see Query
     */
    private Query query;
    
    /**
     * Current transaction for the database access methods supported
     * by <tt>PreparedStatement</tt>.
     * 
     * @see Transaction
     */
    private Transaction transaction;
    
    /**
     * Connection used as default for <tt>setQuery()</tt> and 
     * <tt>setTransaction()</tt>.
     * 
     * @see Connection
     */
    private Connection connection;
    
    /**
     * The ResultSet from the last select executed.
     */
    private ResultSet resultSet;
        
    /**
     * SOLE Constructor.
     */
    public AbstractDataBaseDAO() {
        this.query = null;
        this.transaction = null;
    }
    
    /**
     * Constructor to set the global default connection.
     * 
     * @param connection Connection used for <tt>setQuery()</tt> and <tt>setTransaction()</tt>.
     */
    public AbstractDataBaseDAO(Connection connection){
        this();
        setConnection(connection);
    }

    /** {@inheritDoc }
     */
    @Override
    public void cancel() throws DataAccessException {
        getQuery().cancel();
    }

    /** {@inheritDoc }
     */
    @Override
    public void cancel(CancelQueryListener cancelListener) throws DataAccessException {
        getQuery().cancel(cancelListener);
    }
    
    /**
     * Set a new transaction instance with the default connection. This method 
     * should be called to work a transaction at any place in the class.
     * 
     * @throws SQLException if a database error occurs
     */
    protected void setTransaction()  throws SQLException {
        setTransaction(connection);
    }
    
    /**
     * Set a new query instace with the default connection. This method should 
     * be called to work a query at any place in the class.
     * 
     * @throws SQLException if a database error occurs.
     */
    protected void setQuery() throws SQLException {
        setQuery(connection);
    }
    
    /**
     * Set a new transaction instance with the given connection. This method
     * should be called to work a transaction at any place in the class.
     * 
     * @param connection Connection to be use by the operations of a transaction.
     * @throws SQLException if database error occurs
     */
    protected void setTransaction(Connection connection) throws SQLException {
        this.transaction = new Transaction(connection);
    }
    
    /**
     * Set a new query instance with the given connection. This method should
     * be called to work a query at any place in the class.
     * @param connection 
     */
    protected void setQuery(Connection connection) {
        this.query = new Query(connection);
    }
    
    /**
     * Retrieve the current transaction to execute new queries or make a commit,
     * rollback and others operations.
     * 
     * @return the current transaction
     */
    protected Transaction getTransaction(){
        return this.transaction;
    }
    
    /**
     * Retrieve the current query for being configured.
     * 
     * @return the current query.
     */
    protected Query getQuery(){
        return this.query;
    }

    /**
     * Set the ResultSet of the last select query.
     * 
     * @param resultSet ResultSet of the last select.
     */
    protected void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    /** {@inheritDoc }
     */
    public ResultSet getResultSet() {
        return resultSet;
    }
    
    /** {@inheritDoc }
     */
    public final void setConnection(Connection connection) {
            this.connection = connection;
    }

    @Override
    public boolean next() throws DataAccessException {
        try {
            return this.resultSet.next();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
    
    @Override
    public HashMap<String, Object> getData() throws DataAccessException{
        try {
            HashMap hm = new HashMap();
            ResultSetMetaData metadata = this.resultSet.getMetaData();
            for (int i = 0; i < metadata.getColumnCount(); i++) {
                hm.put(
                        metadata.getColumnName(i),
                        this.resultSet.getObject(i));
            }
            return hm;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<T> getListDTO() throws DataAccessException {
        List<T> result = new ArrayList();
        while(this.next()){
            result.add(getDTO());
        }
        return result;
    }

    @Override
    public void addDAOListener(DAOListener listener) {
        if(listeners == null){
            listeners = new ArrayList();
        }
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    @Override
    public void removeDAOListener(DAOListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        listeners.clear();
    }

    protected void notifyArrival(){
        notifyArrival(new DAOEventArgs(this));
    }
    
    private void notifyArrival(DAOEventArgs eventArgs){
        for(DAOListener listener : listeners){
            listener.Response(eventArgs);
        }
    }
    
}
