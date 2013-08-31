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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * An entity is a reflect of a table in the database. It has
 * methods with specific queries for getting, updating, deleting, etc
 * the information from the database.
 * 
 * Implementing the entity model, the programmer can have
 * the data tier. The programmer have to try
 * to make as simple as he can each method, by just
 * programming the required query. The methods should be atomics.
 * 
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public interface DAO<T> {
    
    /**
     * Retrieve a hashmap indexed by the field name of the table
     * and the value. When the programmer call <tt>next()</tt>,
     * he can call this method and get all the data in this form.
     * 
     * This information corresponds to a row retrieve form the database.
     * 
     * @return a hashmap with the row information.
     */
    public HashMap<String,Object> getData() throws DataAccessException;
    
    /**
     * Retrieve dto struct
     * 
     * @return a dto
     */
    public T getDTO() throws DataAccessException;
    
    /**
     * Retrieve the next row of the ResultSet return
     * of the last select query executed.
     * 
     * @return
     * @throws SQLException 
     */
    public boolean next() throws DataAccessException;
    
    /**
     * Retrieve a list of all dto
     * 
     * @return a list
     */
    public List<T> getListDTO() throws DataAccessException;
        
    public void addDAOListener(DAOListener listener);
    
    public void removeDAOListener(DAOListener listener);
    
    public void removeAllListeners();
}
