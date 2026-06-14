package com.sshubuntu.weblab3.mbean;

import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.concurrent.atomic.AtomicLong;

public class PointStatistics extends NotificationBroadcasterSupport implements PointStatisticsMBean {

    public static final String TWO_MISSES_NOTIFICATION = "com.sshubuntu.weblab3.point.twoMissesInRow";

    private final MonitoringState state;
    private final AtomicLong sequence = new AtomicLong();

    PointStatistics(MonitoringState state) {
        this.state = state;
    }

    void registerPoint(boolean hit) {
        MonitoringState.Snapshot snapshot = state.registerPoint(hit);
        if (snapshot.secondMissInRow()) {
            Notification notification = new Notification(
                    TWO_MISSES_NOTIFICATION,
                    this,
                    sequence.incrementAndGet(),
                    System.currentTimeMillis(),
                    "User made two misses in a row. Total points: " + snapshot.totalPoints()
            );
            notification.setUserData("total=" + snapshot.totalPoints()
                    + ", hits=" + snapshot.hitPoints()
                    + ", misses=" + snapshot.missPoints());
            sendNotification(notification);
        }
    }

    @Override
    public long getTotalPoints() {
        return state.snapshot().totalPoints();
    }

    @Override
    public long getHitPoints() {
        return state.snapshot().hitPoints();
    }

    @Override
    public long getMissPoints() {
        return state.snapshot().missPoints();
    }

    @Override
    public int getConsecutiveMisses() {
        return state.snapshot().consecutiveMisses();
    }

    @Override
    public void reset() {
        state.reset();
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = {TWO_MISSES_NOTIFICATION};
        String name = Notification.class.getName();
        String description = "Notification sent when the user makes two misses in a row";
        return new MBeanNotificationInfo[]{new MBeanNotificationInfo(types, name, description)};
    }
}
