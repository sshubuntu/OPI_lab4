package com.sshubuntu.weblab3.mbean;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public final class MonitoringRegistry {

    public static final String POINT_STATISTICS_OBJECT_NAME = "com.sshubuntu.weblab3:type=PointStatistics";
    public static final String HIT_RATIO_OBJECT_NAME = "com.sshubuntu.weblab3:type=HitRatio";

    private static final MonitoringState STATE = new MonitoringState();
    private static final PointStatistics POINT_STATISTICS = new PointStatistics(STATE);
    private static final HitRatio HIT_RATIO = new HitRatio(STATE);

    private MonitoringRegistry() {
    }

    public static synchronized void registerMBeans() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            register(server, new ObjectName(POINT_STATISTICS_OBJECT_NAME), POINT_STATISTICS);
            register(server, new ObjectName(HIT_RATIO_OBJECT_NAME), HIT_RATIO);
        } catch (JMException e) {
            throw new IllegalStateException("Unable to register web lab MBeans", e);
        }
    }

    public static synchronized void unregisterMBeans() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        unregister(server, POINT_STATISTICS_OBJECT_NAME);
        unregister(server, HIT_RATIO_OBJECT_NAME);
    }

    public static void registerPoint(boolean hit) {
        POINT_STATISTICS.registerPoint(hit);
    }

    private static void register(MBeanServer server, ObjectName objectName, Object mbean) throws JMException {
        if (server.isRegistered(objectName)) {
            server.unregisterMBean(objectName);
        }
        server.registerMBean(mbean, objectName);
    }

    private static void unregister(MBeanServer server, String objectName) {
        try {
            ObjectName name = new ObjectName(objectName);
            if (server.isRegistered(name)) {
                server.unregisterMBean(name);
            }
        } catch (JMException ignored) {
            // Application shutdown must not be blocked by JMX cleanup.
        }
    }
}
