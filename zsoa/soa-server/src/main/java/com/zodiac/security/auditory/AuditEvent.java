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
package com.zodiac.security.auditory;

import com.zodiac.security.Session;
import java.lang.reflect.Method;
import java.util.Date;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class AuditEvent {
    
    private int id;
    
    private String name;
    
    private String description;
    
    private Class clazz;
    
    private Method method;
    
    private Object[] arguments;
    
    private String remoteHostAddress;
    
    private String applicationID;
    
    private Session session;
    
    private Date dateTime;

    public AuditEvent(int id, String name, String description, Class clazz, Method method, Object[] arguments, String remoteHostAddress, String applicationID, Session session, Date dateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.clazz = clazz;
        this.method = method;
        this.arguments = arguments;
        this.remoteHostAddress = remoteHostAddress;
        this.applicationID = applicationID;
        this.session = session;
        this.dateTime = dateTime;
    }
    
    public String getApplicationID() {
        return applicationID;
    }

    public Class getClazz() {
        return clazz;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    public Object[] getArguments() {
        return arguments;
    }

    

    public String getRemoteHostAddress() {
        return remoteHostAddress;
    }

    public Session getSession() {
        return session;
    }
    
        
}
