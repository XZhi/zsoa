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
package com.zodiac.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.ApplicationConfigurationPopulator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class ZodiacConfigurator {

    private static String CONFIGURATOR_FILENAME = "zsoa.xml";
    private static Map<String, Object> data;
    private static ZodiacConfigurator INSTANCE;

    private ZodiacConfigurator() {
        data = new HashMap();
        defaults();
        File stocks = new File(CONFIGURATOR_FILENAME);

        if (stocks.exists()) {
            xml(stocks);
        }
    }

    public final void defaults() {
        //Defaults
        data.put(ENCRYPT_OFFSET, 10);
        data.put(ONLY_ALLOWED_APPLICATION, false);
        data.put(ENCRYPT_COUNT_DIGITS, 6);
        data.put(ENCRYPT_WITH_CHECKSUM, true);
        data.put(ENCRYPT_TOKEN_LIFETIME, 30000);
    }

    public final void xml(File stocks) {
        try {
            if (!stocks.exists()) {
                throw new FileNotFoundException("could not find " + stocks.getCanonicalPath());
            }

            String filename = stocks.getName();

            //Read file
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(stocks);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("zodiac");
            if (nodeList.getLength() > 1) {
                throw new ParserConfigurationException("zodiac should appear once  in " + filename + ".");
            }

            Element zodiac = (Element) nodeList.item(0);

            //zodiac soa
            nodeList = zodiac.getElementsByTagName("soa");
            if (nodeList.getLength() > 1) {
                throw new ParserConfigurationException("soa should appear once in " + filename + ".");
            }
            Element soa = (Element) nodeList.item(0);

            //zodiac soa auditors
            nodeList = soa.getElementsByTagName("auditors");
            int auditorsCount = nodeList.getLength();
            if (auditorsCount == 1) {
                Element auditors = (Element) nodeList.item(0);

                //zodiac soa auditors auditor
                nodeList = auditors.getElementsByTagName("auditor");
                int auditorCount = nodeList.getLength();
                if (auditorCount > 0) {
                    List<String> auditorList = new ArrayList();
                    for (int i = 0; i < auditorCount; i++) {
                        Node auditor = nodeList.item(i);
                        auditorList.add(auditor.getNodeValue());
                    }
                    data.put(AUDITORS, auditorList);
                }
            } else if (auditorsCount > 1) {
                throw new ParserConfigurationException("auditors should appear once in " + filename + ".");
            }

            //zodiac soa app-auth only-allowed-application
            nodeList = soa.getElementsByTagName("app-auth");
            int countAppAuth = nodeList.getLength();
            if (countAppAuth == 1) {
                Element appAuth = (Element)nodeList.item(0);
                
                nodeList = appAuth.getElementsByTagName("only-allowed-application");
                int countOnlyAllowedApp = nodeList.getLength();
                if (countOnlyAllowedApp == 1) {
                    String only_allowed_application = "false";
                    Element _only_allowed_application = (Element) nodeList.item(0);
                    String only_allowed_application_value = _only_allowed_application.getNodeValue();
                    if (only_allowed_application_value.equals("true")
                            || only_allowed_application_value.equals(("false"))) {
                        only_allowed_application = only_allowed_application_value;
                    } else {
                        throw new ParserConfigurationException("only-allowed-application allowed values true/false in " + filename + ".");
                    }
                    data.put(ONLY_ALLOWED_APPLICATION, Boolean.parseBoolean(only_allowed_application));
                } else if (countOnlyAllowedApp > 1) {
                    throw new ParserConfigurationException("only-allowed-application should appear once in " + filename + ".");
                }

                //zodiac soa app-auth count-digits
                nodeList = appAuth.getElementsByTagName("count-digits");
                int encryptCountDigitsCount = nodeList.getLength();
                if (encryptCountDigitsCount == 1) {
                    Element application_id = (Element) nodeList.item(0);
                    int countDigits = 0;
                    try {
                        countDigits = Integer.valueOf(application_id.getNodeValue());
                    } catch (NumberFormatException e) {
                        countDigits = -1;
                    }
                    if (countDigits < 6 || countDigits > 9) {
                        throw new ParserConfigurationException("count-digits must be a number between 6 and 9");
                    }

                    data.put(ENCRYPT_COUNT_DIGITS, countDigits);
                } else if (encryptCountDigitsCount > 1) {
                    throw new ParserConfigurationException("count-digits should appear once " + filename + ".");
                }

                //zodiac soa app-auth token-lifetime
                nodeList = appAuth.getElementsByTagName("token-lifetime");
                int encryptTokenLifetimeCount = nodeList.getLength();
                if (encryptTokenLifetimeCount == 1) {
                    Element token_lifetime = (Element) nodeList.item(0);
                    int movingFactory = 0;
                    try {
                        movingFactory = Integer.valueOf(token_lifetime.getNodeValue());
                    } catch (NumberFormatException e) {
                        movingFactory = -1;
                    }
                    if(movingFactory < 0){
                        throw new ParserConfigurationException("token-lifetime must be a natural number");
                    }
                    
                    data.put(ENCRYPT_TOKEN_LIFETIME, movingFactory * 1000);
                } else if (encryptCountDigitsCount > 1) {
                    throw new ParserConfigurationException("count-digits should appear once " + filename + ".");
                }

                //zodiac soa app-auth application-id
                nodeList = soa.getElementsByTagName("application-id");
                int applicationIdCount = nodeList.getLength();
                if (applicationIdCount == 1) {
                    Element application_id = (Element) nodeList.item(0);
                    data.put(APPLICATION_ID, application_id.getNodeValue());
                } else if (applicationIdCount > 1) {
                    throw new ParserConfigurationException("application-id should appear once " + filename + ".");
                }

                //zodiac soa app-auth application-password
                nodeList = soa.getElementsByTagName("application-password");
                int applicationPasswordCount = nodeList.getLength();
                if (applicationPasswordCount == 1) {
                    Element _application_password = (Element) nodeList.item(0);
                    String application_password = _application_password.getNodeValue();
                    if (application_password.length() < 10) {
                        throw new ParserConfigurationException("application-password should be equal or greater than 10 characters.");
                    }
                    data.put(APPLICATION_PASSWORD, application_password);
                } else if (applicationPasswordCount > 1) {
                    throw new ParserConfigurationException("application-password should appear once " + filename + ".");
                }

                //zodiac soa app-auth applications
                nodeList = soa.getElementsByTagName("applications");
                int applicationsCount = nodeList.getLength();
                if (nodeList.getLength() == 1) {
                    Element applications = (Element) nodeList.item(0);

                    //zodiac soa app-auth applications application
                    nodeList = applications.getElementsByTagName("application");
                    int applicationCount = nodeList.getLength();
                    if (applicationCount > 0) {
                        Map<String, String> applicationMap = new HashMap();
                        for (int i = 0; i < applicationCount; i++) {
                            Element application = (Element) nodeList.item(i);
                            NodeList _application_id =
                                    application.getElementsByTagName("application-id");
                            NodeList _application_password =
                                    application.getElementsByTagName("application-password");
                            if (_application_id.getLength() != 1 || _application_password.getLength() != 1) {
                                throw new ParserConfigurationException("application-id and application-password "
                                        + "should appear once in " + filename + ".");
                            }
                            Element application_id = (Element) _application_id.item(0);
                            Element application_password = (Element) _application_password.item(0);

                            applicationMap.put(application_id.getNodeValue(), application_password.getNodeValue());
                        }
                        data.put(APPLICATIONS, applicationMap);
                    }
                } else if (applicationsCount > 1) {
                    throw new ParserConfigurationException("applications should appear once in " + filename + ".");
                }
            } else if (countAppAuth > 1){
                throw new ParserConfigurationException("app-auth should appear once in " + filename + ".");
            }
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(ZodiacConfigurator.class.getName()).log(Level.ERROR, null, ex);
        }

    }

    public Object get(String key) {
        return data.get(key);
    }

    public <T> T get(String key, Class<T> type) {
        return (T) get(key);
    }

    public static ZodiacConfigurator getInstance() {
        if (INSTANCE == null) {
            synchronized (ZodiacConfigurator.class) {
                INSTANCE = new ZodiacConfigurator();
            }
        }
        return INSTANCE;
    }
    public static final String AUDITORS = "auditors";
    public static final String APPLICATIONS = "applications";
    public static final String ONLY_ALLOWED_APPLICATION = "only-allowed-application";
    public static final String APPLICATION_ID = "application-id";
    public static final String APPLICATION_PASSWORD = "application-password";
    public static final String ENCRYPT_COUNT_DIGITS = "encrypt-count-digits";
    public static final String ENCRYPT_OFFSET = "encrypt-offset";
    public static final String ENCRYPT_WITH_CHECKSUM = "encrypt-with-checksum";
    public static final String ENCRYPT_TOKEN_LIFETIME = "ecrypt-token-lifetime";
}
