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
package com.zodiac.soa;

/**
 * Structure for  responding a request invoke.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class Response extends AbstractDTO {
    
    /**
     * Exception to be thrown at the requester.
     */
    private ServerException exception;
    
    /**
     * The trace exception when request is textModeException
     */
    private String textException;
    
    /**
     * The result of the request.
     */
    private Object result;

    public Response() {
    }
    
    public Response(ServerException e){
        this.exception = e;
    }
    
    public Response(String textException){
        this.textException = textException;
    }
    
    public Response(Object result){
        this.result = result;
    }
    
    public Exception getException() {
        return exception;
    }

    public String getTextException() {
        return textException;
    }
    
    public Object getResult() {
        return result;
    }
    
    
}
