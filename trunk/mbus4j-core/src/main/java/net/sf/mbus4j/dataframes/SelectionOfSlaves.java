/*
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2010, mbus4j.sf.net, and individual contributors as indicated
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
 *
 *
 * @author Arne Plöse
 *
 */
package net.sf.mbus4j.dataframes;

import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.json.JsonSerializeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.mbus4j.MBusUtils;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class SelectionOfSlaves
    implements LongFrame
{
    private byte address;
    private int bcdMaskedId;
    private byte maskedVersion;
    private short maskedMan;
    private byte maskedMedium;
    private List<DataBlock> datablocks = new ArrayList<DataBlock>(  );

    public SelectionOfSlaves( byte address )
    {
        this.address = address;
    }

    public SelectionOfSlaves( SendUserData old )
    {
        this.address = old.getAddress(  );
    }

    @Override
    public boolean addDataBlock( DataBlock dataBlock )
    {
        return datablocks.add( dataBlock );
    }

    @Override
    public byte getAddress(  )
    {
        return address;
    }

    @Override
    public ControlCode getControlCode(  )
    {
        return ControlCode.SND_UD;
    }

    @Override
    public DataBlock getLastDataBlock(  )
    {
        return datablocks.get( datablocks.size(  ) - 1 );
    }

    /**
     * @return the bcdMaskedId
     */
    public int getBcdMaskedId(  )
    {
        return bcdMaskedId;
    }

    /**
     * @return the maskedMan
     */
    public short getMaskedMan(  )
    {
        return maskedMan;
    }

    /**
     * @return the maskedMedium
     */
    public int getMaskedMedium(  )
    {
        return maskedMedium;
    }

    /**
     * @return the maskedVersion
     */
    public int getMaskedVersion(  )
    {
        return maskedVersion;
    }

    /**
     * @return the fcb
     */
    public boolean isFcb(  )
    {
        return false;
    }

    @Override
    public Iterator<DataBlock> iterator(  )
    {
        return datablocks.iterator(  );
    }

    @Override
    public void replaceDataBlock( DataBlock oldDataBlock, DataBlock newDataBlock )
    {
        final int pos = datablocks.indexOf( oldDataBlock );
        datablocks.remove( pos );
        datablocks.add( pos, newDataBlock );
    }

    @Override
    public void setAddress( byte address )
    {
        this.address = address;
    }

    /**
     * @param bcdMaskedId the bcdMaskedId to set
     */
    public void setBcdMaskedId(int bcdMaskedId )
    {
        this.bcdMaskedId = bcdMaskedId;
    }

    /**
     * @param maskedMan the maskedMan to set
     */
    public void setMaskedMan( short maskedMan )
    {
        this.maskedMan = maskedMan;
    }

    /**
     * @param maskedMedium the maskedMedium to set
     */
    public void setMaskedMedium( byte maskedMedium )
    {
        this.maskedMedium = maskedMedium;
    }

    /**
     * @param maskedVersion the maskedVersion to set
     */
    public void setMaskedVersion( byte maskedVersion )
    {
        this.maskedVersion = maskedVersion;
    }

    @Override
    public String toString(  )
    {
        StringBuilder sb = new StringBuilder(  );
        sb.append( "control code = " ).append( getControlCode(  ) ).append( '\n' );
        sb.append( String.format( "address = 0x%02X\n", address ) );
        sb.append( String.format( "bcdMaskedId = 0x%08X\n", bcdMaskedId ) );
        sb.append( String.format( "maskedMan = 0x%04X\n", maskedMan ) );
        sb.append( String.format( "maskedVersion = 0x%02X\n", maskedVersion ) );
        sb.append( String.format( "maskedMedium = 0x%02X\n", maskedMedium ) );

        return sb.toString(  );
    }

    public boolean matchId( int id )
    {
        int hexBcdId = MBusUtils.int2Bcd(id);
        int hexMask = bcdMaskedId;

        for ( int i = 0; i < 8; i++ )
        {
            if ( ( ( hexMask & 0x0F ) == 0x0F ) || ( ( hexMask & 0x0F ) == ( hexBcdId & 0x0F ) ) )
            {
                hexMask >>= 4;
                hexBcdId >>= 4;
            } else
            {
                return false;
            }
        }

        return true;
    }

    public boolean matchAll( int id, String man, MBusMedium medium, int version )
    {
        return matchId( id ) && matchMan( man ) && matchMedium( medium ) && matchVersion( version );
    }

    private boolean matchMan( String man )
    {
        int hexMan = MBusUtils.man2Short( man );
        int hexMask = maskedMan;

        for ( int i = 0; i < 8; i++ )
        {
            if ( ( ( hexMask & 0x0F ) != 0x0F ) && ( ( hexMask & 0x0F ) != ( hexMan & 0x0F ) ) )
            {
                return false;
            }

            hexMask >>= 4;
            hexMan >>= 4;
        }

        return true;
    }

    private boolean matchMedium( MBusMedium medium )
    {
        int hexMedium = medium.getId();
        int hexMask = maskedMedium;

        for ( int i = 0; i < 8; i++ )
        {
            if ( ( ( hexMask & 0x0F ) != 0x0F ) && ( ( hexMask & 0x0F ) != ( hexMedium & 0x0F ) ) )
            {
                return false;
            }

            hexMask >>= 4;
            hexMedium >>= 4;
        }

        return true;
    }

    private boolean matchVersion( int version )
    {
        int hexVersion = version;
        int hexMask = maskedVersion;

        for ( int i = 0; i < 8; i++ )
        {
            if ( ( ( hexMask & 0x0F ) != 0x0F ) && ( ( hexMask & 0x0F ) != ( hexVersion & 0x0F ) ) )
            {
                return false;
            }

            hexMask >>= 4;
            hexVersion >>= 4;
        }

        return true;
    }

    @Override
    public JSONObject toJSON( JsonSerializeType jsonSerializeType )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void fromJSON( JSONObject json )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
