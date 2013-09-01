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
package com.zodiac.soa.server;

import java.lang.reflect.Field;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public abstract class BusinessLogic {
    
    //private MessageContext messageContext;
    private static MessageContext messageContext;

    public MessageContext getMessageContext() {
        return messageContext;
    }
    
    public static void setMessageContext(BusinessLogic bl, MessageContext messageContext) 
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Field messageContextField = BusinessLogic.class.getDeclaredField("messageContext");
        if(messageContextField != null) {
            messageContextField.setAccessible(true);
            messageContextField.set(bl, messageContext);
        }
    }
    
    public static void setMessageContext(MessageContext messageContext) {
        BusinessLogic.messageContext = messageContext;
    }
    
    public String getApplication(){
        return
            (String)getMessageContext().get(
                com.zodiac.soa.server.MessageContext.APPLICATION);
    }
    
}
