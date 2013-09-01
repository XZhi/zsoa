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

import com.zodiac.soa.ServerException;
import com.zodiac.security.Session;
import com.zodiac.soa.SessionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public abstract class SessionBusinessLogic extends BusinessLogic {
    
    private HttpSession httpSession;
    
    public SessionBusinessLogic() {}
    
    public void set(Session session){
        if(session == null){
            throw new ServerException("session cannot be null");
        }
        if(httpSession == null){
            throw new SessionException("session is not open");
        }
        if(httpSession.getId() == null
                || !httpSession.getId().equals(session.getSessionID())){
            throw new SessionException("session has no valid jsessionid");
        }
        
        getMessageContext().put(MessageContext.SOA_SESSION, session);
        getHttpSession().setAttribute(MessageContext.SOA_SESSION, session);
    }
    
    public Session getSession(){
        Session session = 
                (Session)getMessageContext().get(MessageContext.SOA_SESSION);
        if(session == null){
            throw new SessionException("session is not valid");
        }
        return session;
    }
    
    public String open() {
        HttpServletRequest httpServletRequest = 
                (HttpServletRequest)getMessageContext().get(MessageContext.SERVLET_REQUEST);
        HttpSession httpSession = this.httpSession = httpServletRequest.getSession(true);
        String jSessionId = httpSession.getId();
        return jSessionId;
    }
    
    public boolean destroy() {
        HttpServletRequest httpServletRequest = 
                (HttpServletRequest)getMessageContext().get(MessageContext.SERVLET_REQUEST);
        HttpSession httpSession = httpServletRequest.getSession(false);
        if(httpSession != null){
            httpSession.invalidate();
            return true;
        } else {
            return false;
        }
    }
    
    public HttpSession getHttpSession() {
        return httpSession;
    }
    
    
    
}
