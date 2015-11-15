package com.tuotiansudai.util.quartz;

import com.mchange.v2.c3p0.PooledDataSource;
import org.quartz.utils.ConnectionProvider;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringDataSourceConnectionProvider implements ConnectionProvider {

    private DataSource dataSource;

    public SpringDataSourceConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void shutdown() throws SQLException {
        if (dataSource instanceof Closeable) {
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dataSource instanceof PooledDataSource) {
            ((PooledDataSource) dataSource).close();
        }
    }

    @Override
    public void initialize() throws SQLException {
    }
}
