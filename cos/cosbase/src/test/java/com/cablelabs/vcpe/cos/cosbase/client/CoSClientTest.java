package com.cablelabs.vcpe.cos.cosbase.client;

import com.cablelabs.vcpe.common.Dbg;
import com.cablelabs.vcpe.cos.cosbase.model.CoS;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by steve on 5/28/15.
 */

public class CoSClientTest {

    @Test
    public void testAll() throws Exception {
        CoSClient coSClient = new CoSClient();

        CoS gold   = new CoS();
        gold.setAllProps("gold", 100, 0.99, 17.43, 2.43, 0.01);
        Dbg.p("gold svc being created in CoS");
        gold.dump(1);

        gold = coSClient.create(gold);
        assertNotNull(gold);

        CoS retrievedCos = coSClient.get(gold.getId());

        assertNotNull(retrievedCos);
        Dbg.p("gold svc just retrieved from CoS");
        retrievedCos.dump(1);
        retrievedCos = null;

        gold.setCommitedInfoRate(50);
        Dbg.p("gold svc modified, to be updated");
        gold.dump(1);
        assertNotNull(coSClient.update(gold));
        retrievedCos = coSClient.get(gold.getId());
        assertNotNull(retrievedCos);
        Dbg.p("gold svc modified, just retrieved from CoS");
        retrievedCos.dump(1);

        coSClient.delete(gold.getId());

        CoS vid   = new CoS();
        CoS game  = new CoS();
        CoS dl    = new CoS();

        vid.setAllProps ("vid", 100, 0.99, 17.43, 2.43, 0.1);
        game.setAllProps("game", 75, 0.99, 7.43, 1.43, 0.01);
        dl.setAllProps("dl", 100, 0.99, 17.43, 2.43, 0.02);

        vid = coSClient.create(vid);
        assertNotNull(vid);

        game = coSClient.create(game);
        assertNotNull(game);

        dl = coSClient.create(dl);
        assertNotNull(dl);



        List<CoS> cosList = coSClient.getAll();
        assertNotNull(cosList);
        assertEquals(cosList.size(), 3);
        CoS.dumpList(cosList);

        coSClient.delete(game.getId());
        cosList = coSClient.getAll();
        assertNotNull(cosList);
        assertEquals(cosList.size(), 2);
        CoS.dumpList(cosList);

        coSClient.delete(vid.getId());
        cosList = coSClient.getAll();
        assertNotNull(cosList);
        assertEquals(cosList.size(), 1);
        CoS.dumpList(cosList);

        coSClient.delete(dl.getId());
        cosList = coSClient.getAll();
        assertNotNull(cosList);
        assertEquals(cosList.size(), 0);
        CoS.dumpList(cosList);
    }

    @Test
    public void testTestGet() throws Exception {
        CoSClient coSClient = new CoSClient();
        CoS cos = coSClient.testGet();
        cos.dump();;
    }

    @Test
    public void testPing() throws Exception {

        CoSClient coSClient = new CoSClient();
        String resp = coSClient.ping();
        Dbg.p(resp);
    }

}