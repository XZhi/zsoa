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

/**
 *
 * @author brian
 */
public class PasswordLength implements PasswordRule{

    private int minLength;
    
    public PasswordLength(int minLenght) {
        setMinLength(minLength);
    }
    
    @Override
    public boolean compliant(Password password, Object ... args) {
        if(args == null || args.length < 1){
            throw new IllegalArgumentException("Args cannot be null or empty");
        }
        
        String passwd = (String)args[0];
        return passwd.length() >= getMinLength();
    }
    
    private void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    private int getMinLength() {
        return minLength;
    }
    
}
