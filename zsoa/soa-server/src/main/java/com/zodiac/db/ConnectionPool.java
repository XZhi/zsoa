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

import com.zodiac.soa.ConnectionException;
import com.zodiac.soa.ZSOAConfigurator;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class ConnectionPool {

    private static Map<String, Connection> connections;

    public static Connection getConnection(String context) throws SQLException {
        Connection _connection = ConnectionPool.connections.get(context);
        if(_connection != null){
            return _connection;
        }
        
        Map<String, Object> connections =
                ZSOAConfigurator.getInstance().get(ZSOAConfigurator.CONNECTIONS, Map.class);
        Map<String, Object> connection = (Map) connections.get(context);

        String host = (String) connection.get(ZSOAConfigurator.CONNECTION_HOST);
        String port = (String) connection.get(ZSOAConfigurator.CONNECTION_PORT);
        String database = (String) connection.get(ZSOAConfigurator.CONNECTION_DATABASE);
        String user = (String) connection.get(ZSOAConfigurator.CONNECTION_USER);
        String password = (String) connection.get(ZSOAConfigurator.CONNECTION_PASSWORD);
        String driver = (String) connection.get(ZSOAConfigurator.CONNECTION_DRIVER);
        String dbms = (String) connection.get(ZSOAConfigurator.CONNECTION_DBMS);
        Map<String, String> argsMap = (Map) connection.get(ZSOAConfigurator.CONNECTION_ARGUMENTS);

        ConnectionParameter connectionParameter =
                new ConnectionParameter(host, port, database, user, password, driver, dbms);
        
        if (argsMap.size() > 0) {
            for (String arg : argsMap.keySet()) {
                connectionParameter.addArgs(arg, argsMap.get(arg));
            }
        }
        
        String impl = (String)connection.get(ZSOAConfigurator.CONNECTION_IMPL);
        try {
            Class connectionClass = Class.forName(impl);
            ConnectionString connectionString =
                    (ConnectionString)connectionClass
                        .getConstructor(ConnectionParameter.class).newInstance(connectionParameter);
            
            _connection = ConnectionFactory.getConnection(connectionParameter, connectionString);
            ConnectionPool.connections.put(context, _connection);
            
            return _connection;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
                IllegalArgumentException | InvocationTargetException | NoSuchMethodException | 
                SecurityException ex) {
            throw new ConnectionException("could not instance " + impl, ex);
        }
    }
}
