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

/**
 * This are SQL Object that can be executed. For example
 * a query or transaction.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public interface Executeable {
    
    /**
     * Attemp to execute the task of a SQL Object.
     * 
     * @throws SQLException if database error occurs
     */
    public void execute() throws DataAccessException;
}
