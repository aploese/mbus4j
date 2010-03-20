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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import net.sf.mbus4j.dataframes.datablocks.DataBlock;
import net.sf.mbus4j.dataframes.datablocks.dif.DataFieldCode;
import net.sf.mbus4j.json.JSONFactory;
import net.sf.mbus4j.json.JsonSerializeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class UserDataResponse
    implements LongFrame,
               PrimaryAddress,
               Cloneable
{
    @Override
    public JSONObject toJSON( JsonSerializeType jsonSerializeType )
    {
        JSONObject result = new JSONObject(  );
        result.accumulate( "controlCode",
                           getControlCode(  ) );

        if ( JsonSerializeType.ALL == jsonSerializeType )
        {
            result.accumulate( "manufacturer",
                               getManufacturer(  ) );
            result.accumulate( "medium",
                               getMedium(  ).getLabel(  ) );
            result.accumulate( "version",
                               JSONFactory.encodeHexByte( getVersion(  ) ) );
            result.accumulate( "identNumber",
                               getIdentNumber(  ) );
            result.accumulate( "address",
                               JSONFactory.encodeHexByte( getAddress(  ) ) );
            result.accumulate( "accessNumber",
                               getAccessNumber(  ) );
            result.accumulate( "acd",
                               isAcd(  ) );
            result.accumulate( "dfc",
                               isDfc(  ) );
            result.accumulate( "signature",
                               JSONFactory.encodeHexShort( getSignature(  ) ) );

            JSONArray jsonStatusArray = new JSONArray(  );

            for ( StatusCode st : getStatus(  ) )
            {
                jsonStatusArray.add( st.getLabel(  ) );
            }

            result.accumulate( "status", jsonStatusArray );
        }

        if ( ( JsonSerializeType.ALL == jsonSerializeType ) || ( 
                                                                       JsonSerializeType.SLAVE_CONFIG == jsonSerializeType
                                                                    ) )
        {
            JSONArray jsonDataBlocks = new JSONArray(  );

            for ( DataBlock db : this )
            {
                jsonDataBlocks.add( db.toJSON( jsonSerializeType ) );
            }

            result.accumulate( "dataBlocks", jsonDataBlocks );
        }

        return result;
    }

    @Override
    public void fromJSON( JSONObject json )
    {
        setManufacturer( json.containsKey( "manufacturer" ) ? json.getString( "manufacturer" ) : "" );
        setMedium( json.containsKey( "medium" ) ? MBusMedium.fromLabel( json.getString( "medium" ) )
                                                : MBusMedium.UNKNOWN_MEDIUM );
        setVersion( JSONFactory.decodeHexByte( json, "version", (byte) 0 ) );
        setIdentNumber( json.containsKey( "identNumber" ) ? json.getInt( "identNumber" ) : 0 );
        setAddress( JSONFactory.decodeHexByte( json, "address", (byte) 0 ) );
        setAccessNumber( JSONFactory.getShort( json, "accessNumber", (short) 0 ) );
        setAcd( JSONFactory.getBoolean( json, "acd", false ) );
        setDfc( JSONFactory.getBoolean( json, "dfc", false ) );
        setSignature( JSONFactory.decodeHexShort( json, "signature", (short) 0 ) );

        if ( json.containsKey( "status" ) )
        {
            JSONArray statusArray = json.getJSONArray( "status" );

            if ( statusArray.size(  ) == 0 )
            {
                setStatus( new StatusCode[0] );
            } else
            {
                status = new StatusCode[statusArray.size(  )];

                for ( int i = 0; i < status.length; i++ )
                {
                    status[i] = StatusCode.fromLabel( statusArray.getString( i ) );
                }
            }
        }

        JSONArray jsonDataBlocks = json.getJSONArray( "dataBlocks" );

        for ( int i = 0; i < jsonDataBlocks.size(  ); i++ )
        {
            DataBlock db = JSONFactory.createDataBlock( jsonDataBlocks.getJSONObject( i ) );
            db.fromJSON( jsonDataBlocks.getJSONObject( i ) );
            addDataBlock( db );
        }
    }

    public static enum StatusCode
    { // Taken from Chapter 6.6 Fig 27
        APPLICATION_NO_ERROR( 0x00, "No application error" ),
        APPLICATION_BUSY( 0x01, "Application busy" ),
        APPLICATION_ANY_ERROR( 0x02, "Any application error" ),
        APPLICATION_RESERVED( 0x03, "Application reserved" ),
        // Taken from Chapter 6.2 Fig 16
        POWER_LOW( 0x04, "Power Low" ),PERMANENT_ERROR( 0x08, "Permanent error" ),
        TEMPORARY_ERROR( 0x10, "Temporary error" ),
        MAN_SPEC_0X20( 0x20, "Specific to manufacturer 0x20" ),
        MAN_SPEC_0X40( 0x40, "Specific to manufacturer 0x40" ),
        MAN_SPEC_0X80( 0x80, "Specific to manufacturer 0x80" );
        public static byte toId( StatusCode[] values )
        {
            byte result = 0;

            for ( StatusCode sc : values )
            {
                result |= sc.id;
            }

            return result;
        }

        public final byte id;
        private final String label;

        private StatusCode( int id, String description )
        {
            this.id = (byte) id;
            this.label = description;
        }

        @Override
        public String toString(  )
        {
            return label;
        }

        public String getLabel(  )
        {
            return label;
        }

        public static StatusCode fromLabel( String label )
        {
            for ( StatusCode value : values(  ) )
            {
                if ( value.getLabel(  ).equals( label ) )
                {
                    return value;
                }
            }

            return valueOf( label );
        }
    }

    private boolean acd;
    private boolean dfc;
    private byte version;
    private short accessNumber;
    private StatusCode[] status;
    private short signature;
    private MBusMedium medium;
    private int identNumber;
    private String manufacturer;
    private List<DataBlock> dataBlocks = new ArrayList<DataBlock>(  );
    private byte address;

    public UserDataResponse(  )
    {
        super(  );
    }

    public UserDataResponse( boolean acd, boolean dfc )
    {
        this.acd = acd;
        this.dfc = dfc;
    }

    @Override
    public boolean addDataBlock( DataBlock dataBlock )
    {
        return dataBlocks.add( dataBlock );
    }

    public boolean addAllDataBlocks( List<DataBlock> list )
    {
        return dataBlocks.addAll( list );
    }

    public void addStatus( StatusCode status )
    {
        if ( this.status == null )
        {
            this.status = new StatusCode[] { status };
        } else
        {
            this.status = Arrays.copyOf( this.status, this.status.length + 1 );
            this.status[this.status.length - 1] = status;
        }
    }

    public void clearDataBlocks(  )
    {
        dataBlocks.clear(  );
    }

    @Override
    public UserDataResponse clone(  )
                           throws CloneNotSupportedException
    {
        UserDataResponse result = (UserDataResponse) super.clone(  );
        result.dataBlocks = new ArrayList<DataBlock>(  );
        result.dataBlocks.addAll( dataBlocks );

        return result;
    }

    public short getAccessNumber(  )
    {
        return accessNumber;
    }

    /**
     * @return the address
     */
    @Override
    public byte getAddress(  )
    {
        return address;
    }

    @Override
    public ControlCode getControlCode(  )
    {
        return ControlCode.RSP_UD;
    }

    /**
     * @param i
     * @return the dataBlocks
     */
    public DataBlock getDataBlock( int i )
    {
        return dataBlocks.get( i );
    }

    public int getDataBlockCount(  )
    {
        return dataBlocks.size(  );
    }

    public int getIdentNumber(  )
    {
        return identNumber;
    }

    @Override
    public DataBlock getLastDataBlock(  )
    {
        return ( dataBlocks.size(  ) == 0 ) ? null : dataBlocks.get( dataBlocks.size(  ) - 1 );
    }

    public String getManufacturer(  )
    {
        return manufacturer;
    }

    public MBusMedium getMedium(  )
    {
        return medium;
    }

    public short getSignature(  )
    {
        return signature;
    }

    public StatusCode[] getStatus(  )
    {
        return status;
    }

    public byte getVersion(  )
    {
        return version;
    }

    /**
     * @return the acd
     */
    public boolean isAcd(  )
    {
        return acd;
    }

    /**
     * @return the dfc
     */
    public boolean isDfc(  )
    {
        return dfc;
    }

    /**
     * Indicates wheter there are more Packages to follow or no.
     * @return
     */
    public boolean isLastPackage(  )
    {
        return ( getLastDataBlock(  ) == null ) ? true
                                                : ( 
                                                    ! DataFieldCode.SPECIAL_FUNCTION_MAN_SPEC_DATA_PACKETS_FOLLOWS.equals( getLastDataBlock(  )
                                                                                                                               .getDataFieldCode(  ) )
                                                 );
    }

    @Override
    public Iterator<DataBlock> iterator(  )
    {
        return dataBlocks.iterator(  );
    }

    @Override
    public void replaceDataBlock( DataBlock oldDataBlock, DataBlock newDataBlock )
    {
        final int pos = dataBlocks.indexOf( oldDataBlock );
        dataBlocks.remove( pos );
        dataBlocks.add( pos, newDataBlock );
    }

    public void setAccessNumber( short accessNumber )
    {
        this.accessNumber = accessNumber;
    }

    /**
     * @param acd the acd to set
     */
    public void setAcd( boolean acd )
    {
        this.acd = acd;
    }

    /**
     * @param address the address to set
     */
    @Override
    public void setAddress( byte address )
    {
        this.address = address;
    }

    /**
     * @param dcf the dfc to set
     */
    public void setDfc( boolean dfc )
    {
        this.dfc = dfc;
    }

    public void setIdentNumber( int identNumber )
    {
        this.identNumber = identNumber;
    }

    public void setManufacturer( String manufacturer )
    {
        this.manufacturer = manufacturer;
    }

    public void setMedium( MBusMedium medium )
    {
        this.medium = medium;
    }

    public void setSignature( short signature )
    {
        this.signature = signature;
    }

    public void setStatus( StatusCode[] status )
    {
        this.status = status;
    }

    public void setVersion( byte version )
    {
        this.version = version;
    }

    @Override
    public String toString(  )
    {
        StringBuilder sb = new StringBuilder(  );
        sb.append( "control code = " ).append( getControlCode(  ) ).append( '\n' );
        sb.append( "isAcd = " ).append( isAcd(  ) ).append( '\n' );
        sb.append( "isDfc = " ).append( isDfc(  ) ).append( '\n' );
        sb.append( String.format( "address = 0x%02X\n", address ) );
        sb.append( String.format( "ident number = %08d\n", identNumber ) );
        sb.append( "manufacturer = " ).append( manufacturer ).append( '\n' );
        sb.append( String.format( "version = 0x%02X\n", version ) );
        sb.append( "accessnumber = " ).append( accessNumber ).append( '\n' );
        sb.append( String.format( "status = %s\n",
                                  Arrays.toString( status ) ) );
        sb.append( String.format( "signature = 0x%04X\n", signature ) );
        sb.append( "medium = " ).append( medium.toString(  ) ).append( '\n' );
        sb.append( "lastPackage = " ).append( isLastPackage(  ) ).append( '\n' );

        for ( int i = 0; i < dataBlocks.size(  ); i++ )
        {
            sb.append( String.format( "datablock[%d]:\n", i ) );
            dataBlocks.get( i ).toString( sb, "  " );
        }

        return sb.toString(  );
    }
}
