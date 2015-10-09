/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.uni.unibase.model;

/**
 * Created by steve on 7/24/15.
 */
public class EvcPath {

    String id = "unset";
    Uni uni1 = null;
    Uni uni2 = null;
    String cos;
    Uni.SvcSpeed ingressBW = Uni.SvcSpeed.UNASSIGNED;
    Uni.SvcSpeed egressBW = Uni.SvcSpeed.UNASSIGNED;

    public EvcPath(String id, Uni uni1, Uni uni2,
                   Uni.SvcSpeed ingressBW,
                   Uni.SvcSpeed egressBW,
                   String cos)
    {
        this.id = id;
        this.uni1 = uni1;
        this.uni2 = uni2;
        this.cos = cos;
        this.ingressBW = ingressBW;
        this.egressBW = egressBW;
    }

    public String toJson() {

//{
//    "evc":
//    {
//        "evc:id": "822f8284-2b35-11e5-b345-feff819cdc9f",
//        "evc:uni-dest":
//        [
//            {
//                "order": 0,
//                "uni": "822f7eec-2b35-11e5-b345-feff819cdc9f"
//            }
//        ],
//        "evc:uni-source":
//        [
//            {
//                "order": 0,
//                "uni": "111f7eec-2c35-11e5-b345-feff819cdc9f"
//            }
//        ],
//        "evc:cos-id": "string",
//        "evc:ingress-bw":
//        {
//            "speed-1G": {}
//        },
//        "evc:egress-bw":
//        {
//            "speed-1G": {}
//        }
//    }
//}

        String json =   "{\n"+
                "   \"evc\":\n"+
                "   {\n"+
                "      \"evc:id\": \""+ this.getId() +"\",\n"+
                "      \"evc:uni-dest\":\n"+
                "      [\n"+
                "          {\n"+
                "              \"order\": 0,\n"+
                "              \"uni\": \""+ this.uni1.getId() +"\"\n"+
                "          }\n"+
                "      ],\n"+
                "      \"evc:uni-source\":\n"+
                "      [\n"+
                "          {\n"+
                "              \"order\": 0,\n"+
                "              \"uni\": \""+ this.uni2.getId() +"\"\n"+
                "          }\n"+
                "      ],\n"+
                "      \"evc:cos-id\": \""+ this.getCos() +"\",\n"+
                "      \"evc:ingress-bw\":\n"+
                "      {\n"+
//              "          \"speed-1G\": {}\n"+
                "          \"" + this.getIngressBW() + "\": {}\n"+
                "      },\n"+
                "      \"evc:egress-bw\":\n"+
                "      {\n"+
//              "          \"speed-1G\": {}\n"+
                "          \"" + this.getEgressBW() + "\": {}\n"+
                "      }\n"+
                "   }\n"+
                "}";
    return json;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Uni getUn1() { return uni1; }
    public void setUn1(Uni uni1) { this.uni1 = uni1; }

    public Uni getUn2() { return uni2; }
    public void setUn2(Uni uni2) { this.uni2 = uni2; }

    public String getCos() { return cos; }
    public void setCos(String cos) { this.cos = cos; }

    public Uni.SvcSpeed getIngressBW() { return ingressBW; }
    public void setIngressBW(Uni.SvcSpeed ingressBW) { this.ingressBW = ingressBW; }

    public Uni.SvcSpeed getEgressBW() { return egressBW; }
    public void setEgressBW(Uni.SvcSpeed egressBW) { this.egressBW = egressBW; }
}
