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
package com.zodiac.security;

import java.util.Calendar;
import java.util.TimeZone;
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
public class OneTimePasswordTest {
    
    public OneTimePasswordTest() {
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
     * Test of generateOTP method, of class OneTimePassword.
     */
    @Test
    public void testGenerateOTP() throws Exception {
        System.out.println("generateOTP (this test will take about 11s)");
        
        int T0 = 0;
        int X = 6000;
        
        byte[] secret = "pwd".getBytes();
        int codeDigits = 6;
        boolean addChecksum = true;
        int truncationOffset = 3;
        long movingFactor;
                
        movingFactor = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() - T0) / X;
        String expResult = OneTimePassword.generateOTP(secret, movingFactor, codeDigits, addChecksum, truncationOffset);
        Thread.sleep(1000);
        movingFactor = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() - T0) / X;
        String result = OneTimePassword.generateOTP(secret, movingFactor, codeDigits, addChecksum, truncationOffset);
        assertEquals(expResult, result);
        
        Thread.sleep(10000);
        movingFactor = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() - T0) / X;
        String result2 = OneTimePassword.generateOTP(secret, movingFactor, codeDigits, addChecksum, truncationOffset);
        assertNotEquals(expResult, result2);
    }
}