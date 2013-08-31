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
package com.zodiac.security.authenticate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 *
 * @author brian
 */
public class PasswordDigest extends AbstractPassword {

    private int iterations;
    
    private String algorithm;
    
    public PasswordDigest(String password) {
        this(password, PasswordDigest.salt(32, PasswordDigest.SALT_SHA1PRNG));
    }
    
    public PasswordDigest(String password, byte[] salt){
        this(password, salt, 50, PasswordDigest.HASH_SHA256);
    }
    
    public PasswordDigest(String password, byte[] salt, int iterations, String algorithm){
        super(password, salt);
        this.iterations = iterations;
        this.algorithm = algorithm;
    }
    
    @Override
    public byte[] getHash(String password, byte[] salt) {
        if(password == null){
            throw new NullPointerException("Password cannot be null.");
        } else if(password.equals("")) {
            throw new NullPointerException("Password cannot be empty.");
        }
        if(salt == null){
            throw new NullPointerException("Salt cannot be null.");
        } else if (salt.length == 0) {
            throw new NullPointerException("Salt cannot be empty.");
        }
        
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.reset();
            digest.update(salt);
            byte[] pass = digest.digest(password.getBytes());
            for(int i = 1; i < getIterations(); i++){
                digest.reset();
                pass = digest.digest(pass);
            }
            return pass;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordDigest.class.getName()).log(Level.FATAL, null, ex);
            throw new PasswordException(ex);
        }
    }

    @Override
    public int getIterations() {
        return this.iterations;
    }
    
    public static byte[] salt(int length, String algorithm) {
        try {
            if(length < 1){
                throw new IllegalArgumentException("Lenght should be equal or greater than one (1).");
            }
            
            byte[] salt = new byte[length];
            Random random = SecureRandom.getInstance(algorithm);
            random.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordDigest.class.getName()).log(Level.FATAL, null, ex);
            throw new PasswordException(ex);
        }
    }
    
    public static final String SALT_SHA1PRNG = "SHA1PRNG";
    
    public static final String HASH_MD5 = "MD5";
    
    public static final String HASH_SHA256 = "SHA-256";
    
    public static final String HASH_SHA384 = "SHA-384";
    
    public static final String HASH_SHA512 = "SHA-512";
        
}
