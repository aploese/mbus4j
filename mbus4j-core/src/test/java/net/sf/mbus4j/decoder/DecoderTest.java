package net.sf.mbus4j.decoder;

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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.LongDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.VifFD;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Arne Plöse
 */
public class DecoderTest {

    private Decoder instance;

    public DecoderTest() {
    }

    @BeforeEach
    public void setUp() {
        instance = new Decoder();
    }

    @AfterEach
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testErrorRecovery() throws IOException {
        String[] raw = new String[]{
            "68c4c46808007213961113475f0307fd000000041390000000046d1f00cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313fae0f505c2036cdf11c403135c00000082046cdc12840413be000000c2046c0000c404130000000082056c000084051300000000c2056c0000c405130000000082066c000084061300000000c2066c0000c40613000000000f0100009a16",
            "68c4",
            "68c4c46808007213961113475f0307000000041390000000046d2000cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313fae0f505c2036cdf11c403135c00000082046cdc12840413be000000c2046c0000c404130000000082056c000084051300000000c2056c0000c405130000000082066c000084061300000000c2066c0000c40613000000000f0100009d16",
            "68c4c46808007213961113475f030700000000041390000000046d2100cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313fae0f505c2036cdf11c403135c00000082046cdc12840413be000000c2046c0000c404130000000082056c000084051300000000c2056c0000c405130000000082066c000084061300000000c2066c0000c40613000000000f0100009f16",
            "68c4c46808007213961113475f030701000000041390000000046d2100cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c",
            "68c4c46808007213961113475f030702000000041390000000046d2100cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313fae0f505c2036cdf11c403135c00000082046cdc12840413be000000c2046c0000c404130000000082056c000084051300000000c2056c0000c405130000000082066c000084061300000000c2066c0000c40613000000000f010000a116",
            "68c4c46808007213961113475f030703000000041390000000046d2200cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313"};

        Frame f = instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[0])));

        assertEquals(Decoder.DecodeState.SUCCESS, instance.getState());
        assertNotNull(f);

        // Just way too short
        assertThrows(EOFException.class, () -> instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[1]))));

        // Here oner byte is missing
        assertThrows(DecodeException.class, () -> instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[2]))));

        // recover from byteloss in last frame
        f = instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[3])));
        assertNotNull(f);

        // RECOVER
        assertThrows(EOFException.class, () -> instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[4]))));

        final InputStream is = new ByteArrayInputStream(Decoder.ascii2Bytes(raw[5]));
        f = instance.parse(is);

        assertThrows(EOFException.class, () -> instance.parse(is));

        assertThrows(EOFException.class, () -> instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[6]))));
    }

    @Test
    public void testDecode() throws IOException {
        SendUserData sud = (SendUserData) instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes("680e0e6873fd5107fd0b0800180f00000000ff16")));
        assertTrue(sud.isFcb());
        assertTrue(sud.isFcb());

        Iterator<DataBlock> iter = sud.iterator();
        assertTrue(iter.hasNext());
        LongDataBlock ldb = (LongDataBlock) iter.next();
        assertEquals(DataFieldCode._64_BIT_INTEGER, ldb.getDataFieldCode());
        assertEquals(VifFD.PARAMETER_SET_IDENTIFICATION, ldb.getVif());
        assertEquals(0x000000000f180008L, ldb.getValue());
    }

}
