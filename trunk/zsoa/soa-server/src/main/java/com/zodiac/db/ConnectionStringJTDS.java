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

package com.zodiac.db;

import java.util.Iterator;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
public class ConnectionStringJTDS  extends AbstractConnectionString {

    public ConnectionStringJTDS(ConnectionParameter connectionParameter){
        super(connectionParameter);
    }
    /**
     * This method return a connection string in jdbc jtds format for the specified
     * dbms and all connection parameters.
     * 
     * @return a string connection
     */
    @Override
    public String getConnectionString() {
        String connectionString = "jdbc:jtds:" + getConnectionParameter().getDBMS().toLowerCase() +
                "://" + getConnectionParameter().getHost() +
                ":" + getConnectionParameter().getPort() +
                "/" + getConnectionParameter().getDatabase();
        
        String args = null;
        Iterator<String> argsIterator = getConnectionParameter().getArgs().iterator();
        if(argsIterator.hasNext()){
            args = ";";
            do {
                if(args.length() > 1){
                    args += ";";
                }
                
                args += getConnectionParameter().getArg(argsIterator.next());
            }while(argsIterator.hasNext());
        }
        
        if(args != null){
            connectionString = connectionString + args;
        }
        return connectionString;
    }

}
