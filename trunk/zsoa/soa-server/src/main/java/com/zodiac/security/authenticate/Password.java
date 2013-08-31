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
import java.util.Iterator;

/**
 *
 * @author brian
 */
public interface Password {
    
    public byte[] getHash();
    
    public byte[] getHash(String password);
    
    public byte[] getHash(String password, byte[] salt);
    
    public boolean match(String password);
        
    public int getIterations();
    
    public byte[] getSalt();
    
    public Date getEstablishedDate();
        
    public Iterator<PasswordHistoryUnit> getHistory();
    
    public boolean addHistory(PasswordHistoryUnit unit);
    
    public PasswordHistoryUnit toPasswordHistoryUnit();
    
    public String getHashBase64();
    
    public String getSaltBase64();
    
}
