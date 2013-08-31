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
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class PasswordComplex implements PasswordRule {

    public PasswordComplex() {
    }
    
    @Override
    public boolean compliant(Password password, Object... args) {
        if(args == null || args.length < 1) {
            throw new IllegalArgumentException("Args cannot not be null or empty");
        }
        
        String passwd = (String)args[0];
        int length = passwd.length();
        if(length < 6) {
            return false;
        }
        
        int upper = 0, lower = 0, digit = 0, non_alphanumeric = 0;
        for(int i = 0; i < length; i++) {
            char c = passwd.charAt(i);
            
            if(Character.isUpperCase(c)) {
                upper = 1;
            } else if (Character.isLowerCase(c)) {
                lower = 1;
            } else if (Character.isDigit(c)) {
                digit = 1;
            } else if (!Character.isLetterOrDigit(c)) {
                non_alphanumeric = 1;
            }
            
        }
        if ((upper + lower + digit + non_alphanumeric) >= 3) {
            return true;
        }
        return false;
    }
        
    
}
