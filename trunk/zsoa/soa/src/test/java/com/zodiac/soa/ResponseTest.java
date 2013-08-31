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
public class ResponseTest {
    
    public ResponseTest() {
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
     * Test of getException method, of class Response.
     */
    @Test
    public void testSetsGets() {
        System.out.println("SetsGets");
        
        Exception exception = new Exception();
        Object result = 1;
        String textException = "exception";
        
        Response instance1 = new Response(exception);
        Response instance2 = new Response(result);
        Response instance3 = new Response(textException);
        
        assertEquals(instance1.getException(), exception);
        assertEquals(instance2.getResult(), result);
        assertEquals(instance3.getTextException(), textException);
    }

}