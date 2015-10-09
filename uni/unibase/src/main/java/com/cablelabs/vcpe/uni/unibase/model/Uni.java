/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.uni.unibase.model;

import com.cablelabs.vcpe.common.Dbg;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by steve on 5/24/15.
 */

@XmlRootElement
public class Uni
{

    public enum SvcSpeed {
        UNASSIGNED   ("UNASSIGNED"),
        TEN_MEG      ("speed-10M"),
        HUNDRED_MEG  ("speed-100M"),
        ONE_GIG      ("speed-1G"),
        TEN_GIG      ("speed-10G");
                        private final String s;
                        private SvcSpeed(final String s) {this.s = s;}
                        @Override public String toString() { return s; }
    }

    public enum PhysMedium { // just a sampling of 802.3 phys layers
        UNASSIGNED     ("UNASSIGNED"),
        TEN_BASE_T     ("10BASE‑T"),
        HUNDERED_BASE_T("100BASE‑T"),
        GIG_BASE_T     ("1000BASE‑T"),
        TEN_GIG_BASE_T ("10GBASE‑T");
                            private final String s;
                            private PhysMedium(final String s) {this.s = s;}
                            @Override public String toString() { return s; }
        }

    public enum MacLayer {
        UNASSIGNED   ("UNASSIGNED"),
        IEEE_802_3   ("IEEE 802.3-2005");
                        private final String s;
                        private MacLayer(final String s) {this.s = s;}
                        @Override public String toString() { return s; }
        }

    public enum SyncMode {
        UNASSIGNED   ("UNASSIGNED"),
        ENABLED      ("syncEnabled"),
        DISABLED     ("syncDisabled");
                        private final String s;
                        private SyncMode(final String s) {this.s = s;}
                        @Override public String toString() { return s; }
        }

    public enum Type { // not sure what this is
        UNASSIGNED   ("UNASSIGNED"),
        UNITYPE      ("UNITYPE");
                        private final String s;
                        private Type(final String s) {this.s = s;}
                        @Override public String toString() { return s; }
        }

    @XmlElement(name="id")
    private String id;

    @XmlTransient // This does not get written to JSON body
    private SvcSpeed speed;

    @XmlElement(name="ip-address")
    private String ipAddress;

    @XmlElement(name="mac-address")
    private String macAddress;

    @XmlElement(name="physical-medium")
    private PhysMedium physicalMedium;

    @XmlElement(name="mac-layer")
    private MacLayer macLayer;

    @XmlElement(name="mode")
    private SyncMode mode;

    @XmlElement(name="type")
    private Type type;

    @XmlElement(name="mtu-size")
    private long mtuSize;

    // no argument constructor required for JAX-RS
    public Uni() {
        this.id              = "unset";
        this.speed           = SvcSpeed.UNASSIGNED;
        this.ipAddress       = "unset";
        this.macAddress      = "unset";
        this.physicalMedium  = PhysMedium.UNASSIGNED;
        this.macLayer        = MacLayer.UNASSIGNED;
        this.mode            = SyncMode.UNASSIGNED;
        this.type            = Type.UNASSIGNED;
        this.mtuSize         = -1;
    }

    public void setAllProps (String id, SvcSpeed speed, String ipAddress,
                             String macAddress, PhysMedium physicalMedium,
                             MacLayer macLayer, SyncMode mode, Type type, long mtuSize) {
        this.id = id;
        this.speed = speed;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.physicalMedium = physicalMedium;
        this.macLayer = macLayer;
        this.mode = mode;
        this.type = type;
        this.mtuSize = mtuSize;
    }

    public void dump() { dump(0); }
    public void dump(int tab) {
        Dbg.p(tab, "id:             " + this.id);
        Dbg.p(tab, "speed:          " + this.speed);
        Dbg.p(tab, "ipAddress:      " + this.ipAddress);
        Dbg.p(tab, "macAddress:     " + this.macAddress);
        Dbg.p(tab, "physicalMedium: " + this.physicalMedium);
        Dbg.p(tab, "macLayer:       " + this.macLayer);
        Dbg.p(tab, "mode:           " + this.mode);
        Dbg.p(tab, "type:           " + this.type);
        Dbg.p(tab, "mtuSize:        " + this.mtuSize);
    }

    public String toJson() {

        String json =   "{\n"+
                "   \"uni\":\n"+
                "   {\n"+
                "      \"uni:id\": \""+ this.getId() +"\",\n"+
                "      \"speed\":\n" +
                "      {\n" +
                "            \""  + this.getSpeed() +  "\": "+"\"1\"\n"+
                "      },\n"+
                "      \"uni:mac-layer\": \""+ this.getMacLayer() +"\",\n"+
                "      \"uni:physical-medium\": \""+ this.getPhysicalMedium() +"\",\n"+
                "      \"uni:mtu-size\": \""+ this.getMtuSize() +"\",\n"+
                "      \"uni:type\": \"\",\n"+
                "      \"uni:mac-address\": \""+ this.getMacAddress() +"\",\n"+
                "      \"uni:ip-address\": \""+ this.getIpAddress() +"\",\n"+
                "      \"uni:mode\": \""+ this.getMode() +"\"\n"+
                "   }\n"+
                "}";

        return json;
    }


    public static SvcSpeed cirToSvcSpeed (long cir) {

        // find closest
        SvcSpeed svcSpeed = SvcSpeed.UNASSIGNED;
        if ( cir <= 10000 )
            svcSpeed = SvcSpeed.TEN_MEG;
        else if ( cir <= 100000 )
            svcSpeed = SvcSpeed.HUNDRED_MEG;
        else if ( cir <= 1000000 )
            svcSpeed = SvcSpeed.ONE_GIG;
        else
            svcSpeed = SvcSpeed.TEN_GIG;

        return svcSpeed;
    }

    public static PhysMedium svcSpeedToPhysMedium (SvcSpeed svcSpeed) {


        // just for demo, this really needs to come from host, when hosts are modeled
        switch (svcSpeed) {
            case TEN_MEG:
                return PhysMedium.TEN_BASE_T;
            case HUNDRED_MEG:
                return PhysMedium.HUNDERED_BASE_T;
            case ONE_GIG:
                return PhysMedium.GIG_BASE_T;
            case TEN_GIG:
                return PhysMedium.TEN_GIG_BASE_T;
            default:
                return PhysMedium.UNASSIGNED;
        }
    }

    public static void dumpList(List<Uni> uniList) { dumpList(0, uniList); }
    public static void dumpList(int tab, List<Uni> uniList) {
        int numUni = 0;
        Dbg.p("----- Uni List : [" + uniList.size() + "] elements");
        for (Uni curUni : uniList) {
            numUni++;
            Dbg.p(tab+1, "<Entry " + numUni+">");
            curUni.dump(tab+2);
        }
    }

    // getters & setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public SvcSpeed getSpeed() { return speed; }
    public void setSpeed(SvcSpeed speed) { this.speed = speed; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }

    public PhysMedium getPhysicalMedium() { return physicalMedium; }
    public void setPhysicalMedium(PhysMedium physicalMedium) { this.physicalMedium = physicalMedium; }

    public MacLayer getMacLayer() { return macLayer; }
    public void setMacLayer(MacLayer macLayer) { this.macLayer = macLayer; }

    public SyncMode getMode() { return mode; }
    public void setMode(SyncMode mode) { this.mode = mode; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public long getMtuSize() { return mtuSize; }
    public void setMtuSize(long mtuSize) { this.mtuSize = mtuSize; }
}
