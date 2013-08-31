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

import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author brian
 */
public class PasswordHistory implements PasswordRule {
    
    private int countInvalid;
    
    public PasswordHistory(int countInvalid) {
        this.countInvalid = countInvalid;
    }
    
    @Override
    public boolean compliant(Password password, Object ... args) {
        if(password == null){
            throw new NullPointerException("Password cannot be null");
        }
        if(args == null || args.length < 1){
            throw new IllegalArgumentException("Args cannot be null or empty");
        }
        
        String passwd = (String)args[0];
        Iterator<PasswordHistoryUnit> history = password.getHistory();
        
        int index = 0;
        while(history.hasNext() && index < this.countInvalid) {
            PasswordHistoryUnit historyUnit = history.next();
            
            byte[] historyHash = historyUnit.getHash();
            byte[] hash = password.getHash(passwd, historyUnit.getSalt());
            
            if(Arrays.equals(historyHash, hash)){
                return false;
            }
            
            index++;
        }
        
        return true;
    }
    
}
