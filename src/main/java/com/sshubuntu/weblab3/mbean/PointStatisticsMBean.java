package com.sshubuntu.weblab3.mbean;

public interface PointStatisticsMBean {

    long getTotalPoints();

    long getHitPoints();

    long getMissPoints();

    int getConsecutiveMisses();

    void reset();
}
