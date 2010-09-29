/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.slaves;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class SelectFrameContainerTest {

    public SelectFrameContainerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testAcwAppReset() {
        Slave s = new Slave();
        assertTrue(true);
//        s.handleApplicationReset()
    }

}