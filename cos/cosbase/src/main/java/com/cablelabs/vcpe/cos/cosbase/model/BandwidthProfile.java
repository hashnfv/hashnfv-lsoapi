/*******************************************************************************
* Copyright (c) 2015 CableLabs Inc. and others.
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License, Version 2.0
* which accompanies this distribution, and is available at
* http://www.apache.org/licenses/LICENSE-2.0
*******************************************************************************/

package com.cablelabs.vcpe.cos.cosbase.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by steve on 5/24/15.
 */

@XmlRootElement
public class BandwidthProfile {
    private int committedInfoRate = 0; // MBS
    private int committedBurtSize = 0; // MBS
    private int excessInfoRate    = 0; // MBS
    private int excessBurstSize   = 0; // MBS

    public int getCommittedInfoRate() { return committedInfoRate; }
    public void setCommittedInfoRate(int committedInfoRate) { this.committedInfoRate = committedInfoRate; }

    public int getCommittedBurtSize() { return committedBurtSize; }
    public void setCommittedBurtSize(int committedBurtSize) { this.committedBurtSize = committedBurtSize; }

    public int getExcessInfoRate() { return excessInfoRate; }
    public void setExcessInfoRate(int excessInfoRate) { this.excessInfoRate = excessInfoRate; }

    public int getExcessBurstSize() { return excessBurstSize; }
    public void setExcessBurstSize(int excessBurstSize) { this.excessBurstSize = excessBurstSize; }
}
