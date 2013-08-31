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

import com.thoughtworks.xstream.XStream;

/**
 * This class is a skeletal to the implementation of DTO pattern.
 * 
 * In this package the pogrammer can find also the DAO pattern,
 * and as it is recomended use both to separate completly the
 * data access tier from logic tier.
 * 
 * This class provide the methods to serializce from object to
 * XML and from XML to object.
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public abstract class AbstractDTO implements DTO {
 
    /**
     * {@inheritDoc}
     */
    @Override
    public void fromXML(String xml) {
        XStream xstream = new XStream();
        xstream.fromXML(xml, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toXML() {
        XStream xstream = new XStream();
        return xstream.toXML(this);
    }
    
    
}
