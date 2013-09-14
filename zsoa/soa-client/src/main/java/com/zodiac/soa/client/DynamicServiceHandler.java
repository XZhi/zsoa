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

import com.zodiac.security.OneTimePassword;
import com.zodiac.soa.HttpHeadersConstants;
import com.zodiac.soa.Request;
import com.zodiac.soa.RequestException;
import com.zodiac.soa.Response;
import com.zodiac.soa.SOAException;
import com.zodiac.soa.ServerException;
import com.zodiac.soa.ZSOAConfigurator;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;

/**
 * This class is design for an easy implementation of
 * <tt>DynamicService</tt> class. It has one method, <tt>run(Request)</tt>, to
 * execute a request and interpret the reponse.
 *
 * @see com.zodiac.soa.client.DynamicService
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class DynamicServiceHandler {

    private DynamicService service;
    private String applicationName;
    private String applicationPassword;
    private int countDigits;
    private int tokenLifetime;

    public DynamicServiceHandler(URL url) {
        this(url, null, null, 0, 0);
    }

    public DynamicServiceHandler(URL url, String applicationName, String applicationPassword,
            int countDigits, int tokenLifetime) {
        service = new DynamicServiceImpl(url).getDynamicPort();
        getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, Boolean.TRUE);

        setApplicationName(applicationName);
        setApplicationPassword(applicationPassword);
        setCountDigits(countDigits);
        setTokenLifetime(tokenLifetime);
    }

    /**
     * Execute the request, interpret the arguments and return the result.
     *
     * @param request a Request to be executed.
     * @return an object
     * @throws SOAException an Exception thrown in server as final user
     * exception.
     * @throws ServerException an Exception thrown in server as developer debug
     * exception.
     */
    public Object run(Request request) {
        Map<String, List<String>> headers = new HashMap();
        if (getApplicationName() != null && getApplicationName().length() > 0) {
            headers.put(HttpHeadersConstants.Z_APPLICATION, Arrays.asList(getApplicationName()));
            if (getApplicationPassword() != null && getApplicationPassword().length() > 0) {
                headers.put(HttpHeadersConstants.Z_TOKEN, Arrays.asList(getTokenTOTP()));
            }
        }
        getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);

        String result = this.service.run(request.toXML());

        Response response = new Response();
        response.fromXML(result);

        if (response.getException() instanceof SOAException) {
            throw (SOAException) response.getException();
        } else if (response.getException() instanceof ServerException) {
            response.getException().printStackTrace();
            throw (ServerException) response.getException();
        }

        return response.getResult();
    }

    private final Map<String, Object> getRequestContext() {
        Map<String, Object> rc =
                ((BindingProvider) service).getRequestContext();
        return rc;
    }

    protected DynamicService getService() {
        return service;
    }

    public final void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public final void setApplicationPassword(String applicationPassword) {
        this.applicationPassword = applicationPassword;
    }

    public final void setCountDigits(int countDigits) {
        this.countDigits = countDigits;
    }

    public final void setTokenLifetime(int tokenLifetime) {
        this.tokenLifetime = tokenLifetime;
    }

    protected String getApplicationName() {
        return this.applicationName;
    }

    protected String getApplicationPassword() {
        return this.applicationPassword;
    }

    protected int getCountDigits() {
        return this.countDigits;
    }

    protected int getTokenLifetime() {
        return this.tokenLifetime;
    }

    protected String getTokenTOTP() {
        int X = getTokenLifetime();
        String application_password = getApplicationPassword();
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int countDigits = getCountDigits();
        boolean encryptWithChecksum = ZSOAConfigurator.getInstance().get(
                ZSOAConfigurator.ENCRYPT_WITH_CHECKSUM, boolean.class);
        int encryptOffset = ZSOAConfigurator.getInstance().get(
                ZSOAConfigurator.ENCRYPT_OFFSET, int.class);
        try {
            long movingFactor = now.getTimeInMillis() / X;
            String zToken = OneTimePassword.generateOTP(
                    application_password.getBytes(),
                    movingFactor,
                    countDigits,
                    encryptWithChecksum,
                    encryptOffset);
            return zToken;
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new RequestException(ex);
        }
    }
}
