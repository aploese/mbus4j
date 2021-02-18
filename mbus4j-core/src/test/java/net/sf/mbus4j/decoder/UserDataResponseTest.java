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
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import net.sf.json.JSONObject;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.IntegerDataBlock;
import net.sf.mbus4j.dataframes.datablocks.LongDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ShortDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.dif.FunctionField;
import net.sf.mbus4j.dataframes.datablocks.vif.VifPrimary;
import net.sf.mbus4j.encoder.Encoder;
import net.sf.mbus4j.json.JSONFactory;
import net.sf.mbus4j.json.JsonSerializeType;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class UserDataResponseTest {

    private Decoder instance;

    public UserDataResponseTest() {
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
    public void testABB_HEAT_8_3476_0() throws Exception {
        testPackage("ABB", MBusMedium.HEAT, 8, 3476, 0, true);
    }

    @Test
    public void testACW_HEAT_11_8772050_0() throws Exception {
        testPackage("ACW", MBusMedium.HEAT, 11, 8772050, 0, true);
    }

    @Test
    public void testACW_HEAT_11_9803784_0() throws Exception {
        testPackage("ACW", MBusMedium.HEAT, 11, 9803784, 0, true);
    }

    @Test
    public void testACW_HEAT_9_6522360_0() throws Exception {
        testPackage("ACW", MBusMedium.HEAT, 9, 6522360, 0, true);
    }

    @Test
    public void testEMH_ELECTRICITY_9_332092_0() throws Exception {
        testPackage("EMH", MBusMedium.ELECTRICITY, 9, 332092, 0, true);
    }

    @Test
    public void testEMU_32_5_701841_0() throws Exception {
        testPackage("EMU", MBusMedium.RESERVED_0X20, 5, 701841, 0, false);
    }

    @Test
    public void testLUG_HEAT_2_65068549_0() throws Exception {
        testPackage("LUG", MBusMedium.HEAT, 2, 65068549, 0, true);
    }

    private Frame testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex, final boolean testUniqueDB) throws Exception {
        return testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d", manufacturerId, medium.name(), version, identNumber, packetIndex), testUniqueDB);
    }

    private Frame testPackage(String manufacturerId, MBusMedium medium,
            int version, int identNumber, int packetIndex, String comment, final boolean testUniqueDB) throws Exception {
        return testPackage(manufacturerId, String.format("%s-%s-%d-%d-%d-%s", manufacturerId, medium.name(), version, identNumber, packetIndex, comment), testUniqueDB);
    }

    private Frame testPackage(final String man, final String deviceName, final boolean testUniqueDB) throws Exception {
        System.out.println("testPackage: " + deviceName);
        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("../byMAN/%s/%s.txt", man, deviceName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        Frame f = null;
        try {
            f = instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(br.readLine())));
        } catch (RuntimeException ex) {
            System.out.println(String.format("PACKAGE>>> >>> >>>%s<<< <<< <<<PACKAGE", f.toString()));
            throw ex;
        }
        assertEquals(Decoder.DecodeState.SUCCESS, instance.getState(), "ParserState");
        assertNotNull(f, "DataValue not available");
        testJSON(f, man, deviceName);
        if (testUniqueDB) {
            testUniqueDB(f);
        }
        BufferedReader resultStr = new BufferedReader(new StringReader(f.toString()));
        int line = 1;
        String dataLine = br.readLine();
        String parsedLine = resultStr.readLine();
        while (parsedLine != null && dataLine != null) {
            line++;
            assertEquals(dataLine, parsedLine, String.format("Line %d", line));
            dataLine = br.readLine();
            parsedLine = resultStr.readLine();
        }
        resultStr.close();
        br.close();

        assertEquals(dataLine, parsedLine, String.format("Length mismatch at line %d Data:", line));
        return f;
    }

    @Test
    public void testPAD_WATER_1_12345678_0() throws Exception {
        testPackage("PAD", MBusMedium.WATER, 1, 12345678, 0, true);
    }

    @Test
    public void testREL_GAS_8_3043269_0() throws Exception {
        testPackage("REL", MBusMedium.GAS, 8, 3043269, 0, true);
    }

    @Test
    public void testREL_GAS_9_5119949_0() throws Exception {
        testPackage("REL", MBusMedium.GAS, 9, 5119949, 0, true);
    }

    @Test
    public void testREL_GAS_9_8077237_0() throws Exception {
        testPackage("REL", MBusMedium.GAS, 9, 8077237, 0, true);
    }

    @Test
    public void testREL_HEAT_COST_ALLOCATOR_64_13131313_0() throws Exception {
        testPackage("REL", MBusMedium.HEAT_COST_ALLOCATOR, 64, 13131313, 0, true);
    }

    @Test
    public void testSIE_HEAT_1_60109158_0() throws Exception {
        testPackage("SIE", MBusMedium.HEAT, 1, 60109158, 0, true);
    }

    @Test
    public void testSLB_WATER_2_1309125_0() throws Exception {
        testPackage("SLB", MBusMedium.WATER, 2, 1309125, 0, true);
    }

    @Test
    public void testSLB_WATER_3_99365425_0() throws Exception {
        testPackage("SLB", MBusMedium.WATER, 3, 99365425, 0, true);
    }

    @Test
    public void testSPX_HEAT_52_44350175_0_No_Vife() throws Exception {
        testPackage("SPX", MBusMedium.HEAT, 52, 44350175, 0, "No_Vife", true);
    }

    @Test
    public void testSPX_HEAT_52_54850059_0_With_Vife() throws Exception {
        testPackage("SPX", MBusMedium.HEAT, 52, 54850059, 0, "With_Vife", true);
    }

    @Test
    public void testTCH_HEAT_1_44830614_0() throws Exception {
        testPackage("TCH", MBusMedium.HEAT, 1, 44830614, 0, true);
    }

    @Test
    public void testTCH_HEAT_38_21519982_0() throws Exception {
        testPackage("TCH", MBusMedium.HEAT, 38, 21519982, 0, true);
    }

    @Test
    public void testTCH_HEAT_38_21519982_1() throws Exception {
        testPackage("TCH", MBusMedium.HEAT, 38, 21519982, 1, true);
    }

    @Test
    public void testJAN_ELECTRICITY_9_57102137_0() throws Exception {
        testPackage("JAN", MBusMedium.ELECTRICITY, 9, 57102137, 0, true);
    }

    @Test
    public void testJAN_ELECTRICITY_9_26001712_0() throws Exception {
        testPackage("JAN", MBusMedium.ELECTRICITY, 9, 26001712, 0, true);
    }

    @Test
    public void testTIP_ELECTRICITY_2_15124_0() throws Exception {
        testPackage("TIP", MBusMedium.ELECTRICITY, 2, 15124, 0, true);
    }

    @Test
    public void testTIP_ELECTRICITY_2_15808_0() throws Exception {
        testPackage("TIP", MBusMedium.ELECTRICITY, 2, 15808, 0, false);
    }

    private void testJSON(Frame frame, String man, String deviceName) throws Exception {
        JSONObject json = frame.toJSON(JsonSerializeType.ALL);
        System.out.println(json.toString(1));
        Frame jsonFrame = JSONFactory.createFrame(json);
        jsonFrame.fromJSON(json);
        assertEquals(frame.toString(), jsonFrame.toString(), "JSON Serializing of " + deviceName);

        InputStream is = UserDataResponseTest.class.getResourceAsStream(String.format("../byMAN/%s/%s-all.json", man, deviceName));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        BufferedReader resultStr = new BufferedReader(new StringReader(json.toString(1)));
        int line = 0;
        String dataLine = br.readLine();
        String parsedLine = resultStr.readLine();
        while (parsedLine != null && dataLine != null) {
            line++;
            assertEquals(dataLine, parsedLine, String.format("Line %d", line));
            dataLine = br.readLine();
            parsedLine = resultStr.readLine();
        }
        resultStr.close();
        br.close();

        assertEquals(dataLine, parsedLine, String.format("Length mismatch at line %d Data", line));
    }

    @Test
    public void testBCD() throws Exception {
        UserDataResponse udr = new UserDataResponse();
        udr.setManufacturer("BCD");
        udr.setMedium(MBusMedium.OTHER);
        udr.setStatus(UserDataResponse.StatusCode.APPLICATION_NO_ERROR);

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
        byte[] data = enc.encodeFrame(udr);
        System.out.print(Decoder.bytes2Ascii(data));

        try {
            Frame f = instance.parse(new ByteArrayInputStream(data));
            System.out.println(String.format("PACKAGE>>> >>> >>>%s<<< <<< <<<PACKAGE", f.toString()));
            assertEquals(Decoder.DecodeState.SUCCESS, instance.getState(), "ParserState");
            assertNotNull(f, "DataValue not available");
            assertEquals(udr.toString(), f.toString());
        } catch (RuntimeException ex) {
            System.out.println(String.format("PACKAGE>>> >>> >>>%s<<< <<< <<<PACKAGE", udr.toString()));
            throw ex;
        }
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
        //final String dataStr =   "68f7f768083272121700262e2809021100000006040c81d701000086100451ec2a020000862004486b53000000864004f35924000000865004680e79000000866004884babffffff868040046f7dbf02000084402400000000848040240000000084c040240000000084808040240000000084c0804024000000008480c040240000000004246fc88b0284808040fd59f5fe010084c080402b942701008480c0402bf6bcffff84c0c0402b713901008440fd48b2080000848040fd48ed08000084c040fd48e90800008440fd5927f80200848040fd592e68010084c040fd59610f010084402b86a400008480402b924d000084c0402b7b3500000f8316";

//final String dataStr = "68F7F768083272121700262E2809028700000006040DB0EF010000861004942770020000862004897780000000864004C5F53100000086500448709F0000008660047B8592FFFFFF86804004D6AB4603000084402400000000848040240000000084C040240000000084808040240000000084C0804024000000008480C0402400000000042459C7F10284808040FD59C087000084C080402B5D7700008480C0402B31CEFFFF84C0C0402B408400008440FD48F0080000848040FD48F708000084C040FD48F50800008440FD59ECFB0000848040FD59B6A5000084C040FD59719F000084402B7C3300008480402B5F22000084C0402B812100000F4916";
//final String dataStr = "68F7F768083372753700262E280902B00000000604C90E60010000861004D90E600100008620040300000000008640044CC9010000008650047A6537000000866004E363CAFFFFFF8680400439697201000084402400000000848040240000000084C040240000000084808040240000000084C0804024000000008480C04024000000000424E4FA350284808040FD597148000084C080402BA12700008480C0402B97F0FFFF84C0C0402B2F2D00008440FD48ED080000848040FD48FA08000084C040FD48F10800008440FD592A620000848040FD59F136000084C040FD59312C000084402BC01300008480402B5C0A000084C0402B840900000F0816";
//final String dataStr = "68F7F768083472773700262E280902650000000604531AFFFFFFFF861004004608000000862004AE2B090000008640049D1626000000865004D9CF03000000866004B3462200000086804004DFD84B01000084402400000000848040240000000084C040240000000084808040240000000084C0804024000000008480C04024000000000424A35D720284808040FD599C35000084C080402B060100008480C0402B3906000084C0C0402B483000008440FD48F3080000848040FD48FB08000084C040FD48F00800008440FD59025A0000848040FD592738000084C040FD596A40000084402BFB0000008480402B5B0B000084C0402BB0F4FFFF0F4E16";
//final String dataStr = "68F7F768083572763700262E280902F700000006047F1F4D000000861004831F4D00000086200400000000000086400429AA0100000086500486C016000000866004A2E9EAFFFFFF86804004C3FF5600000084402400000000848040240000000084C040240000000084808040240000000084C0804024000000008480C04024000000000424006AD40184808040FD59FD11000084C080402B1F0500008480C0402B15F9FFFF84C0C0402B8D0A00008440FD48F2080000848040FD48FB08000084C040FD48F80800008440FD595D120000848040FD59060C000084C040FD59990F000084402B5C0200008480402BF800000084C0402BCB0100000F3416";
//final String dataStr = "68D6D6680836722451010030510202C3000000841005F5D909008420050300000004AAFC016CFDFFFF04AAFC0292FFFFFF04AAFC0342FFFFFF042A2CFCFFFF8440AAFC01E40700008440AAFC023A0700008440AAFC03B207000084402ADA160000848040AAFC0152080000848040AAFC0244070000848040AAFC03BC0700008480402A2A17000002FDC8FC01F20802FDC8FC02FC0802FDC8FC03F80802FB2EF40102FF13010003FDD9FC01A4030003FDD9FC0229030003FDD9FC035E030003FD592B0A0001FFF1FC01E101FFF1FC02FA01FFF1FC03F701FF61F0B216";
//final String dataStr = "6839396808377208580100305102026F000000841005F586000084100519C3FFFF842005000000008420050000000002FB2EF50102FF13010001FF6163D916";
//final String dataStr = "68D6D668083872070202003051020290000000841005076F07008420052000000004AAFC015258000004AAFC02182E000004AAFC03343F0000042AA8C500008440AAFC013CFBFFFF8440AAFC02D0F8FFFF8440AAFC0366EFFFFF84402A68E3FFFF848040AAFC017A580000848040AAFC02A42E0000848040AAFC035A4100008480402ABAC7000002FDC8FC01F50802FDC8FC02000902FDC8FC03FB0802FB2EF50102FF13010003FDD9FC012D2A0003FDD9FC0291190003FDD9FC0336220003FD59F5650001FFF1FC016301FFF1FC026201FFF1FC036001FF61624216";
//final String dataStr = "68D6D668083972085101003051020272000000841005D5A902008420050300000004AAFC014416000004AAFC02E60F000004AAFC0332230000042A664900008440AAFC01E40C00008440AAFC026C0C00008440AAFC03361A000084402A90330000848040AAFC01BE190000848040AAFC0232140000848040AAFC03E82B00008480402ABA59000002FDC8FC01F20802FDC8FC02FD0802FDC8FC03F40802FB2EF50102FF13010003FDD9FC01400B0003FDD9FC02C7080003FDD9FC0327130003FD592F270001FFF1FC015601FFF1FC024E01FFF1FC035001FF6151EB16";
//final String dataStr = "68D6D668083A7230510100305102027E000000841005164702008420050300000004AAFC01903D000004AAFC022E3B000004AAFC03B6440000042A74BD00008440AAFC013A1100008440AAFC023C0F00008440AAFC03E60F000084402A5C300000848040AAFC01E83F0000848040AAFC02183D0000848040AAFC03824600008480402A8CC3000002FDC8FC01F40802FDC8FC02FC0802FDC8FC03F60802FB2EF50102FF13010003FDD9FC01DE1F0003FDD9FC02331E0003FDD9FC0368220003FD597B600001FFF1FC016001FFF1FC026001FFF1FC036101FF61604116";
//final String dataStr = "68D6D668083B7241840000305102026E0000008410054CFF02008420050300000004AAFC01B220000004AAFC02820A000004AAFC037E130000042AC63E00008440AAFC01641E00008440AAFC02BE0500008440AAFC035A19000084402A863D0000848040AAFC01A62C0000848040AAFC02FE0B0000848040AAFC03FE1F00008480402AEE57000002FDC8FC01F40802FDC8FC02FD0802FDC8FC03F50802FB2EF50102FF13010003FDD9FC017E130003FDD9FC0239050003FDD9FC03F30D0003FD59AB260001FFF1FC014901FFF1FC025701FFF1FC033C01FF61479216";
        final String dataStr = "68D6D668083C72752603003051020238000000841005AC3901008420052000000004AAFC010000000004AAFC020000000004AAFC0300000000042A000000008440AAFC01F6FFFFFF8440AAFC02000000008440AAFC030000000084402AECFFFFFF848040AAFC010A000000848040AAFC0200000000848040AAFC03000000008480402A1400000002FDC8FC01F30802FDC8FC02FB0802FDC8FC03FB0802FB2EF40102FF13010003FDD9FC0104000003FDD9FC0202000003FDD9FC0304000003FD590A000001FFF1FC016401FFF1FC022C01FFF1FC036401FF6164E616";

//final String dataStr = "";
        //final String dataStr = "";
        Frame f = instance.parse(new ByteArrayInputStream(Decoder.ascii2Bytes(dataStr)));
        assertEquals(Decoder.DecodeState.SUCCESS, instance.getState(), "ParserState");
        assertNotNull(f, "DataValue not available");
        if (f instanceof UserDataResponse) {
            UserDataResponse udr = (UserDataResponse) f;
            System.out.println(String.format("%s-%s-%d-%d-%d", udr.getManufacturer(), udr.getMedium().name(), udr.getVersion(), udr.getIdentNumber(), 0));
        }
        System.out.println(String.format("JSON>>> >>> >>>%s<<< <<< <<<JSON", f.toJSON(JsonSerializeType.ALL).toString()));
        System.out.println(String.format("PACKAGE>>> >>> >>>%s\n%s<<< <<< <<<PACKAGE", dataStr, f.toString()));
    }

    private void testUniqueDB(Frame frame) {
        UserDataResponse udr = (UserDataResponse) frame;
        for (DataBlock db : udr) {
            assertEquals(db, udr.findDataBlock(db.getDataFieldCode(), db.getParamDescr(), db.getUnitOfMeasurement(), db.getFunctionField(), db.getStorageNumber(), db.getSubUnit(), db.getTariff()));
        }
    }

}
