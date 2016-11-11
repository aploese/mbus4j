package net.sf.mbus4j;

/*
 * #%L
 * mbus4j-core
 * %%
 * Copyright (C) 2009 - 2014 MBus4J
 * %%
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009-2014, mbus4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * #L%
 */
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

    public ConnectionTest() {
    }

    @Test
    public void readWriteTcpIpConnectionObject() throws Exception {
        TcpIpConnection conn = new TcpIpConnection("test", 32, 33, 42);
        oos.writeObject(conn);
        flipMemStreams();
        TcpIpConnection conn1 = (TcpIpConnection) ois.readObject();
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
