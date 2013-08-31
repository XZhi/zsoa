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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author brian
 */
public class PasswordRuleSuite implements PasswordRule {
    
    private List<PasswordRule> rules;

    public PasswordRuleSuite() {
        rules = new ArrayList();
    }
    
    public boolean add(PasswordRule passwordRule) {
        return this.rules.add(passwordRule);
    }

    @Override
    public boolean compliant(Password password, Object ... args) {
        Iterator<PasswordRule> rules = this.rules.iterator();
        
        while(rules.hasNext()){
            PasswordRule rule = rules.next();
            if(!rule.compliant(password, args)) {
                return false;
            }
        }
        return true;
    }
        
}
