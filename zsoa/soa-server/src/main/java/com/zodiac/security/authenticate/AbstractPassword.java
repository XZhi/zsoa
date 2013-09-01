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

import com.zodiac.security.Base64;
import com.zodiac.util.SortedList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public abstract class AbstractPassword extends Object implements Password {
    
    private byte[] hash;
    
    private byte[] salt;
    
    private Date establishedDate;
    
    private List<PasswordHistoryUnit> history;
    
    private AbstractPassword() {
        this.history = new SortedList();
    }
    
    public AbstractPassword(String password, byte[] salt) {
        this();
        setHash(getHash(password, salt));
        setSalt(salt);
        setEstablishedDate(new Date());
    }
    
    public AbstractPassword(byte[] hash, byte[] salt, Date establishedDate){
        this();
        setHash(hash);
        setSalt(salt);
        setEstablishedDate(establishedDate);
    }
    
    private void setHash(byte[] hash){
        if(hash == null){
            throw new PasswordException("Password's hash cannot be null.");
        }
        
        this.hash = hash;
    }
    
    private void setSalt(byte[] salt){
        if(salt == null){
            throw new PasswordException("Password's salt cannot be null.");
        }
        
        this.salt = salt;
    }
    
    private void setEstablishedDate(Date establishedDate) {
        this.establishedDate = establishedDate;
    }

    @Override
    public final byte[] getHash() {
        return this.hash;
    }

    @Override
    public final byte[] getHash(String password) {
        return getHash(password, getSalt());
    }

    @Override
    public boolean match(String password) {
        byte[] testHash = getHash(password);
        byte[] currentHash = getHash();
        
        if(testHash == null){
            throw new PasswordException("New Password's hash should no be null.");
        }
        
        int diff = testHash.length ^ getHash().length;
        for(int i = 0; i < testHash.length && i < getHash().length; i++){
            diff |= testHash[i] ^ currentHash[i];
        }
        
        return diff == 0;
    }
    
    @Override
    public byte[] getSalt() {
        return this.salt;
    }

    @Override
    public Date getEstablishedDate() {
        return this.establishedDate;
    }

    @Override
    public Iterator<PasswordHistoryUnit> getHistory() {
        return this.history.iterator();
    }

    @Override
    public boolean addHistory(PasswordHistoryUnit unit) {
        return this.history.add(unit);
    }

    @Override
    public PasswordHistoryUnit toPasswordHistoryUnit() {
        return new PasswordHistoryUnit(getHash(), getSalt(), getEstablishedDate());
    }

    @Override
    public String getHashBase64() {
        return Base64.encode(getHash());
    }
    
    @Override
    public String getSaltBase64() {
        return Base64.encode(getSalt());
    }
    
}
