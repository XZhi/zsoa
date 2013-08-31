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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class RequestTest {
    
    public RequestTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setTextModeException method, of class Request.
     */
    @Test
    public void testIsTextModeException() {
        System.out.println("setTextModeException");
        boolean textModeException = true;
        Request instance = new Request();
        instance.setTextModeException(textModeException);
        assertEquals(instance.isTextModeException(), textModeException);
    }

        /**
     * Test of getArgumentsMethod method, of class Request.
     */
    @Test
    public void testSetsGets() {
        System.out.println("SetsGets");
        
        String clazz = "com.zodiac.zsoa";
        Class[] paramsConst = new Class[] { int.class, boolean.class };
        Object[] argsConst = new Object[]{ 1, true };
        String method = "method";
        Class[] paramsMethod = new Class[] { String.class };
        Object[] argsMethod = new Object[] { "param" };
        
        Request instance = new Request(
                clazz,
                paramsConst,
                argsConst,
                method,
                paramsMethod,
                argsMethod);
        
        assertEquals(instance.getClazz(), clazz);
        assertArrayEquals(instance.getParametersConstructor(), paramsConst);
        assertArrayEquals(instance.getArgumentsConstructor(), argsConst);
        assertEquals(instance.getMethod(), method);
        assertArrayEquals(instance.getParametersMethod(), paramsMethod);
        assertArrayEquals(instance.getArgumentsMethod(), argsMethod);
    }

}