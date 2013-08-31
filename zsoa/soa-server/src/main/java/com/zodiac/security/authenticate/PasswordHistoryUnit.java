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
package com.zodiac.security.authenticate;

import java.util.Date;

/**
 *
 * @author brian
 */
public class PasswordHistoryUnit implements Comparable<PasswordHistoryUnit> {
        
    private byte[] hash;
    
    private byte[] salt;

    private Date establishedDate;
    
    public PasswordHistoryUnit(byte[] hash, byte[] salt, Date establishedDate) {
        this.hash = hash;
        this.salt = salt;
        this.establishedDate = establishedDate;
    }
    
    public Date getEstablishedDate() {
        return establishedDate;
    }

    public byte[] getHash() {
        return hash;
    }

    public byte[] getSalt() {
        return salt;
    }
    
    @Override
    public int compareTo(PasswordHistoryUnit o) {
        return this.getEstablishedDate().compareTo(o.getEstablishedDate());
    }
    
}
