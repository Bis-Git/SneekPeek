/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javax.sql.DataSource;

/**
 *
 * @author bisiv
 */
public class DataSourceSingleton {
    private static final String PASSWORD = "SQL";
    private static final String USER = "sa";
    private static final String DATABASE_NAME = "SneakPeekDatabase";
    private static final String SERVER_NAME = "localhost";

    private static DataSource instance;

    private static DataSource createInstance() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName(SERVER_NAME);
        dataSource.setDatabaseName(DATABASE_NAME);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);
        
        return dataSource;
    }
    
    private DataSourceSingleton() {}
    
    public static DataSource getInstance(){
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }
}
