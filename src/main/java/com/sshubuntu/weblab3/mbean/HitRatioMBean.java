package com.sshubuntu.weblab3.mbean;

public interface HitRatioMBean {

    long getTotalPoints();

    long getHitPoints();

    double getHitPercentage();

    String getHitPercentageText();

    void reset();
}
