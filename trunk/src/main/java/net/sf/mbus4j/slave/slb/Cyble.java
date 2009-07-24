/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.mbus4j.slave.slb;

import java.util.Date;
import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.RequestClassXData;
import net.sf.mbus4j.dataframes.SendUserData;
import net.sf.mbus4j.dataframes.SendUserDataManSpec;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.datablocks.ByteDataBlock;
import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.DateAndTimeDataBlock;
import net.sf.mbus4j.dataframes.datablocks.EnhancedIdentificationDataBlock;
import net.sf.mbus4j.dataframes.datablocks.IntegerDataBlock;
import net.sf.mbus4j.dataframes.datablocks.RawDataBlock;
import net.sf.mbus4j.dataframes.datablocks.ShortDataBlock;
import net.sf.mbus4j.dataframes.datablocks.StringDataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.dataframes.datablocks.vif.AsciiVif;
import net.sf.mbus4j.dataframes.datablocks.vif.Vif;
import net.sf.mbus4j.dataframes.datablocks.vif.VifStd;
import net.sf.mbus4j.slave.Slave;

/**
 *
 * @author aploese
 */
public class Cyble extends Slave {

    private final IntegerDataBlock fabNo;
    private final StringDataBlock custId;
    private final DateAndTimeDataBlock currentTime;
    private final ShortDataBlock batTime;
    private final IntegerDataBlock volume;
    private final IntegerDataBlock volumeReturn;
    private final IntegerDataBlock volumeLastMonth;
    private final RawDataBlock manSpecData;

    public Cyble(int address, int id, int version, MBusMedium medium, int fabNo, String custId, Short batTime, Vif volumeVif) {
        super(address, id, "SLB", version, medium);
        this.fabNo = new IntegerDataBlock(DataFieldCode._32_BIT_INTEGER, VifStd.FABRICATION_NO);
        this.fabNo.setValue(fabNo);
        this.custId = new StringDataBlock(DataFieldCode.VARIABLE_LENGTH, new AsciiVif("CUST.ID"));
        this.custId.setValue(custId);
        currentTime = new DateAndTimeDataBlock(VifStd.TIMEPOINT_TIME_AND_DATE);
        if (batTime == null) {
            this.batTime = null;
        } else {
            this.batTime = new ShortDataBlock(DataFieldCode._16_BIT_INTEGER, new AsciiVif("BAT.TIME"));
            this.batTime.setValue(batTime);
        }
        volume = new IntegerDataBlock(DataFieldCode._32_BIT_INTEGER, volumeVif);
        volume.setValue(12345);
        volumeReturn = new IntegerDataBlock(DataFieldCode._32_BIT_INTEGER, volumeVif);
        volumeLastMonth = new IntegerDataBlock(DataFieldCode._32_BIT_INTEGER, volumeVif);
        volumeLastMonth.setValue(54321);
        manSpecData = new RawDataBlock(DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_LAST_PACKET);
        manSpecData.setValue(0x00, 0x00, 0x00);
    }

    @Override
    public Frame handleReqUd2(RequestClassXData request) {
        Frame result = super.handleReqUd2(request);
        if (result instanceof UserDataResponse) {
            currentTime.setValue(new Date());
            UserDataResponse resp = (UserDataResponse) result;
            resp.addDataBlock(fabNo);
            resp.addDataBlock(custId);
            resp.addDataBlock(currentTime);
            if (batTime != null) {
                resp.addDataBlock(batTime);
            }
            resp.addDataBlock(volume);
            resp.addDataBlock(volumeReturn);
            resp.addDataBlock(volumeLastMonth);
            resp.addDataBlock(manSpecData);
        }
        return result;
    }

    @Override
    public Frame handleSendUserData(SendUserData sendUserData) {
        for (DataBlock db : sendUserData) {
            if (VifStd.BUS_ADDRESS.equals(db.getVif())) {
                setAddress(((ByteDataBlock)db).getValue());
            } else if (VifStd.ENHANCED_IDENTIFICATION_RECORD.equals(db.getVif())) {
                final EnhancedIdentificationDataBlock edb = (EnhancedIdentificationDataBlock)db;
                setId(edb.getId());
                setMan(edb.getMan());
                setMedium(edb.getMedium());
            }
        }
        return super.handleSendUserData(sendUserData);
    }

    @Override
    public Frame handleSendUserDataManSpec(SendUserDataManSpec sendUserDataManSpec) {
        if (sendUserDataManSpec.getCiField() == 0xA6) {
            return handleReqUd2(null);
        } else {
            return super.handleSendUserDataManSpec(sendUserDataManSpec);
        }
    }

}
