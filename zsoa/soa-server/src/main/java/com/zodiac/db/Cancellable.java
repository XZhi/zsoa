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
 * A sql operation that can be cancelled. The main method is <tt>cancel()</tt>, 
 * but the programmer can implement <tt>cancel(CaCancelQueryListener cancelListener)</tt>
 * to cancel the operation using another thread and listening when the
 * operation has finished.
 * 
 * This methods should be used by the final user. If the programmer needs a
 * timeout, configure it using {@link java.sql.Statement#setQueryTimeout(int) }.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public interface Cancellable {
    
    /**
     * Interrup a sql operation.
     * 
     * @throws SQLException if database error occurs
     */
    public void cancel() throws DataAccessException;
    
    /**
     * Interrup a sql operations using another thread.
     * 
     * @param cancelListener listener to know when cancellation ends.
     * @throws SQLException if database error occurs
     */
    public void cancel(CancelQueryListener cancelListener) throws DataAccessException;
    
}
