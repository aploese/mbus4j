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
import java.util.ArrayList;
import java.util.List;
import net.sf.mbus4j.dataframes.Frame;
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
public class DecoderTest {

	private Decoder instance;

	public DecoderTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new Decoder();
	}

	@After
	public void tearDown() {
		instance = null;
	}

	@Test
	public void testErrorRecovery() throws IOException {
		String[] raw = new String[] {
				"68c4c46808007213961113475f0307fd000000041390000000046d1f00cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313fae0f505c2036cdf11c403135c00000082046cdc12840413be000000c2046c0000c404130000000082056c000084051300000000c2056c0000c405130000000082066c000084061300000000c2066c0000c40613000000000f0100009a16",
				"68c4",
				"68c4c46808007213961113475f0307000000041390000000046d2000cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313fae0f505c2036cdf11c403135c00000082046cdc12840413be000000c2046c0000c404130000000082056c000084051300000000c2056c0000c405130000000082066c000084061300000000c2066c0000c40613000000000f0100009d16",
				"68c4c46808007213961113475f030700000000041390000000046d2100cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313fae0f505c2036cdf11c403135c00000082046cdc12840413be000000c2046c0000c404130000000082056c000084051300000000c2056c0000c405130000000082066c000084061300000000c2066c0000c40613000000000f0100009f16",
				"68c4c46808007213961113475f030701000000041390000000046d2100cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c",
				"68c4c46808007213961113475f030702000000041390000000046d2100cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313fae0f505c2036cdf11c403135c00000082046cdc12840413be000000c2046c0000c404130000000082056c000084051300000000c2056c0000c405130000000082066c000084061300000000c2066c0000c40613000000000f010000a116",
				"68c4c46808007213961113475f030703000000041390000000046d2200cd13426c000044130000000042ec7ede180c789351050082016cbf1884011300000000c2016cbe19c401130000000082026cbf1a84021300000000c2026cbe1bc40213fae0f50582036cbf1c840313" };

		Frame f = instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[0])));

		assertEquals(Decoder.DecodeState.SUCCESS, instance.getState());
		assertNotNull(f);

		// Just way too short
		try {
			instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[1])));
			fail();
		} catch (EOFException e) {
			assertTrue(true);
		}

		// Here oner byte is missing
		try {
			instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[2])));
			fail();
		} catch (DecodeException e) {
			assertTrue(true);
		}

		// recover from byteloss in last frame
		f = instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[3])));
		assertNotNull(f);

		// RECOVER
		try {
			instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[4])));
			fail();
		} catch (EOFException e) {
			assertTrue(true);
		}

		final InputStream is = new ByteArrayInputStream(Decoder.ascii2Bytes(raw[5]));
		f = instance.parse(is);
		
		try {
			instance.parse(is);
			fail();
		} catch (EOFException e) {
			assertTrue(true);
		}
		
		try {
			instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(raw[6])));
			fail();
		} catch (EOFException e) {
			assertTrue(true);
		}
	}

}
