package com.sshubuntu.weblab3.mbean;

import java.util.Locale;

public class HitRatio implements HitRatioMBean {

    private final MonitoringState state;

    HitRatio(MonitoringState state) {
        this.state = state;
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
    public double getHitPercentage() {
        return state.snapshot().hitPercentage();
    }

    @Override
    public String getHitPercentageText() {
        return String.format(Locale.US, "%.2f%%", getHitPercentage());
    }

    @Override
    public void reset() {
        state.reset();
    }
}
