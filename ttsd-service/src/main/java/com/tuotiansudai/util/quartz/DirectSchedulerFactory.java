package com.tuotiansudai.util.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.core.JobRunShellFactory;
import org.quartz.core.QuartzScheduler;
import org.quartz.core.QuartzSchedulerResources;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdJobRunShellFactory;
import org.quartz.impl.StdScheduler;
import org.quartz.simpl.CascadingClassLoadHelper;
import org.quartz.spi.*;

import java.util.Iterator;
import java.util.Map;

public class DirectSchedulerFactory extends org.quartz.impl.DirectSchedulerFactory {

    private static DirectSchedulerFactory instance = new DirectSchedulerFactory();

    protected DirectSchedulerFactory() {
        super();
    }

    public static DirectSchedulerFactory getInstance() {
        return instance;
    }

    public void createScheduler(String schedulerName,
                                String schedulerInstanceId, ThreadPool threadPool,
                                ThreadExecutor threadExecutor,
                                JobStore jobStore, Map<String, SchedulerPlugin> schedulerPluginMap,
                                String rmiRegistryHost, int rmiRegistryPort,
                                long idleWaitTime, long dbFailureRetryInterval,
                                boolean jmxExport, String jmxObjectName, int maxBatchSize, long batchTimeWindow)
            throws SchedulerException {

        JobRunShellFactory jrsf = new StdJobRunShellFactory();

        threadPool.initialize();

        QuartzSchedulerResources qrs = new QuartzSchedulerResources();

        qrs.setName(schedulerName);
        qrs.setInstanceId(schedulerInstanceId);
        // modify here by zzg 20151023
        threadPool.setInstanceId(schedulerInstanceId);
        threadPool.setInstanceName(schedulerName);
        qrs.setMakeSchedulerThreadDaemon(true);
        // modify end
        qrs.setJobRunShellFactory(jrsf);
        qrs.setThreadPool(threadPool);
        qrs.setThreadExecutor(threadExecutor);
        qrs.setJobStore(jobStore);
        qrs.setMaxBatchSize(maxBatchSize);
        qrs.setBatchTimeWindow(batchTimeWindow);
        qrs.setRMIRegistryHost(rmiRegistryHost);
        qrs.setRMIRegistryPort(rmiRegistryPort);
        qrs.setJMXExport(jmxExport);
        if (jmxObjectName != null) {
            qrs.setJMXObjectName(jmxObjectName);
        }

        // add plugins
        if (schedulerPluginMap != null) {
            for (Iterator<SchedulerPlugin> pluginIter = schedulerPluginMap.values().iterator(); pluginIter.hasNext(); ) {
                qrs.addSchedulerPlugin(pluginIter.next());
            }
        }

        QuartzScheduler qs = new QuartzScheduler(qrs, idleWaitTime, dbFailureRetryInterval);

        ClassLoadHelper cch = new CascadingClassLoadHelper();
        cch.initialize();

        // modify here by zzg 20151023
        jobStore.setInstanceId(schedulerInstanceId);
        jobStore.setInstanceName(schedulerName);
        // modify end

        jobStore.initialize(cch, qs.getSchedulerSignaler());

        Scheduler scheduler = new StdScheduler(qs);

        jrsf.initialize(scheduler);

        qs.initialize();


        // Initialize plugins now that we have a Scheduler instance.
        if (schedulerPluginMap != null) {
            for (Iterator<Map.Entry<String, SchedulerPlugin>> pluginEntryIter = schedulerPluginMap.entrySet().iterator(); pluginEntryIter.hasNext(); ) {
                Map.Entry<String, SchedulerPlugin> pluginEntry = pluginEntryIter.next();

                pluginEntry.getValue().initialize(pluginEntry.getKey(), scheduler, cch);
            }
        }

        getLog().info("Quartz scheduler " + scheduler.getSchedulerName());

        getLog().info("Quartz scheduler version: " + qs.getVersion());

        SchedulerRepository schedRep = SchedulerRepository.getInstance();

        qs.addNoGCObject(schedRep); // prevents the repository from being
        // garbage collected

        schedRep.bind(scheduler);
    }
}
