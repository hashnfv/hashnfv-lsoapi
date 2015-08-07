package com.cablelabs.vcpe.cos.cosbase.model;

import com.cablelabs.vcpe.common.Dbg;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by steve on 5/24/15.
 */

@XmlRootElement
public class CoS
{
    private String id;

    // private BandwidthProfile ingressBWProfile;
    // private BandwidthProfile egressBWProfile;
    // Above should be integrated at some point
    // ... for now we will support commitedInfoRate in Lie of BW Profiles
    private int    commitedInfoRate; // MBPS

    private double availbility;      // percentage
    private double frameDelay;       // milli-seconds
    private double jitter;           // milli-seconds
    private double frameLoss;        // percentage

    // no argument constructor required for JAX-RS
    public CoS() {
        commitedInfoRate = 0;
        availbility = 0.0;
        frameDelay = 0.0;
        jitter = 0.0;
        frameLoss = 0.0;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getCommitedInfoRate() { return commitedInfoRate; }
    public void setCommitedInfoRate(int commitedInfoRate) { this.commitedInfoRate = commitedInfoRate; }

    public double getAvailbility() { return availbility; }
    public void setAvailbility(double availbility) { this.availbility = availbility; }

    public double getFrameDelay() { return frameDelay; }
    public void setFrameDelay(double frameDelay) { this.frameDelay = frameDelay; }

    public double getJitter() { return jitter; }
    public void setJitter(double jitter) { this.jitter = jitter; }

    public double getFrameLoss() { return frameLoss; }
    public void setFrameLoss(double frameLoss) { this.frameLoss = frameLoss; }

    public void setAllProps(String id, int commitedInfoRate, double availbility, double frameDelay, double jitter, double frameLoss)
    {
        this.id = id;
        this.commitedInfoRate = commitedInfoRate;
        this.availbility = availbility;
        this.frameDelay = frameDelay;
        this.jitter = jitter;
        this.frameLoss = frameLoss;
    }

    public void dump() { dump(0); }
    public void dump(int tab) {
        Dbg.p(tab, "id:           " + this.id);
        Dbg.p(tab, "commInfoRate: " + this.commitedInfoRate);
        Dbg.p(tab, "availbility:  " + this.availbility);
        Dbg.p(tab, "frameDelay:   " + this.frameDelay);
        Dbg.p(tab, "jitter:       " + this.jitter);
        Dbg.p(tab, "frameLoss:    " + this.frameLoss);
    }

    public static void dumpList(List<CoS> cosList) { dumpList(0, cosList); }
    public static void dumpList(int tab, List<CoS> cosList) {
        int numCos = 0;
        Dbg.p("----- CoS List : [" + cosList.size() + "] elements");
        for (CoS curCos : cosList) {
            numCos++;
            Dbg.p(tab+1, "<Entry " + numCos+">");
            curCos.dump(tab+2);
        }
    }
}
