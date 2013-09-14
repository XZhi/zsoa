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
package com.zodiac.soa;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ZSOAConfigurator {

    private static String CONFIGURATOR_FILENAME = "zsoa.xml";
    private static Map<String, Object> data;
    private static ZSOAConfigurator INSTANCE;

    private ZSOAConfigurator() {
        data = new HashMap();
        defaults();
        InputStream resource =
                getClass().getClassLoader().getResourceAsStream(CONFIGURATOR_FILENAME);
        if (resource != null) {
            xml(resource);
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

    public final void xml(InputStream is) {
        try {
            //Read file
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("zodiac");
            if (nodeList.getLength() > 1) {
                throw new ConfigurationException("zodiac should appear once.");
            } else if (nodeList.getLength() == 1) {

                Element zodiac = (Element) nodeList.item(0);

                //zodiac soa
                nodeList = zodiac.getElementsByTagName("soa");
                if (nodeList.getLength() > 1) {
                    throw new ConfigurationException("soa should appear once.");
                } else if (nodeList.getLength() == 1) {
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
                                auditorList.add(auditor.getFirstChild().getTextContent());
                            }
                            data.put(AUDITORS, auditorList);
                        }
                    } else if (auditorsCount > 1) {
                        throw new ConfigurationException("auditors should appear once.");
                    }

                    //zodiac soa app-auth 
                    nodeList = soa.getElementsByTagName("app-auth");
                    int countAppAuth = nodeList.getLength();
                    if (countAppAuth == 1) {
                        Element appAuth = (Element) nodeList.item(0);

                        //zodiac soa app-auth only-allowed-application
                        nodeList = appAuth.getElementsByTagName("only-allowed-application");
                        int countOnlyAllowedApp = nodeList.getLength();
                        if (countOnlyAllowedApp == 1) {
                            String only_allowed_application = "false";
                            Node _only_allowed_application = nodeList.item(0);
                            String only_allowed_application_value = _only_allowed_application.getTextContent();
                            if (only_allowed_application_value.equals("true")
                                    || only_allowed_application_value.equals(("false"))) {
                                only_allowed_application = only_allowed_application_value;
                            } else {
                                throw new ConfigurationException("only-allowed-application allowed values true/false.");
                            }
                            data.put(ONLY_ALLOWED_APPLICATION, Boolean.parseBoolean(only_allowed_application));
                        } else if (countOnlyAllowedApp > 1) {
                            throw new ConfigurationException("only-allowed-application should appear once.");
                        }

                        //zodiac soa app-auth count-digits
                        nodeList = appAuth.getElementsByTagName("count-digits");
                        int encryptCountDigitsCount = nodeList.getLength();
                        if (encryptCountDigitsCount == 1) {
                            Node _countDigits = nodeList.item(0);
                            int countDigits = 0;
                            try {
                                countDigits = Integer.valueOf(_countDigits.getFirstChild().getTextContent());
                            } catch (NumberFormatException e) {
                                countDigits = -1;
                            }
                            if (countDigits < 6 || countDigits > 9) {
                                throw new ConfigurationException("count-digits must be a number between 6 and 9");
                            }

                            data.put(ENCRYPT_COUNT_DIGITS, countDigits);
                        } else if (encryptCountDigitsCount > 1) {
                            throw new ConfigurationException("count-digits should appear once.");
                        }

                        //zodiac soa app-auth token-lifetime
                        nodeList = appAuth.getElementsByTagName("token-lifetime");
                        int encryptTokenLifetimeCount = nodeList.getLength();
                        if (encryptTokenLifetimeCount == 1) {
                            Node token_lifetime = nodeList.item(0);
                            int movingFactory = 0;
                            try {
                                movingFactory = Integer.valueOf(token_lifetime.getFirstChild().getTextContent());
                            } catch (NumberFormatException e) {
                                movingFactory = -1;
                            }
                            if (movingFactory < 0) {
                                throw new ConfigurationException("token-lifetime must be a natural number.");
                            }

                            data.put(ENCRYPT_TOKEN_LIFETIME, movingFactory * 1000);
                        } else if (encryptCountDigitsCount > 1) {
                            throw new ConfigurationException("count-digits should appear once.");
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
                                    String application_name = application.getAttribute("name");
                                    String application_password = application.getAttribute("password");

                                    applicationMap.put(application_name, application_password);
                                }
                                data.put(APPLICATIONS, applicationMap);
                            }
                        } else if (applicationsCount > 1) {
                            throw new ConfigurationException("applications should appear once.");
                        }
                    } else if (countAppAuth > 1) {
                        throw new ConfigurationException("app-auth should appear once.");
                    }

                    //zodiac soa servicehandlers
                    nodeList = soa.getElementsByTagName("servicehandlers");
                    int countServiceHandlers = nodeList.getLength();
                    if (countServiceHandlers == 1) {
                        Element serviceHandlers = (Element) nodeList.item(0);

                        nodeList = serviceHandlers.getElementsByTagName("dynamicservicehandler");
                        int countDynamicServiceHandler = nodeList.getLength();
                        if (countDynamicServiceHandler > 0) {
                            for (int i = 0; i < countDynamicServiceHandler; i++) {
                                Element dynamicServiceHandler = (Element) nodeList.item(i);
                                dynamicServiceHandler.setAttribute("impl", "com.zodiac.soa.client.DynamicServiceHandler");
                                document.renameNode(dynamicServiceHandler, null, "servicehandler");
                            }
                        }

                        nodeList = serviceHandlers.getElementsByTagName("servicehandler");
                        int countServiceHandler = nodeList.getLength();
                        if (countServiceHandler > 0) {
                            Map<String, Object> serviceHandlersMap = new HashMap();
                            for (int i = 0; i < countServiceHandler; i++) {
                                Element serviceHandler = (Element) nodeList.item(i);

                                Map<String, String> serviceHandlerMap = new HashMap();
                                String context = serviceHandler.getAttribute("context");
                                String url = serviceHandler.getAttribute("url");
                                String application_name = serviceHandler.getAttribute("application-name");
                                String application_password = serviceHandler.getAttribute("application-password");
                                String impl = serviceHandler.getAttribute("impl");
                                String count_digits = serviceHandler.getAttribute("count-digits");
                                String token_lifetime = serviceHandler.getAttribute("token-lifetime");

                                if (context.length() == 0) {
                                    throw new ConfigurationException("servicehandler should have a context attribute.");
                                }
                                if (url.length() == 0) {
                                    throw new ConfigurationException("servicehandler should have a url attribute ");
                                }
                                if (impl.length() == 0) {
                                    throw new ConfigurationException("servicehandler should have a impl attribute ");
                                }
                                if (context.equals(url)) {
                                    throw new ConfigurationException("context should be different from url ");
                                }
                                if(application_name.length() > 0){
                                    if(application_password.length() == 0
                                            || count_digits.length() == 0
                                            || token_lifetime.length() == 0){
                                        throw new ConfigurationException("when application-name is added should be follow by application-password, count-digits and token-lifetime");
                                    }
                                }
                                if (count_digits.length() > 0) {
                                    try {
                                        int value = Integer.parseInt(count_digits);
                                        if(value < 1){
                                            throw new NumberFormatException();
                                        }
                                    } catch (NumberFormatException ex) {
                                        throw new ConfigurationException("count-digits should be a natural number and greater than zero");
                                    }
                                }
                                if (token_lifetime.length() > 0) {
                                    try {
                                        int value = Integer.parseInt(token_lifetime) * 1000;
                                        if(value < 1){
                                            throw new NumberFormatException();
                                        }
                                        
                                        token_lifetime = String.valueOf(value);
                                    } catch (NumberFormatException ex) {
                                        throw new ConfigurationException("token-lifetime should be a natural number and greater than zero");
                                    }
                                }

                                serviceHandlerMap.put(SERVICEHANDLER_CONTEXT, context);
                                serviceHandlerMap.put(SERVICEHANDLER_URL, url);
                                serviceHandlerMap.put(SERVICEHANDLER_APPLICATION_NAME, application_name);
                                serviceHandlerMap.put(SERVICEHANDLER_APPLICATION_PASSWORD, application_password);
                                serviceHandlerMap.put(SERVICEHANDLER_IMPL, impl);
                                serviceHandlerMap.put(SERVICEHANDLER_COUNT_DIGITS, count_digits);
                                serviceHandlerMap.put(SERVICEHANDLER_TOKEN_LIFETIME, token_lifetime);

                                serviceHandlersMap.put(context, serviceHandlerMap);
                                serviceHandlersMap.put(url, serviceHandlerMap);
                            }
                            data.put(SERVICEHANDLERS, serviceHandlersMap);
                        }
                    } else if (countServiceHandlers > 1) {
                        throw new ConfigurationException("app-auth should appear once.");
                    }
                }

                //zodiac connections
                nodeList = zodiac.getElementsByTagName("connections");
                int countConnections = nodeList.getLength();
                if (countConnections == 1) {
                    Element connections = (Element) nodeList.item(0);

                    nodeList = connections.getElementsByTagName("connection-jdbc");
                    int countConnectionJDBC = nodeList.getLength();
                    if (countConnectionJDBC > 0) {
                        for (int i = 0; i < countConnectionJDBC; i++) {
                            Element connectionJDBC = (Element) nodeList.item(i);
                            connectionJDBC.setAttribute("impl", "com.zodiac.db.ConnectionStringJDBC");
                            document.renameNode(connectionJDBC, null, "connection");
                        }
                    }

                    nodeList = connections.getElementsByTagName("connection-jtds");
                    int countConnectionJTDS = nodeList.getLength();
                    if (countConnectionJTDS > 0) {
                        for (int i = 0; i < countConnectionJTDS; i++) {
                            Element connectionJTDS = (Element) nodeList.item(i);
                            connectionJTDS.setAttribute("impl", "com.zodiac.db.ConnectionStringJTDS");
                            document.renameNode(connectionJTDS, null, "connection");
                        }
                    }

                    //zodiac connections connection
                    nodeList = connections.getElementsByTagName("connection");
                    int countConnection = nodeList.getLength();
                    if (countConnection > 0) {
                        Map<String, Object> connectionsMap = new HashMap();
                        for (int i = 0; i < countConnection; i++) {
                            Element connection = (Element) nodeList.item(i);

                            String context = connection.getAttribute("context");
                            String impl = connection.getAttribute("impl");
                            String host = connection.getAttribute("host");
                            String port = connection.getAttribute("port");
                            String database = connection.getAttribute("database");
                            String schema = connection.getAttribute("schema");
                            String user = connection.getAttribute("user");
                            String password = connection.getAttribute("password");
                            String driver = connection.getAttribute("driver");
                            String dbms = connection.getAttribute("dbms");

                            if (context.length() == 0) {
                                throw new ConfigurationException("connection should have a context attribute.");
                            }
                            if (impl.length() == 0) {
                                throw new ConfigurationException("connection should have a impl attribute.");
                            }
                            if (host.length() == 0) {
                                throw new ConfigurationException("connection should have a host attribute.");
                            }
                            if (database.length() == 0) {
                                throw new ConfigurationException("connection should have a database attribute.");
                            }
                            if (user.length() == 0) {
                                throw new ConfigurationException("connection should have a user attribute.");
                            }
                            if (password.length() == 0) {
                                throw new ConfigurationException("connection should have a password attribute.");
                            }
                            if (driver.length() == 0) {
                                throw new ConfigurationException("connection should have a driver attribute.");
                            }
                            if (dbms.length() == 0) {
                                throw new ConfigurationException("connection should have a dbms attribute.");
                            }

                            Map<String, Object> connectionMap = new HashMap();
                            connectionMap.put(CONNECTION_CONTEXT, context);
                            connectionMap.put(CONNECTION_IMPL, impl);
                            connectionMap.put(CONNECTION_HOST, host);
                            connectionMap.put(CONNECTION_PORT, port);
                            connectionMap.put(CONNECTION_DATABASE, database);
                            connectionMap.put(CONNECTION_SCHEMA, schema);
                            connectionMap.put(CONNECTION_USER, user);
                            connectionMap.put(CONNECTION_PASSWORD, password);
                            connectionMap.put(CONNECTION_DRIVER, driver);
                            connectionMap.put(CONNECTION_DBMS, dbms);

                            //zodiac connections connection arguments
                            nodeList = connection.getElementsByTagName("arguments");
                            int countArguments = nodeList.getLength();
                            if (countArguments == 1) {
                                Element arguments = (Element) nodeList.item(0);

                                nodeList = arguments.getElementsByTagName("argument");
                                int countArgument = nodeList.getLength();
                                if (countArgument > 0) {
                                    Map<String, String> argumentMap = new HashMap();
                                    for (int j = 0; j < countArgument; j++) {
                                        Element argument = (Element) nodeList.item(i);
                                        String key = argument.getAttribute("key");
                                        String value = argument.getFirstChild().getNodeValue();

                                        argumentMap.put(key, value);
                                    }
                                    connectionMap.put(CONNECTION_ARGUMENTS, argumentMap);
                                }
                            } else if (countArguments > 1) {
                                throw new ConfigurationException("arguments should appear once.");
                            }

                            connectionsMap.put(context, connectionMap);
                        }
                        data.put(CONNECTIONS, connectionsMap);
                    }
                } else if (countConnections > 1) {
                    throw new ConfigurationException("connection should appear once.");
                }

                //zodiac dataaccess
                nodeList = zodiac.getElementsByTagName("dataaccess");
                int countDataAccess = nodeList.getLength();
                if (countDataAccess == 1) {
                    Element dataAccess = (Element) nodeList.item(0);

                    //zodiac dataaccess dataaccessobjects
                    nodeList = dataAccess.getElementsByTagName("dataaccessobjects");
                    int countDataAccessObjects = nodeList.getLength();
                    if (countDataAccessObjects > 0) {
                        Element dataAccessObjects = (Element) nodeList.item(0);

                        //zodiac dataaccess dataaccessobjects dataaccessobject
                        nodeList = dataAccessObjects.getElementsByTagName("dataaccessobject");
                        int countDataAccessObject = nodeList.getLength();
                        if (countDataAccessObject > 0) {
                            Map<String, Object> dataaccessMap = new HashMap();
                            for (int i = 0; i < countDataAccessObjects; i++) {
                                Element dataAccessObject = (Element) nodeList.item(i);

                                String _interface = dataAccessObject.getAttribute("interface");
                                String implPkg = dataAccessObject.getAttribute("implpkg");

                                if (_interface.length() == 0) {
                                    throw new ConfigurationException("connection should have a interface attribute.");
                                }

                                dataaccessMap.put(_interface, implPkg);

                                //zodiac dataaccess dataaccessobjects dataaccessobject driver
                                nodeList = dataAccessObject.getElementsByTagName("driver");
                                int countDriver = nodeList.getLength();
                                if (countDriver > 0) {
                                    for (int j = 0; j < countDriver; j++) {
                                        Element driver = (Element) nodeList.item(j);

                                        String name = driver.getAttribute("name");
                                        String impl = driver.getAttribute("impl");

                                        dataaccessMap.put(_interface + "." + name, impl);
                                    }
                                }
                            }

                            data.put(DATAACCESSOBJECTS, dataaccessMap);
                        }

                    } else if (countDataAccessObjects > 1) {
                        throw new ConfigurationException("dataaccessobjects should appear once.");
                    }

                    //zodiac dataaccess contexts
                    nodeList = dataAccess.getElementsByTagName("contexts");
                    int countContexts = nodeList.getLength();
                    if (countContexts == 0) {
                        Element contexts = (Element) nodeList.item(0);

                        //zodiac dataaccess contexts context
                        nodeList = contexts.getElementsByTagName("context");
                        int countContext = nodeList.getLength();
                        if (countContext > 0) {
                            Map<String, Object> _contexts = new HashMap();
                            for (int i = 0; i < countContext; i++) {
                                Map<String, String> _context = new HashMap();
                                Element context = (Element) nodeList.item(i);

                                String name = context.getAttribute("name");
                                String driver = context.getAttribute("driver");
                                String implpkg = context.getAttribute("implpkg");

                                if (name.length() == 0) {
                                    throw new ConfigurationException("context should have a name attribute.");
                                }
                                if (driver.length() == 0) {
                                    throw new ConfigurationException("context should have a driver attribute.");
                                }

                                _context.put(DATAACCESSOBJECT_CONTEXTS_NAME, name);
                                _context.put(DATAACCESSOBJECT_CONTEXTS_IMPLPKG, implpkg);
                                _context.put(DATAACCESSOBJECT_CONTEXTS_DRIVER, driver);
                                _contexts.put(name, _context);
                            }
                            data.put(DATAACCESSOBJECT_CONTEXTS, _contexts);
                        }
                    } else if (countContexts > 1) {
                        throw new ConfigurationException("contexts should appear once.");
                    }
                } else if (countDataAccess > 1) {
                    throw new ConfigurationException("dataaccess should appear once.");
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            throw new ConfigurationException(ex.fillInStackTrace());
        }

    }

    public Object get(String key) {
        return data.get(key);
    }

    public <T> T get(String key, Class<T> type) {
        return (T) get(key);
    }

    public static ZSOAConfigurator getInstance() {
        if (INSTANCE == null) {
            synchronized (ZSOAConfigurator.class) {
                INSTANCE = new ZSOAConfigurator();
            }
        }
        return INSTANCE;
    }
    public static final String AUDITORS = "zodiac.soa.auditors";
    public static final String APPLICATIONS = "zodiac.soa.app-auth..applications";
    public static final String ONLY_ALLOWED_APPLICATION = "zodiac.soa.app-auth.only-allowed-application";
    public static final String ENCRYPT_COUNT_DIGITS = "zodiac.soa.app-auth.count-digits";
    public static final String ENCRYPT_OFFSET = "zodiac.soa.app-auth.offset";
    public static final String ENCRYPT_WITH_CHECKSUM = "zodiac.soa.app-auth.with-checksum";
    public static final String ENCRYPT_TOKEN_LIFETIME = "zodiac.soa.app-auth.token-lifetime";
    public static final String SERVICEHANDLERS = "zodiac.soa.servicehandlers";
    public static final String SERVICEHANDLER_CONTEXT = "zodiac.soa.servicehandlers.servicehandler.context";
    public static final String SERVICEHANDLER_URL = "zodiac.soa.servicehandlers.servicehandler.url";
    public static final String SERVICEHANDLER_APPLICATION_NAME = "zodiac.soa.servicehandlers.servicehandler.application-name";
    public static final String SERVICEHANDLER_APPLICATION_PASSWORD = "zodiac.soa.servicehandlers.servicehandler.application-password";
    public static final String SERVICEHANDLER_IMPL = "zodiac.soa.servicehandlers.servicehandler.impl";
    public static final String SERVICEHANDLER_COUNT_DIGITS = "zodiac.soa.servicehandlers.servicehandler.count-digits";
    public static final String SERVICEHANDLER_TOKEN_LIFETIME = "zodiac.soa.servicehandlers.servicehandler.token-lifetime";
    public static final String CONNECTIONS = "zodiac.connections";
    public static final String CONNECTION_CONTEXT = "zodiac.connections.connection.context";
    public static final String CONNECTION_IMPL = "zodiac.connections.connections.impl";
    public static final String CONNECTION_HOST = "zodiac.connections.connection.host";
    public static final String CONNECTION_PORT = "zodiac.connections.connection.port";
    public static final String CONNECTION_DATABASE = "zodiac.connections.connection.database";
    public static final String CONNECTION_SCHEMA = "zodiac.connections.connection.schema";
    public static final String CONNECTION_USER = "zodiac.connections.connection.user";
    public static final String CONNECTION_PASSWORD = "zodiac.connections.connection.password";
    public static final String CONNECTION_DRIVER = "zodiac.connections.connection.driver";
    public static final String CONNECTION_DBMS = "zodiac.connections.connection.dbms";
    public static final String CONNECTION_ARGUMENTS = "zodiac.connections.connection.arguments";
    public static final String DATAACCESSOBJECTS = "zodiac.dataaccess.dataaccessobjects.dataaccessobject";
    public static final String DATAACCESSOBJECT_CONTEXTS = "zodiac.dataaccess.contexts";
    public static final String DATAACCESSOBJECT_CONTEXTS_NAME = "zodiac.dataaccess.contexts.context.name";
    public static final String DATAACCESSOBJECT_CONTEXTS_IMPLPKG = "zodiac.dataaccess.contexts.context.implpkg";
    public static final String DATAACCESSOBJECT_CONTEXTS_DRIVER = "zodiac.dataaccess.contexts.context.driver";
}
