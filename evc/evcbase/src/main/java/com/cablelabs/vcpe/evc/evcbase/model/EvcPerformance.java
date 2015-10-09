/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.evc.evcbase.model;

import com.cablelabs.vcpe.common.Dbg;

/**
 * Created by steve on 6/9/15.
 */
public class EvcPerformance {

    private String cosId;                               // associated CoS
    private long   oneWayFrameDelay;                    // milliseconds
    private long   oneWayFrameDelayRange;               // milliseconds
    private long   oneWayMeanFrameDelay;                // milliseconds
    private long   oneWayInterFrameDelayVariation;      // milliseconds
    private double oneWayFrameLossRatio;                // percentage
    private double oneWayAvailability;                  // percentage
    private long   oneWayResilHighLossIntervals;        //count
    private long   oneWayResilConsecHighLossIntervals;  //count

    // zero argument constructor required for JAX-RS
    public EvcPerformance() {
        oneWayFrameDelay = -1;
        oneWayFrameDelayRange = -1;
        oneWayMeanFrameDelay = -1;
        oneWayInterFrameDelayVariation = -1;
        oneWayFrameLossRatio = -1.0;
        oneWayAvailability = -1.0;
        oneWayResilHighLossIntervals = -1;
        oneWayResilConsecHighLossIntervals = -1;
    }

    public void setAllProps (String cosId, long oneWayFrameDelay, long oneWayFrameDelayRange,
                             long oneWayMeanFrameDelay, long oneWayInterFrameDelayVariation,
                             double oneWayFrameLossRatio, double oneWayAvailability,
                             long oneWayResilHighLossIntervals, long oneWayResilConsecHighLossIntervals)
    {
        this.cosId = cosId;
        this.oneWayFrameDelay = oneWayFrameDelay;
        this.oneWayFrameDelayRange = oneWayFrameDelayRange;
        this.oneWayMeanFrameDelay = oneWayMeanFrameDelay;
        this.oneWayInterFrameDelayVariation = oneWayInterFrameDelayVariation;
        this.oneWayFrameLossRatio = oneWayFrameLossRatio;
        this.oneWayAvailability = oneWayAvailability;
        this.oneWayResilHighLossIntervals = oneWayResilHighLossIntervals;
        this.oneWayResilConsecHighLossIntervals = oneWayResilConsecHighLossIntervals;
    }


    // util fxns

    public void dump() { dump(0); }
    public void dump(int tab) {
        Dbg.p(tab, "cosId:   " + this.cosId);
        Dbg.p(tab, "oneWayFrameDelay:      " + this.oneWayFrameDelay);
        Dbg.p(tab, "oneWayFrameDelayRange: " + this.oneWayFrameDelayRange);
        Dbg.p(tab, "oneWayMeanFrameDelay:  " + this.oneWayMeanFrameDelay);
        Dbg.p(tab, "oneWayFrameLossRatio: " + this.oneWayFrameLossRatio);
        Dbg.p(tab, "oneWayAvailability:   " + this.oneWayAvailability);
        Dbg.p(tab, "oneWayInterFrameDelayVariation:     " + this.oneWayInterFrameDelayVariation);
        Dbg.p(tab, "oneWayResilHighLossIntervals:       " + this.oneWayResilHighLossIntervals);
        Dbg.p(tab, "oneWayResilConsecHighLossIntervals: " + this.oneWayResilConsecHighLossIntervals);
    }

    // Getters and setters

    public String getCosId() { return cosId; }
    public void setCosId(String cosId) { this.cosId = cosId; }

    public long getOneWayFrameDelay() { return oneWayFrameDelay; }
    public void setOneWayFrameDelay(long oneWayFrameDelay) {
        this.oneWayFrameDelay = oneWayFrameDelay; }

    public long getOneWayFrameDelayRange() { return oneWayFrameDelayRange; }
    public void setOneWayFrameDelayRange(long oneWayFrameDelayRange) {
        this.oneWayFrameDelayRange = oneWayFrameDelayRange; }

    public long getOneWayMeanFrameDelay() { return oneWayMeanFrameDelay; }
    public void setOneWayMeanFrameDelay(long oneWayMeanFrameDelay) {
        this.oneWayMeanFrameDelay = oneWayMeanFrameDelay; }

    public long getOneWayInterFrameDelayVariation() { return oneWayInterFrameDelayVariation; }
    public void setOneWayInterFrameDelayVariation(long oneWayInterFrameDelayVariation) {
        this.oneWayInterFrameDelayVariation = oneWayInterFrameDelayVariation; }

    public double getOneWayFrameLossRatio() { return oneWayFrameLossRatio; }
    public void setOneWayFrameLossRatio(double oneWayFrameLossRatio) {
        this.oneWayFrameLossRatio = oneWayFrameLossRatio; }

    public double getOneWayAvailability() { return oneWayAvailability; }
    public void setOneWayAvailability(double oneWayAvailability) {
        this.oneWayAvailability = oneWayAvailability; }

    public long getOneWayResilHighLossIntervals() { return oneWayResilHighLossIntervals; }
    public void setOneWayResilHighLossIntervals(long oneWayResilHighLossIntervals) {
        this.oneWayResilHighLossIntervals = oneWayResilHighLossIntervals; }

    public long getOneWayResilConsecHighLossIntervals() { return oneWayResilConsecHighLossIntervals; }
    public void setOneWayResilConsecHighLossIntervals(long oneWayResilConsecHighLossIntervals) {
        this.oneWayResilConsecHighLossIntervals = oneWayResilConsecHighLossIntervals; }


}
