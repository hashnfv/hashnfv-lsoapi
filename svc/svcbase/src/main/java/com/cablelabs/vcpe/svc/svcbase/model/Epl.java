package com.cablelabs.vcpe.svc.svcbase.model;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.evc.evcbase.model.Evc;
import com.cablelabs.vcpe.evc.evcbase.client.EvcClient;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by steve on 5/24/15.
 */

@XmlRootElement
public class Epl
{
    private String id;
    private long numCustLocations;
    private List<String> custAddressList = null;
    private List<String> uniHostMacList = null;
    private List<String> uniHostIpList = null;
    private String cos;
    private String evcId;

    // zero argument constructor required for JAX-RS
    public Epl() {
        id = "unset";
        this.numCustLocations = -1;
        this.custAddressList = null;
        this.uniHostMacList = null;
        this.uniHostIpList = null;
        this.cos = "unset";
        this.evcId = "unset";
    }

    public void setAllProps (String id, long numCustLocations,
                             List<String> custAddressList,
                             List<String> uniHostMacList, List<String> uniHostIpList, String cos, String evcId) {
        this.id = id;
        this.numCustLocations = numCustLocations;
        this.custAddressList = custAddressList;
        this.uniHostMacList = uniHostMacList;
        this.uniHostIpList = uniHostIpList;
        this.cos = cos;
        this.evcId = evcId;
    }

    public void dump() { dump(0); }
    public void dump(int tab) {

        Dbg.p(tab, "id: " + this.id);
        Dbg.p(tab, "numCustLocations: " + this.numCustLocations);
        Dbg.p(tab, "Address List:");
        for (String addr : custAddressList)
            Dbg.p(tab+1, addr);
        Dbg.p(tab, "UNI Mac List:");
        for (String mac : uniHostMacList)
            Dbg.p(tab+1, mac);
        Dbg.p(tab, "UNI IP List:");
        for (String ip : uniHostIpList)
            Dbg.p(tab+1, ip);
        Dbg.p(tab, "cos: " + this.cos);
        Dbg.p(tab, "Evc:"  + this.evcId);
//        if ( this.evcId != "unset") { // hacky
//            EvcClient evcClient = new EvcClient();
//            Evc evc = evcClient.get(this.evcId);
//            evc.dump(tab + 2);
//        }
    }

    public static void dumpList(List<Epl> eplList) { dumpList(0, eplList); }
    public static void dumpList(int tab, List<Epl> eplList) {
        int numSvc = 0;
        Dbg.p("----- Epl List : [" + eplList.size() + "] elements");
        for (Epl curEpl : eplList) {
            numSvc++;
            Dbg.p(tab+1, "<Entry " + numSvc+">");
            curEpl.dump(tab+2);
        }
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public long getNumCustLocations() { return numCustLocations; }
    public void setNumCustLocations(long numCustLocations) { this.numCustLocations = numCustLocations; }

    public List<String> getCustAddressList() { return custAddressList; }
    public void setCustAddressList(List<String> custAddressList) { this.custAddressList = custAddressList; }

    public List<String> getUniHostMacList() { return uniHostMacList; }
    public void setUniHostMacList(List<String> uniHostMacList) { this.uniHostMacList = uniHostMacList; }


    public List<String> getUniHostIpList() { return uniHostIpList; }
    public void setUniHostIpList(List<String> uniHostIpList) { this.uniHostIpList = uniHostIpList; }

    public String getCos() { return cos; }
    public void setCos(String cos) { this.cos = cos; }

    public String getEvcId() { return evcId; }
    public void setEvcId(String evcId) { this.evcId = evcId; }
}
