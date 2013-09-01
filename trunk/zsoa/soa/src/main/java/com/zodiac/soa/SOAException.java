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
package com.zodiac.soa;

/**
 * Throw to indicate a soa server exception.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class SOAException extends ServerException {

    /**
     * {@inherits}
     */
    public SOAException() {
    }
    
    /**
     * {@inherits}
     */
    public SOAException(String message) {
        super("an error has been thrown on soa server: " + message);
    }

    /**
     * {@inherits}
     */
    public SOAException(Throwable cause) {
        super(cause);
    }

    /**
     * {@inherits}
     */
    public SOAException(String message, Throwable cause) {
        super("an error has been thrown on server soa: " + message, cause);
    }
    
}
