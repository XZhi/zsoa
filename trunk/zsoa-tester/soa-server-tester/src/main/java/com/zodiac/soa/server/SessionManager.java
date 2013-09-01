/*
 * Copyright (C) 2013 Zodiac Innovation
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
package com.zodiac.soa.server;

import com.zodiac.soa.SessionException;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class SessionManager extends SessionBusinessLogic {
    
    public SessionManager(){
        
    }
    
    public boolean test(String username, String password){
        if(username == null || username.length() == 0 
                || password == null || password.length() == 0){
            throw new SessionException("username and password cannot be null or empty");
        }
        if (username.equals("zuser") && password.equals("zpass")){
            set(new Session(open()));
            return true;
        } else {
            return false;
        }
    }
    
}
