package com.tuotiansudai.util.quartz;

import org.quartz.impl.jdbcjobstore.InvalidConfigurationException;
import org.quartz.impl.jdbcjobstore.JobStoreTX;
import org.quartz.spi.JobStore;
import org.quartz.utils.DBConnectionManager;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;

public class JobStoreBuilder implements InitializingBean {

    private static final String Default_Data_Source_Name = "DefaultJobDataSourceName";

    private static final String Job_Store_Driver_Delegate_Class = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";

    private static final String Job_Store_Table_Prefix = "QRTZ_";

    private DataSource dataSource;

    public JobStore buildJdbcJobStore(String schedulerName) {
        return buildJdbcJobStore(schedulerName, 60000, 20, true, 15000);
    }

    public JobStore buildJdbcJobStore(String schedulerName,
                                      long misfireThreshold,
                                      int maxMisfiresToHandleAtATime,
                                      boolean isClustered,
                                      long clusterCheckinInterval) {
        JobStoreTX jdbcJobStore = new JobStoreTX();
        jdbcJobStore.setDataSource(Default_Data_Source_Name);
        jdbcJobStore.setTablePrefix(Job_Store_Table_Prefix);
        jdbcJobStore.setInstanceId(schedulerName);
        jdbcJobStore.setMisfireThreshold(misfireThreshold);
        jdbcJobStore.setMaxMisfiresToHandleAtATime(maxMisfiresToHandleAtATime);
        jdbcJobStore.setIsClustered(isClustered);
        jdbcJobStore.setClusterCheckinInterval(clusterCheckinInterval);
        try {
            jdbcJobStore.setDriverDelegateClass(Job_Store_Driver_Delegate_Class);
        } catch (InvalidConfigurationException e) {
        }
        return jdbcJobStore;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DBConnectionManager.getInstance().addConnectionProvider(Default_Data_Source_Name, new SpringDataSourceConnectionProvider(dataSource));
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
