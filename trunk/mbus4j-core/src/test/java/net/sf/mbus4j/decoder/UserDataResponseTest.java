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
import net.sf.mbus4j.dataframes.Frame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.IntegerDataBlock;
import net.sf.mbus4j.dataframes.datablocks.LongDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ShortDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import net.sf.mbus4j.encoder.Encoder;
import net.sf.mbus4j.json.JSONFactory;
import net.sf.mbus4j.json.JsonSerializeType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class UserDataResponseTest implements DecoderListener {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    private Decoder instance;
    private List<Frame> frames;

    public UserDataResponseTest() {
    }

    @Before
    public void setUp() {
        frames = new ArrayList<>();
        instance = new Decoder(this);
    }

    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testABB_HEAT_8_3476_0() throws Exception {
        testPackage("ABB", MBusMedium.HEAT, 8, 3476, 0);
    }

    @Test
    public void testACW_HEAT_11_8772050_0() throws Exception {
        testPackage("ACW", MBusMedium.HEAT, 11, 8772050, 0);
    }

    @Test
    public void testACW_HEAT_11_9803784_0() throws Exception {
        testPackage("ACW", MBusMedium.HEAT, 11, 9803784, 0);
    }

    @Test
    public void testACW_HEAT_9_6522360_0() throws Exception {
        testPackage("ACW", MBusMedium.HEAT, 9, 6522360, 0);
    }

    @Test
    public void testEMH_ELECTRICITY_9_332092_0() throws Exception {
        testPackage("EMH", MBusMedium.ELECTRICITY, 9, 332092, 0);
    }

    @Test
    public void testEMU_32_5_701841_0() throws Exception {
        testPackage("EMU", MBusMedium.RESERVED_0X20, 5, 701841, 0);
    }

    @Test
    public void testLUG_HEAT_2_65068549_0() throws Exception {
        testPackage("LUG", MBusMedium.HEAT, 2, 65068549, 0);
    }

    public void testNew() throws Exception {
        try {
            testPackage("", "");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("PARSED PACKAGE DATA >>>>");
            System.err.println(frames.get(0).toString());
            System.err.println("<<<< PARSED PACKAGE DATA");
            throw ex;
        }
    }

    private void testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex) throws Exception {
        testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d", manufacturerId, medium.name(), version, identNumber, packetIndex));
    }

    private void testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex, String comment) throws Exception {
        testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d-%s", manufacturerId, medium.name(), version, identNumber, packetIndex, comment));
    }

    private void testPackage(final String man, final String deviceName) throws Exception {
        System.out.println("testPackage: " + deviceName);
        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("../byMAN/%s/%s.txt", man, deviceName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        final byte[] data = Decoder.ascii2Bytes(br.readLine());
        try {
            for (byte b : data) {
                instance.addByte(b);
            }
        } catch (RuntimeException ex) {
            System.out.println(String.format("PACKAGE>>> >>> >>>%s<<< <<< <<<PACKAGE", frames.get(0).toString()));
            throw ex;
        }
        assertEquals("ParserState", Decoder.DecodeState.EXPECT_START, instance.getState());
        assertEquals("DataValue not available", 1, frames.size());
        testJSON(frames.get(0), man, deviceName);
        BufferedReader resultStr = new BufferedReader(new StringReader(frames.get(0).toString()));
        int line = 1;
        String dataLine = br.readLine();
        String parsedLine = resultStr.readLine();
        while (parsedLine != null && dataLine != null) {
            line++;
            assertEquals(String.format("Line %d", line), dataLine, parsedLine);
            dataLine = br.readLine();
            parsedLine = resultStr.readLine();
        }
        resultStr.close();
        br.close();

        assertEquals(String.format("Length mismatch at line %d Data:", line), dataLine, parsedLine);
    }

    @Test
    public void testPAD_WATER_1_12345678_0() throws Exception {
        testPackage("PAD", MBusMedium.WATER, 1, 12345678, 0);
    }

    @Test
    public void testREL_GAS_8_3043269_0() throws Exception {
        testPackage("REL", MBusMedium.GAS, 8, 3043269, 0);
    }

    @Test
    public void testREL_GAS_9_5119949_0() throws Exception {
        testPackage("REL", MBusMedium.GAS, 9, 5119949, 0);
    }

    @Test
    public void testREL_GAS_9_8077237_0() throws Exception {
        testPackage("REL", MBusMedium.GAS, 9, 8077237, 0);
    }

    @Test
    public void testREL_HEAT_COST_ALLOCATOR_64_13131313_0() throws Exception {
        testPackage("REL", MBusMedium.HEAT_COST_ALLOCATOR, 64, 13131313, 0);
    }

    @Test
    public void testSIE_HEAT_1_60109158_0() throws Exception {
        testPackage("SIE", MBusMedium.HEAT, 1, 60109158, 0);
    }

    @Test
    public void testSLB_WATER_2_1309125_0() throws Exception {
        testPackage("SLB", MBusMedium.WATER, 2, 1309125, 0);
    }

    @Test
    public void testSLB_WATER_3_99365425_0() throws Exception {
        testPackage("SLB", MBusMedium.WATER, 3, 99365425, 0);
    }

    @Test
    public void testSPX_HEAT_52_44350175_0_No_Vife() throws Exception {
        testPackage("SPX", MBusMedium.HEAT, 52, 44350175, 0, "No_Vife");
    }

    @Test
    public void testSPX_HEAT_52_54850059_0_With_Vife() throws Exception {
        testPackage("SPX", MBusMedium.HEAT, 52, 54850059, 0, "With_Vife");
    }

    @Test
    public void testTCH_HEAT_1_44830614_0() throws Exception {
        testPackage("TCH", MBusMedium.HEAT, 1, 44830614, 0);
    }

    @Test
    public void testTCH_HEAT_38_21519982_0() throws Exception {
        testPackage("TCH", MBusMedium.HEAT, 38, 21519982, 0);
    }

    @Test
    public void testTCH_HEAT_38_21519982_1() throws Exception {
        testPackage("TCH", MBusMedium.HEAT, 38, 21519982, 1);
    }

    @Test
    public void testJAN_ELECTRICITY_9_57102137_0() throws Exception {
        testPackage("JAN", MBusMedium.ELECTRICITY, 9, 57102137, 0);
    }

    @Test
    public void testJAN_ELECTRICITY_9_26001712_0() throws Exception {
        testPackage("JAN", MBusMedium.ELECTRICITY, 9, 26001712, 0);
    }

    private void testJSON(Frame frame, String man, String deviceName) throws Exception {
        JSONObject json = frame.toJSON(JsonSerializeType.ALL);
        System.out.println(json.toString(1));
        Frame jsonFrame = JSONFactory.createFrame(json);
        jsonFrame.fromJSON(json);
        assertEquals("JSON Serializing of " + deviceName, frame.toString(), jsonFrame.toString());

        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("../byMAN/%s/%s-all.json", man, deviceName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        BufferedReader resultStr = new BufferedReader(new StringReader(json.toString(1)));
        int line = 0;
        String dataLine = br.readLine();
        String parsedLine = resultStr.readLine();
        while (parsedLine != null && dataLine != null) {
            line++;
            assertEquals(String.format("Line %d", line), dataLine, parsedLine);
            dataLine = br.readLine();
            parsedLine = resultStr.readLine();
        }
        resultStr.close();
        br.close();

        assertEquals(String.format("Length mismatch at line %d Data", line), dataLine, parsedLine);
    }

    @Test
    public void testBCD() throws Exception {
        UserDataResponse udr = new UserDataResponse();
        udr.setManufacturer("BCD");
        udr.setMedium(MBusMedium.OTHER);
        udr.setStatus(new UserDataResponse.StatusCode[]{UserDataResponse.StatusCode.APPLICATION_NO_ERROR});

        ByteDataBlock bdb = new ByteDataBlock();
        bdb.setDataFieldCode(DataFieldCode._2_DIGIT_BCD);
        bdb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        bdb.setVif(VifPrimary.ENERGY_J_E_0);
        bdb.setValue((byte) -1);
        udr.addDataBlock(bdb);
        bdb = new ByteDataBlock();
        bdb.setDataFieldCode(DataFieldCode._2_DIGIT_BCD);
        bdb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        bdb.setVif(VifPrimary.ENERGY_J_E_0);
        bdb.setBcdError("-E");
        udr.addDataBlock(bdb);
        ShortDataBlock sdb = new ShortDataBlock();
        sdb.setDataFieldCode(DataFieldCode._4_DIGIT_BCD);
        sdb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        sdb.setVif(VifPrimary.ENERGY_J_E_0);
        sdb.setValue((short) -1);
        udr.addDataBlock(sdb);
        sdb = new ShortDataBlock();
        sdb.setDataFieldCode(DataFieldCode._4_DIGIT_BCD);
        sdb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        sdb.setVif(VifPrimary.ENERGY_J_E_0);
        sdb.setBcdError("-EE-");
        udr.addDataBlock(sdb);
        IntegerDataBlock idb = new IntegerDataBlock();
        idb.setDataFieldCode(DataFieldCode._6_DIGIT_BCD);
        idb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        idb.setVif(VifPrimary.ENERGY_J_E_0);
        idb.setValue(-1);
        udr.addDataBlock(idb);
        idb = new IntegerDataBlock();
        idb.setDataFieldCode(DataFieldCode._6_DIGIT_BCD);
        idb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        idb.setVif(VifPrimary.ENERGY_J_E_0);
        idb.setBcdError("--EE  ");
        udr.addDataBlock(idb);
        idb = new IntegerDataBlock();
        idb.setDataFieldCode(DataFieldCode._8_DIGIT_BCD);
        idb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        idb.setVif(VifPrimary.ENERGY_J_E_0);
        idb.setValue(-1);
        udr.addDataBlock(idb);
        idb = new IntegerDataBlock();
        idb.setDataFieldCode(DataFieldCode._8_DIGIT_BCD);
        idb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        idb.setVif(VifPrimary.ENERGY_J_E_0);
        idb.setBcdError("--EE  CC");
        udr.addDataBlock(idb);
        LongDataBlock ldb = new LongDataBlock();
        ldb.setDataFieldCode(DataFieldCode._12_DIGIT_BCD);
        ldb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        ldb.setVif(VifPrimary.ENERGY_J_E_0);
        ldb.setValue(-1);
        udr.addDataBlock(ldb);
        ldb = new LongDataBlock();
        ldb.setDataFieldCode(DataFieldCode._12_DIGIT_BCD);
        ldb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        ldb.setVif(VifPrimary.ENERGY_J_E_0);
        ldb.setBcdError("--EE  CCbbAA");
        udr.addDataBlock(ldb);
        ldb = new LongDataBlock();
        ldb.setDataFieldCode(DataFieldCode._12_DIGIT_BCD);
        ldb.setFunctionField(FunctionField.INSTANTANEOUS_VALUE);
        ldb.setStorageNumber(1);
        ldb.setVif(VifPrimary.ENERGY_J_E_0);
        ldb.setBcdError("00--EE  CC90");
        udr.addDataBlock(ldb);

        System.out.print(udr);
        JSONObject jsonUdr = udr.toJSON(JsonSerializeType.ALL);
        System.out.print(jsonUdr.toString(1));
        UserDataResponse udr1 = (UserDataResponse) JSONFactory.createFrame(jsonUdr);
        udr1.fromJSON(jsonUdr);
        System.out.print(udr1);

        assertEquals(udr.toString(), udr1.toString());

        Encoder enc = new Encoder();
        byte[] data = enc.encode(udr);
        System.out.print(Decoder.bytes2Ascii(data));

        try {
            for (byte b : data) {
                instance.addByte(b);
            }
        } catch (RuntimeException ex) {
            System.out.println(String.format("PACKAGE>>> >>> >>>%s<<< <<< <<<PACKAGE", frames.get(0).toString()));
            throw ex;
        }
        System.out.println(String.format("PACKAGE>>> >>> >>>%s<<< <<< <<<PACKAGE", frames.get(0).toString()));
        assertEquals("ParserState", Decoder.DecodeState.EXPECT_START, instance.getState());
        assertEquals("DataValue not available", 1, frames.size());
        assertEquals(udr.toString(), frames.get(0).toString());
    }

    @Test
    public void testUnknown() throws Exception {
        //final String dataStr = "";
        //final String dataStr = "0001040000681B1B6808007237720708AC4809033100000006163819000000000F010101E2164F";
        //final String dataStr = "0201040000684D4D680800725020770877040B042B0000000C78502077080407AB3800000C15433008000B2D4804000B3B6629000A5A82060A5E51050B61191300046D39132C130227FA0009FD0E1009FD0F200F00003D169D";
        //final String dataStr = "0301040000681515680800724606780877040B04410000000C784606780861163E";
        //final String dataStr = "0401040000681515680800727299780877040B04400000000C7872997808DE1639";
        //final String dataStr = "0501040000685555680800728237800977040B043D0000000C7882378009C2006C3C12C4000600000000CC001401000000D400AE4F16123912D5002ECD0A9D3BD400BE4F16123912D5003E6E12033BD400DB4F160F3212D5005BA2C4F041B916B0";
        //final String dataStr = "0601040000684D4D680800728337800977040B04240000000C78833780090406620000000C14471000000B2D0000000B3B0000000A5A62010A5E44010B61740100046D15132C130227330009FD0E1009FD0F210F0020A51671";
        //final String dataStr = "0701040000685555680800728437800977040B042F0000000C7884378009C2006C3C12C4000665000000CC001452190000D400AE4F1A0B3212D5002EB19A0E41D400BE4F1A0A3B12D5003EB6F37D3FD400DB4F1A0E3212D5005B4A000842041648";
        //final String dataStr = "68F7F768080172372110572E2809020200000006047E18000000008610047E18000000008620040000000000008640042800000000008650040000000000008660042800000000008680400492180000000084402400000000848040240000000084C040240000000084808040240000000084C0804024000000008480C04024000000000424FA4F000084808040FD590000000084C080402B000000008480C0402B0000000084C0C0402B000000008440FD48C8080000848040FD48ED03000084C040FD48EC0300008440FD5900000000848040FD590000000084C040FD590000000084402B000000008480402B0000000084C0402B000000000F2516";
        //WRONG_CS final String dataStr =   "68f7f768083272121700262e2809020f0000000604ff80d701000086100444ec2a020000862004486b53000000864004f35924000000865004650e790000008660048b4babffffff86804004607dbf02000084402000000000848040240000000084c040240000000084808040240000000084c0804024000000008480c0402400000000042468c88b0284808040fd59a9e1010084c080402bfa1601008480c0402b95bcffff84c0c0402b6c2a01008440fd48b5080000848040fd48e908000084c040fd48ea0800008440e85988ca0200848040fd598362010084c040fd5917ff000084402b259a00008480402b144c000084c0402bbf3000000f4316";
final String dataStr =   "68f7f768083272121700262e2809021100000006040c81d701000086100451ec2a020000862004486b53000000864004f35924000000865004680e79000000866004884babffffff868040046f7dbf02000084402400000000848040240000000084c040240000000084808040240000000084c0804024000000008480c040240000000004246fc88b0284808040fd59f5fe010084c080402b942701008480c0402bf6bcffff84c0c0402b713901008440fd48b2080000848040fd48ed08000084c040fd48e90800008440fd5927f80200848040fd592e68010084c040fd59610f010084402b86a400008480402b924d000084c0402b7b3500000f8316";
//final String dataStr = "";
        //final String dataStr = "";
        final byte[] data = Decoder.ascii2Bytes(dataStr);
        for (byte b : data) {
            instance.addByte(b);
        }
        assertEquals("ParserState", Decoder.DecodeState.EXPECT_START, instance.getState());
        assertNotNull("DataValue not available", frames.get(0));
        if (frames.get(0) instanceof UserDataResponse) {
            UserDataResponse udr = (UserDataResponse)frames.get(0);
        System.out.println(String.format("%s-%s-%d-%d-%d", udr.getManufacturer(), udr.getMedium().name(), udr.getVersion(), udr.getIdentNumber(), 0));
        }
        System.out.println(String.format("JSON>>> >>> >>>%s<<< <<< <<<JSON", frames.get(0).toJSON(JsonSerializeType.ALL).toString()));
        System.out.println(String.format("PACKAGE>>> >>> >>>%s\n%s<<< <<< <<<PACKAGE", dataStr, frames.get(0).toString()));
    }

    @Override
    public void success(Frame parsingFrame) {
        frames.add(parsingFrame);
    }

}
