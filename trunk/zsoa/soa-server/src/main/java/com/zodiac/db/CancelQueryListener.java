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
 * The listener to receive the CancelThread event.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public interface CancelQueryListener {
    
    /**
     * Invoke when the cancelation of query execution has
     * ended.
     * 
     * @param canceled if the query execution has been canceled or not.
     */
    public void finished(boolean canceled);
    
}
