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

import com.zodiac.soa.Request;
import com.zodiac.soa.Response;
import java.net.URL;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 * This class is design for an easy implementation of
 * <tt>DynamicService</tt> class. It has one 
 * method, <tt>run(Request)</tt>, to execute a request
 * and interpret the reponse.
 * 
 * @see com.zodiac.soa.client.DynamicService
 * 
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class DynamicServiceHandler {
    
    private DynamicService service;
    
    public DynamicServiceHandler(URL url) {
        service = new DynamicServiceImpl(url).getDynamicPort();
        getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, Boolean.TRUE);
    }
    
    /**
     * Execute the request, interpret the arguments and return the
     * result.
     * 
     * @param request a Request to be executed.
     * @return an object
     * @throws Exception an Exception thrown in server. 
     */
    public Object run(Request request, String applicationId) throws Exception{
        String result = this.service.run(request.toXML());
        
        Response response = new Response();
        response.fromXML(result);
        
        if(response.getException() instanceof Exception){
            throw response.getException();
        }
        
        return response.getResult();
    }
    
    private Map<String, Object> getRequestContext(){
        Map<String, Object> rc = 
                ((BindingProvider)service).getRequestContext();
        return rc;
    }
    
}
