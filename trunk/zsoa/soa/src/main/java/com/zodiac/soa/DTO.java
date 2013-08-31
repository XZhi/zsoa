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
package com.zodiac.soa;

import java.io.Serializable;


/**
 * Interface for DTO pattern serializing from and to xml.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public interface DTO extends Serializable {
    
    /**
     * Fill the object with the information of a xml string.
     * 
     * @param xml a xml
     */
    public void fromXML(String xml);
    
    /**
     * This method transform the object to xml.
     * 
     * @return a xml
     */
    public String toXML();
    
    
}
