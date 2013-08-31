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

import com.zodiac.security.Session;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Brian Estrada
 */
public class MessageContext implements javax.xml.ws.handler.MessageContext {
    
    public static final String SOA_SESSION = 
            "com.zodiac.security.Session";
    
    public static final String APPLICATION =
            "application";
    
    private javax.xml.ws.handler.MessageContext messageContext;
        
    public MessageContext(javax.xml.ws.handler.MessageContext messageContext){
        this.messageContext = messageContext;
        setSOASession(messageContext);
    }
    
    private void setSOASession(Session session, 
            javax.xml.ws.handler.MessageContext messageContext){
        HttpServletRequest httpServletRequest = 
                (HttpServletRequest)messageContext.get(SERVLET_REQUEST);
        HttpSession httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute(MessageContext.SOA_SESSION, session);
    }
    
    private void setSOASession(javax.xml.ws.handler.MessageContext messageContext){
        HttpServletRequest httpServletRequest = 
                (HttpServletRequest)messageContext.get(SERVLET_REQUEST);
        HttpSession httpSession = httpServletRequest.getSession(false);
        
        Session session = null;
        if(httpSession != null){
            session = (Session)httpSession.getAttribute(MessageContext.SOA_SESSION);
        }
        messageContext.put(SOA_SESSION, session);
    }
    
    @Override
    public Object get(Object key) {
        return messageContext.get(key);
    }
    
    @Override
    public void setScope(String name, Scope scope) {
        messageContext.setScope(name, scope);
    }

    @Override
    public Scope getScope(String name) {
        return messageContext.getScope(name);
    }

    @Override
    public int size() {
        return messageContext.size();
    }

    @Override
    public boolean isEmpty() {
        return messageContext.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return messageContext.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return messageContext.containsValue(value);
    }

    @Override
    public Object put(String key, Object value) {
        if(key.equals(SOA_SESSION)){
            if(value instanceof Session){
                setSOASession((Session)value, messageContext);
            } else {
                throw new IllegalArgumentException(
                        "The session object is not instance of com.zodiac.security.Session");
            }
        }
        return messageContext.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return messageContext.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        messageContext.putAll(m);
    }

    @Override
    public void clear() {
        messageContext.clear();
    }

    @Override
    public Set<String> keySet() {
        return messageContext.keySet();
    }

    @Override
    public Collection<Object> values() {
        return messageContext.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return messageContext.entrySet();
    }
    
}
