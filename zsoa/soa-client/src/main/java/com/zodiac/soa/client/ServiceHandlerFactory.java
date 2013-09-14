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

import com.zodiac.soa.RequestException;
import com.zodiac.soa.ZSOAConfigurator;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class ServiceHandlerFactory {

    private static Map<String, DynamicServiceHandler> serviceByContext;
    private static Map<String, DynamicServiceHandler> serviceByURL;

    static {
        serviceByContext = new HashMap();
        serviceByURL = new HashMap();
    }

    public static <T extends DynamicServiceHandler> T getServiceHandlerByURL(String url) {
        return (T) serviceByURL.get(url);
    }

    public static <T extends DynamicServiceHandler> T getServiceHandler(String context) {
        try {
            T t;
            t = (T) serviceByContext.get(context);
            if (t != null) {
                return t;
            }

            Map<String, String> serviceHandler = getServiceHandlerMap(context);

            if (serviceHandler == null) {
                throw new RequestException("could not found context service handler");
            }

            String url = serviceHandler.get(ZSOAConfigurator.SERVICEHANDLER_URL);
            String impl = serviceHandler.get(ZSOAConfigurator.SERVICEHANDLER_IMPL);
            String appName = serviceHandler.get(ZSOAConfigurator.SERVICEHANDLER_APPLICATION_NAME);
            

            Class componentType = Class.forName(impl);

            if (appName == null) {
                t = (T) componentType.getConstructor(URL.class).newInstance(new URL(url));
            } else {
                String appPass = serviceHandler.get(ZSOAConfigurator.SERVICEHANDLER_APPLICATION_PASSWORD);
                String countDigs = serviceHandler.get(ZSOAConfigurator.SERVICEHANDLER_COUNT_DIGITS);
                String tokenLT = serviceHandler.get(ZSOAConfigurator.SERVICEHANDLER_TOKEN_LIFETIME);
                t = (T) componentType.getConstructor(URL.class, String.class, String.class, int.class, int.class)
                        .newInstance(new URL(url), appName, appPass, Integer.parseInt(countDigs), Integer.parseInt(tokenLT));
            }
            serviceByContext.put(context, t);
            serviceByURL.put(url, t);
            return t;
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException |
                InstantiationException | IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | MalformedURLException ex) {
            throw new RequestException(ex);
        }
    }

    private static Map<String, Object> getServiceHandlerMap() {
        Map<String, Object> serviceHandlers =
                ZSOAConfigurator.getInstance().get(ZSOAConfigurator.SERVICEHANDLERS, Map.class);
        return serviceHandlers;
    }

    private static Map<String, String> getServiceHandlerMap(String cotext) {
        Map<String, Object> serviceHandlers = getServiceHandlerMap();

        if (serviceHandlers == null) {
            return null;
        }

        return (Map<String, String>) serviceHandlers.get(cotext);
    }
}
