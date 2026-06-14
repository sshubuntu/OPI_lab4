package com.sshubuntu.weblab3.mbean;

final class MonitoringState {

    private long totalPoints;
    private long hitPoints;
    private long missPoints;
    private int consecutiveMisses;

    synchronized Snapshot registerPoint(boolean hit) {
        totalPoints++;
        if (hit) {
            hitPoints++;
            consecutiveMisses = 0;
        } else {
            missPoints++;
            consecutiveMisses++;
        }

        return snapshot(!hit && consecutiveMisses == 2);
    }

    synchronized Snapshot snapshot() {
        return snapshot(false);
    }

    synchronized void reset() {
        totalPoints = 0;
        hitPoints = 0;
        missPoints = 0;
        consecutiveMisses = 0;
    }

    private Snapshot snapshot(boolean secondMissInRow) {
        return new Snapshot(totalPoints, hitPoints, missPoints, consecutiveMisses, secondMissInRow);
    }

    record Snapshot(long totalPoints, long hitPoints, long missPoints, int consecutiveMisses, boolean secondMissInRow) {

        double hitPercentage() {
            if (totalPoints == 0) {
                return 0.0;
            }
            return hitPoints * 100.0 / totalPoints;
        }
    }
}
