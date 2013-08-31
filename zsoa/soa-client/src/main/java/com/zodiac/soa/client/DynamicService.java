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
package com.zodiac.soa.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;

/**
 * The <tt>DynamicService</tt> Interface provides a method to execute a
 * request serialized in xml XStream format.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 * @see com.zodiac.soa.Request
 * @see com.thoughtworks.xstream.XStream
 */
@WebService(name="DynamicService")
@SOAPBinding(style= SOAPBinding.Style.RPC)
public interface DynamicService {
    
    /**
     * Method to make a dynamic invocation and return a response.
     * 
     * @param xml a xml xstream format (request).
     * @return a xml stream format (response)
     */
    @WebMethod(operationName="Run", action="RunAction")
    @WebResult(name="xmlResponse", partName="xmlResponse")
    @Action(input="RunAction", output="http://server.soa.zodiac.com/DynamicService/RunResponse")
    public String run(
            @WebParam(name="xmlRequest", partName="xmlRequest") 
            String xml);
    
}
