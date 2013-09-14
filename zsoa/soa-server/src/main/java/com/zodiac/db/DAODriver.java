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

import com.zodiac.soa.ZSOAConfigurator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class DAODriver {

    private static Map<String, DAO> drivers;

    static {
        drivers = new HashMap();
    }

    protected DAODriver() {
    }

    public static <T extends DAO> T getDAODriver(String context, Class<T> daoClass) {
        Map<String, Object> contexts =
                ZSOAConfigurator.getInstance().get(ZSOAConfigurator.DATAACCESSOBJECT_CONTEXTS, Map.class);
        Map<String, Object> _context = (Map<String, Object>) contexts.get(context);

        String driver = (String) _context.get(ZSOAConfigurator.DATAACCESSOBJECT_CONTEXTS_DRIVER);

        Map<String, Object> daos =
                ZSOAConfigurator.getInstance().get(ZSOAConfigurator.DATAACCESSOBJECTS, Map.class);
        String impl = (String) daos.get(daoClass.getName() + "." + driver);

        if (impl == null || impl.length() == 0) {
            String implpkg = (String) daos.get(daoClass.getName());
            if (implpkg != null && implpkg.length() > 0) {
                impl = implpkg + "." + daoClass.getCanonicalName() + driver;
            }
        }

        if(impl == null || impl.length() == 0){
            String implpkg = (String)_context.get(ZSOAConfigurator.DATAACCESSOBJECT_CONTEXTS_IMPLPKG);
            if(implpkg != null && implpkg.length() > 0){
                impl = implpkg + "." + daoClass.getCanonicalName() + driver;
            }
        }
        
        if(impl == null || impl.length() == 0){
            DAOPackage daoPackage = daoClass.getAnnotation(DAOPackage.class);
            if(daoPackage != null && daoPackage.pkg().length() > 0){
                String implpkg = daoPackage.pkg();
                impl = implpkg + "." + daoClass.getCanonicalName() + driver;
            }
        }
        
        if(impl == null || impl.length() == 0){
            throw new DAOException("could not found an implementation");
        }
        try {
            DAO dao = drivers.get(impl);
            if(dao != null){
                return (T)dao;
            }
            
            Class implClass = Class.forName(impl);
            dao = (DAO)implClass.newInstance();
            
            drivers.put(impl, dao);
            
            return (T)dao;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new DAOException("unable to instance " + impl, ex);
        }
        
    }
}
