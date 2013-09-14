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

import com.zodiac.soa.ServerException;
import com.zodiac.security.OneTimePassword;
import com.zodiac.soa.HttpHeadersConstants;
import com.zodiac.soa.Request;
import com.zodiac.soa.Response;
import com.zodiac.soa.SOAException;
import com.zodiac.soa.ZSOAConfigurator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * @author Brina Estrada <brianseg014@gmail.com>
 */
@WebService(endpointInterface = "com.zodiac.soa.server.DynamicService",
        serviceName = "DynamicService", portName = "DynamicPort")
public class DynamicServiceImpl implements DynamicService {

    private final static Logger log = Logger.getLogger(DynamicServiceImpl.class);
    @Resource(name = "wsContext")
    private WebServiceContext wsContext;

    @PostConstruct
    public void postConstruct() {
        BasicConfigurator.configure();
    }

    @PreDestroy
    public void preDestroy() {
    }

    @Override
    public String run(String xml) {
        Response response = null;
        try {
            Request request = new Request();
            request.fromXML(xml);

            MessageContext messageContext = new MessageContext(wsContext.getMessageContext());
            try {
                Map<String, Object> headers = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);

                List applicationList = (List)headers.get(HttpHeadersConstants.Z_APPLICATION);
                String application = applicationList != null && applicationList.size() > 0 ? 
                                            (String)applicationList.get(0) : null;
                messageContext.put(MessageContext.APPLICATION, application);

                //Verify if the application is allowed
                boolean only_allowed_application = ZSOAConfigurator.getInstance().get(
                        ZSOAConfigurator.ONLY_ALLOWED_APPLICATION, boolean.class);
                if (only_allowed_application) {
                    if (application == null) {
                        throw new ServerException("Z-Application must be specified in request headers.");
                    }

                    List zTokenList = (List)headers.get(HttpHeadersConstants.Z_TOKEN);
                    String zToken = zTokenList != null && zTokenList.size() > 0 ? 
                                            (String)zTokenList.get(0) : null;
                    if (zToken == null) {
                        throw new ServerException("Z-Token must be specified in request headers.");
                    }
                    
                    Map<String, String> applications =
                            (Map<String, String>) ZSOAConfigurator.getInstance()
                            .get(ZSOAConfigurator.APPLICATIONS);
                    
                    if(applications == null){
                        throw new ServerException("The application " + application
                                + " is not allowed.");
                    }
                    
                    String application_password = applications.get(application);
                    if (application_password == null) {
                        throw new SecurityException("The application " + application
                                + " is not allowed.");
                    }

                    int X = ZSOAConfigurator.getInstance().get(
                            ZSOAConfigurator.ENCRYPT_TOKEN_LIFETIME, int.class);
                    Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    int countDigits = ZSOAConfigurator.getInstance().get(
                            ZSOAConfigurator.ENCRYPT_COUNT_DIGITS, int.class);
                    int encryptOffset = ZSOAConfigurator.getInstance().get(
                            ZSOAConfigurator.ENCRYPT_OFFSET, int.class);
                    boolean encryptWithChecksum = ZSOAConfigurator.getInstance().get(
                            ZSOAConfigurator.ENCRYPT_WITH_CHECKSUM, boolean.class);
                    
                    long movingFactor = now.getTimeInMillis() / X;
                    String zTokenExpected = OneTimePassword.generateOTP(
                            application_password.getBytes(),
                            movingFactor,
                            countDigits,
                            encryptWithChecksum,
                            encryptOffset);

                    if (!zTokenExpected.equals(zToken)) {
                        throw new ServerException("Z-Token is not valid.");
                    }
                }

                Object result = DynamicInvoke.invoke(messageContext, request);
                response = new Response(result);
            } catch (ServerException ex) {
                if (request.isTextModeException()) {
                    response = new Response(ex.toString());
                } else {
                    response = new Response(ex);
                }
            } catch (Exception ex) {
                log.fatal(ex);
                response = new Response(getGenericException());
            }
        } catch (Exception ex) {
            log.fatal(ex);
            response = new Response(getGenericException());
        } finally {
            return response.toXML();
        }
    }

    public ServerException getGenericException() {
        ServerException e =
                new ServerException("An unhandle exception on soa server has been thrown. "
                + "Contact your system administrator or Zodiac Innovation team.");
        return e;
    }
}
