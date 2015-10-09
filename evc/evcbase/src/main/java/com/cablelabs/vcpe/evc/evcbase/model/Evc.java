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

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by steve on 5/24/15.
 */

@XmlRootElement
public class Evc
{
    // set up so that we can use the ENUM's as strings for REST calls & dbg prints

    public enum EvcType {

        UNASSIGNED                  ("UNASSIGNED"),
        POINT_TO_POINT              ("POINT_TO_POINT"),
        MULTIPOINT_TO_MULTIPOINT    ("MULTIPOINT_TO_MULTIPOINT"),
        ROOTED_MULTIPOINT           ("ROOTED_MULTIPOINT");
                                        private final String s;
                                        private EvcType(final String s) {this.s = s;}
                                        @Override public String toString() { return s; }
    }
    public enum FrameDelivery { // may belong in more general location eventually

        UNASSIGNED      ("UNASSIGNED"),
        DISCARD         ("DISCARD"),
        UNCONDITIONAL   ("UNCONDITIONAL"),
        CONDITIONAL     ("CONDITIONAL");
                            private final String s;
                            private FrameDelivery(final String s) {this.s = s;}
                            @Override public String toString() { return s; }
    }

    private String id;
    private EvcType evcType;
    private long maxUnis;
    private FrameDelivery unicastFrameDelivery;
    private FrameDelivery multicastFrameDelivery;
    private FrameDelivery broadcastFrameDelivery;
    private List<String> uniIdList = null;
    private List<String> uniIpList = null;
    private List<String> uniMacList = null;
    private boolean ceVLanIdPreservation;
    private boolean ceVlanCosPreservation;
    private long evcMaxSvcFrameSize; // at least 1522, should be > 1600 byte
    private String cosId; // CoS Attributes serving as EVCPerformance attr for now
    //private EvcPerf evcPerf = null;

    // EVC Perf params
    private double oneWayFrameDelay;                    // milliseconds
    private double oneWayFrameLossRatio;                // percentage
    private double oneWayAvailability;                  // percentage

    // zero argument constructor required for JAX-RS
    public Evc() {
        id = "unset";
        evcType = EvcType.UNASSIGNED;
        maxUnis = -1;
        unicastFrameDelivery = FrameDelivery.UNASSIGNED;
        multicastFrameDelivery  = FrameDelivery.UNASSIGNED;;
        broadcastFrameDelivery  = FrameDelivery.UNASSIGNED;;
        uniIdList = null;
        uniIpList = null;
        uniMacList = null;
        ceVLanIdPreservation = false;
        ceVlanCosPreservation = false;
        evcMaxSvcFrameSize = -1;
        cosId = "unset";

        //evcPerf = new EvcPerf();
        oneWayFrameDelay = -1.0;
        oneWayFrameLossRatio = -1.0;
        oneWayAvailability = -1.0;
    }

    // Utility methods

    public void setAllNonPerfProps(String id, EvcType evcType, long maxUnis,
                                   List<String> uniIdList, List<String> uniMacList, List<String> uniIpList,
                                   FrameDelivery unicastFrameDelivery,
                                   FrameDelivery multicastFrameDelivery,
                                   FrameDelivery broadcastFrameDelivery,
                                   boolean ceVLanIdPreservation, boolean ceVlanCosPreservation,
                                   long evcMaxSvcFrameSize, String cosId) {
        this.id = id;
        this.evcType = evcType;
        this.uniIdList = uniIdList;
        this.uniMacList = uniMacList;
        this.uniIpList = uniIpList;
        this.maxUnis = maxUnis;
        this.unicastFrameDelivery = unicastFrameDelivery;
        this.multicastFrameDelivery = multicastFrameDelivery;
        this.broadcastFrameDelivery = broadcastFrameDelivery;
        this.ceVLanIdPreservation = ceVLanIdPreservation;
        this.ceVlanCosPreservation = ceVlanCosPreservation;
        this.evcMaxSvcFrameSize = evcMaxSvcFrameSize;
        this.cosId = cosId;
    }

    public void setAllPerfProps( double oneWayFrameDelay,
                                 double oneWayFrameLossRatio,
                                 double oneWayAvailability )
    {
        this.oneWayFrameDelay     = oneWayFrameDelay;
        this.oneWayFrameLossRatio = oneWayFrameLossRatio;
        this.oneWayAvailability   = oneWayAvailability;
    }

    public void dump() { dump(0); }
    public void dump(int tab) {
        Dbg.p(tab, "id:      " + this.id);
        Dbg.p(tab, "cosId:   " + this.cosId);
        Dbg.p(tab, "evcType: " + this.evcType);
        Dbg.p(tab, "maxUnis: " + this.maxUnis);
        Dbg.p(tab, "Uni ID List:");
        for (String uniId : uniIdList)
            Dbg.p(tab+1, uniId);
        Dbg.p(tab, "Uni Mac List:");
        for (String macId : uniMacList)
            Dbg.p(tab+1, macId);
        Dbg.p(tab, "Uni IP List:");
        for (String ipAddr : uniIpList)
            Dbg.p(tab+1, ipAddr);
        Dbg.p(tab, "UnicastFrameDelivery:   " + this.unicastFrameDelivery);
        Dbg.p(tab, "MulticastFrameDelivery: " + this.multicastFrameDelivery);
        Dbg.p(tab, "BroadcastFrameDelivery: " + this.broadcastFrameDelivery);
        Dbg.p(tab, "CeVLanIdPreservation:   " + this.ceVLanIdPreservation);
        Dbg.p(tab, "CeVlanCosPreservation:  " + this.ceVlanCosPreservation);
        Dbg.p(tab, "EvcMaxSvcFrameSize:     " + this.evcMaxSvcFrameSize);
        Dbg.p(tab, "--- EVC Performance Params");
        Dbg.p(tab+1, "oneWayFrameDelay:     " + this.oneWayFrameDelay);
        Dbg.p(tab+1, "oneWayFrameLossRatio: " + this.oneWayFrameLossRatio);
        Dbg.p(tab+1, "oneWayAvailability:   " + this.oneWayAvailability);
    }

    public static void dumpList(List<Evc> evcList) { dumpList(0, evcList); }
    public static void dumpList(int tab, List<Evc> evcList) {
        int numEvc = 0;
        Dbg.p("----- Evc List : [" + evcList.size() + "] elements");
        for (Evc curEvc : evcList) {
            numEvc++;
            Dbg.p(tab+1, "<Entry " + numEvc+">");
            curEvc.dump(tab+2);
        }
    }

    // Getters & Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public EvcType getEvcType() { return evcType; }
    public void setEvcType(EvcType evcType) { this.evcType = evcType; }

    public long getMaxUnis() { return maxUnis; }

    public void setMaxUnis(long maxUnis) { this.maxUnis = maxUnis; }

    public FrameDelivery getUnicastFrameDelivery() { return unicastFrameDelivery; }
    public void setUnicastFrameDelivery(FrameDelivery unicastFrameDelivery) {
        this.unicastFrameDelivery = unicastFrameDelivery; }

    public FrameDelivery getMulticastFrameDelivery() { return multicastFrameDelivery; }
    public void setMulticastFrameDelivery(FrameDelivery multicastFrameDelivery) {
        this.multicastFrameDelivery = multicastFrameDelivery; }

    public FrameDelivery getBroadcastFrameDelivery() { return broadcastFrameDelivery; }
    public void setBroadcastFrameDelivery(FrameDelivery broadcastFrameDelivery) {
        this.broadcastFrameDelivery = broadcastFrameDelivery; }

    public List<String> getUniIdList() { return uniIdList; }
    public void setUniIdList(List<String> uniIdList) { this.uniIdList = uniIdList; }

    public List<String> getUniMacList() { return uniMacList; }
    public void setUniMacList(List<String> uniMacList) { this.uniMacList = uniMacList; }


    public List<String> getUniIpList() { return uniIpList; }
    public void setUniIpList(List<String> uniIpList) { this.uniIpList = uniIpList; }

    public boolean isCeVLanIdPreservation() { return ceVLanIdPreservation; }
    public void setCeVLanIdPreservation(boolean ceVLanIdPreservation) {
        this.ceVLanIdPreservation = ceVLanIdPreservation; }

    public boolean isCeVlanCosPreservation() { return ceVlanCosPreservation; }

    public void setCeVlanCosPreservation(boolean ceVlanCosPreservation) {
        this.ceVlanCosPreservation = ceVlanCosPreservation; }

    public long getEvcMaxSvcFrameSize() { return evcMaxSvcFrameSize; }
    public void setEvcMaxSvcFrameSize(long evcMaxSvcFrameSize) {
        this.evcMaxSvcFrameSize = evcMaxSvcFrameSize; }

    public String getCosId() { return cosId; }
    public void setCosId(String cosId) { this.cosId = cosId; }

    public double getOneWayFrameDelay() { return oneWayFrameDelay; }
    public void setOneWayFrameDelay(double oneWayFrameDelay) { this.oneWayFrameDelay = oneWayFrameDelay; }

    public double getOneWayFrameLossRatio() { return oneWayFrameLossRatio; }
    public void setOneWayFrameLossRatio(double oneWayFrameLossRatio) { this.oneWayFrameLossRatio = oneWayFrameLossRatio; }

    public double getOneWayAvailability() { return oneWayAvailability; }
    public void setOneWayAvailability(double oneWayAvailability) { this.oneWayAvailability = oneWayAvailability; }


    // Someday might make sense to put all EVC Perf params in seperate class
//    private class EvcPerf {
//        private long   oneWayFrameDelay;                    // milliseconds
//        private long   oneWayFrameDelayRange;               // milliseconds
//
//
//        public EvcPerf () {
//            this.oneWayFrameDelay = -333;
//            this.oneWayFrameDelayRange = -333;
//        }
//
//        public void setAllParams (long oneWayFrameDelay, long oneWayFrameDelayRange) {
//            this.oneWayFrameDelay = oneWayFrameDelay;
//            this.oneWayFrameDelayRange = oneWayFrameDelayRange;
//        }
//
//        public long getOneWayFrameDelay() { return oneWayFrameDelay; }
//        public void setOneWayFrameDelay(long oneWayFrameDelay) {
//            this.oneWayFrameDelay = oneWayFrameDelay; }
//
//        public long getOneWayFrameDelayRange() { return oneWayFrameDelayRange; }
//        public void setOneWayFrameDelayRange(long oneWayFrameDelayRange) {
//            this.oneWayFrameDelayRange = oneWayFrameDelayRange; }
//    }
}
