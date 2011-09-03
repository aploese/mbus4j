/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author aploese
 */
public class ConnectionTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private MasterStreams master;

    public ConnectionTest() {
    }

    @Test
    public void readWriteTcpIpConnectionObject() throws Exception {
        TcpIpConnection conn = new TcpIpConnection("test", 32, 33, 42);
        oos.writeObject(conn);
        flipMemStreams();
        TcpIpConnection conn1 = (TcpIpConnection)ois.readObject();
        assertEquals(conn.getHost(), conn1.getHost());
    }

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ByteArrayOutputStream baos;
    private ByteArrayInputStream bais;

    @Before
    public void setUp() throws Exception {
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
    }

    @After
    public void tearDown() throws Exception {
        oos = null;
        baos = null;
        ois = null;
        bais = null;
    }

    private void flipMemStreams() throws IOException {

        bais = new ByteArrayInputStream(baos.toByteArray());
        ois = new ObjectInputStream(bais);
    }

}
