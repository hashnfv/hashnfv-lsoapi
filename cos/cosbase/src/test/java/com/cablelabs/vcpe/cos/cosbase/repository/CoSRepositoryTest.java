package com.cablelabs.vcpe.cos.cosbase.repository;

import com.cablelabs.vcpe.cos.cosbase.model.CoS;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by steve on 5/25/15.
 */
public class CoSRepositoryTest
{

    @Test
    public void test() {

        CoS gold   = new CoS();
        CoS silver = new CoS();
        CoS bronze = new CoS();

        //            id        CIR/MBS  avail  delay   jitter  frameloss
        gold.setAllProps("gold", 100, 0.99, 17.43, 2.43, 0.01);
        silver.setAllProps("silver", 50, 0.95, 27.43, 2.43, 0.02);
        bronze.setAllProps("bronze", 25,      0.90,  37.43,  2.43,   0.03);

        CoSRespository repo = CoSRespositoryInMem.INSTANCE;

        assertNotNull(repo.add(gold));
        assertNotNull(repo.add(silver));
        assertNotNull(repo.add(bronze));
        assertNull(repo.add(bronze)); // duplicate
        assertEquals(repo.count(), 3);

        assertNotNull(repo.get(gold.getId()));
        assertNotNull(repo.get(silver.getId()));
        assertNotNull(repo.get(bronze.getId()));

        assertNotNull(repo.delete(silver.getId()));
        assertNull(repo.delete(silver.getId()));
        assertNull(repo.delete("not-in-repo"));
        assertEquals(repo.count(), 2);

        assertEquals(repo.get(gold.getId()).getCommitedInfoRate(), 100);
        assertNotEquals(repo.get(bronze.getId()).getCommitedInfoRate(), 100);

        CoS tin  = new CoS();
        tin.setAllProps("tin", 1, 0.50, 47.43, 2.43, 0.04);


        assertNull(repo.update(tin));    // update non-existent cos
        assertEquals(repo.count(), 3);
        assertEquals(repo.get("tin").getCommitedInfoRate(), 1);

        tin.setCommitedInfoRate(5);
        assertNotNull(repo.update(tin)); // update existing cos, same object
        assertEquals(repo.get("tin").getCommitedInfoRate(), 5);

        CoS tin2  = new CoS();
        tin.setAllProps("tin", 8, 0.50, 47.43, 2.43, 0.04);
        assertNotNull(repo.update(tin)); // update existing cos, new object
        assertEquals(repo.get("tin").getCommitedInfoRate(), 8);
    }
}
