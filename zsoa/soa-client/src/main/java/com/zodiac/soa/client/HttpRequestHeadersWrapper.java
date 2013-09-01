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
package com.zodiac.soa.client;

import com.zodiac.security.OneTimePassword;
import com.zodiac.soa.HttpHeadersConstants;
import com.zodiac.soa.SOAException;
import com.zodiac.soa.ZSOAConfigurator;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class HttpRequestHeadersWrapper extends HttpServletRequestWrapper {

    public HttpRequestHeadersWrapper(HttpServletRequest request) {
        super(request);
    }
    
    @Override
    public String getHeader(String name) {
        //get the request object and cast it
        HttpServletRequest request = (HttpServletRequest)getRequest();
         
        //if we are looking for the "username" request header
        if(HttpHeadersConstants.Z_APPLICATION.equals(name)) {
            return ZSOAConfigurator.getInstance().get(
                    ZSOAConfigurator.APPLICATION_ID).toString();
        }
        
        if(HttpHeadersConstants.Z_TOKEN.equals(name)){
            int X = ZSOAConfigurator.getInstance().get(
                    ZSOAConfigurator.ENCRYPT_TOKEN_LIFETIME, int.class);
            String application_password = 
                    ZSOAConfigurator.getInstance().get(
                    ZSOAConfigurator.APPLICATION_PASSWORD, String.class);
            Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            int countDigits = ZSOAConfigurator.getInstance().get(
                    ZSOAConfigurator.ENCRYPT_COUNT_DIGITS, int.class);
            boolean encryptWithChecksum = ZSOAConfigurator.getInstance().get(
                    ZSOAConfigurator.ENCRYPT_WITH_CHECKSUM, boolean.class);
            int encryptOffset = ZSOAConfigurator.getInstance().get(
                    ZSOAConfigurator.ENCRYPT_OFFSET, int.class);
            try {
                String zToken = OneTimePassword.generateOTP(
                                application_password.getBytes(),
                                now.getTimeInMillis() / X,
                                countDigits,
                                encryptWithChecksum,
                                encryptOffset);
                return zToken;
            } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
                throw new SOAException(ex);
            }
        }
         
        //otherwise fall through to wrapped request object
        return request.getHeader(name);
    }
    
    @Override
    public Enumeration getHeaderNames() {
        //create an enumeration of the request headers
        //additionally, add the "username" request header
         
        //create a list
        List list = new ArrayList();
         
        //loop over request headers from wrapped request object
        HttpServletRequest request = (HttpServletRequest)getRequest();
        Enumeration e = request.getHeaderNames();
        while(e.hasMoreElements()) {
            //add the names of the request headers into the list
            String n = (String)e.nextElement();
            list.add(n);
        }
         
        //add new headers
        if(ZSOAConfigurator.getInstance().get(
                ZSOAConfigurator.APPLICATION_ID) != null){
            list.add(HttpHeadersConstants.Z_APPLICATION);
        }
        
        if(ZSOAConfigurator.getInstance().get(
                ZSOAConfigurator.APPLICATION_PASSWORD) != null){
            list.add(HttpHeadersConstants.Z_TOKEN);
        }
         
        //create an enumeration from the list and return
        Enumeration en = Collections.enumeration(list);
        return en;
    }
    
    
}
