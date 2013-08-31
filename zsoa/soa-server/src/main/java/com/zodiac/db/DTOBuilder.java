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
package com.zodiac.db;

import com.zodiac.soa.DTO;
import com.zodiac.soa.DTOMap;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class DTOBuilder {
    
    public static <T extends DTO>T fromResultSet(
            Class<T> componentType, ResultSet resultSet)
            throws InstantiationException, IllegalAccessException, DataAccessException {
        return fromResultSet(componentType, resultSet, false, null);
    }
    
    private static <T extends DTO>T fromResultSet(
            Class<T> componentType, ResultSet resultSet, boolean forceColumnOptional,
            String prefix) 
            throws InstantiationException, IllegalAccessException, DataAccessException {
        
        DTO dto = componentType.newInstance();
        
        Field[] fields = componentType.getFields();
        for(Field field : fields){
            if(!(DTO.class.isAssignableFrom(componentType) ||
                    field.getType().equals(String.class) || 
                    field.getType().isPrimitive())){
                throw new IllegalArgumentException(
                        "Fields (" + field.getName() + ") type should be primitive or String.");
            }
            
            DTOMap dtoMap = field.getAnnotation(DTOMap.class);
            if(dtoMap != null){
                boolean isOptional = dtoMap.isOptional();
                String reference;
                if(prefix == null){
                    reference = dtoMap.reference();
                } else {
                    reference = prefix + "_" + dtoMap.reference();
                }
                
                field.setAccessible(true);
                try{
                    if(DTO.class.isAssignableFrom(field.getType())){
                        DTO attribute = 
                                fromResultSet(
                                    (Class<T>)field.getType(),
                                    resultSet,
                                    isOptional,
                                    dtoMap.prefix());
                        field.set(
                                componentType.cast(dto), 
                                attribute);
                    } else {
                        field.set(componentType.cast(dto), 
                            resultSet.getObject(reference));
                    }
                }catch(SQLException e){
                    boolean optional = forceColumnOptional || (!forceColumnOptional && isOptional);
                    if(optional){
                        throw new DataAccessException(e);
                    }
                }
            }
        }
        return (T)dto;
    }
    
}
