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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("Starting set of test...");

        System.out.println("Tests (NoDB,NoXMLConf)");

        System.out.println("Connecting to URL Context...");

        System.out.println("Tests(NoDB,WithXMLConf)");
        DynamicServiceHandler dsh = ServiceHandlerFactory.getServiceHandler("default");

        System.out.println("Public test:");
        PublicManagerTest.test(dsh);
        System.out.println("Session test:");
        SessionManagerTest.test(dsh);
        System.out.println("Private test:");
        PrivateManagerTest.test(dsh);
    }
}
