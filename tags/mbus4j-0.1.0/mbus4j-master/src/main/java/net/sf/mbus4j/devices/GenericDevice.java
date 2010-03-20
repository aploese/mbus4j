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
package net.sf.mbus4j.devices;

import net.sf.mbus4j.dataframes.Frame;
import net.sf.mbus4j.dataframes.MBusMedium;
import net.sf.mbus4j.dataframes.MBusResponseFramesContainer;
import net.sf.mbus4j.dataframes.ResponseFrameContainer;
import net.sf.mbus4j.dataframes.UserDataResponse;
import net.sf.mbus4j.dataframes.UserDataResponse.StatusCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public class GenericDevice
    implements MBusResponseFramesContainer
{
    private byte address;
    private List<ResponseFrameContainer> responseFrameContainers = new ArrayList<ResponseFrameContainer>(  );
    private int identNumber;
    private byte version;
    private boolean acd;
    private boolean dfc;
    private StatusCode[] status;
    private short accessnumber;
    private short signature;
    private String manufacturer;
    private MBusMedium medium;

    @Override
    public void setAddress( byte address )
    {
        this.address = address;
    }

    @Override
    public void setIdentNumber( int identNumber )
    {
        this.identNumber = identNumber;
    }

    @Override
    public void setVersion( byte version )
    {
        this.version = version;
    }

    @Override
    public boolean isAcd(  )
    {
        return acd;
    }

    @Override
    public void setAcd( boolean acd )
    {
        this.acd = acd;
    }

    @Override
    public boolean isDfc(  )
    {
        return dfc;
    }

    @Override
    public void setDfc( boolean dfc )
    {
        this.dfc = dfc;
    }

    @Override
    public void setManufacturer( String manufacturer )
    {
        this.manufacturer = manufacturer;
    }

    @Override
    public void setMedium( MBusMedium medium )
    {
        this.medium = medium;
    }

    @Override
    public byte getAddress(  )
    {
        return address;
    }

    @Override
    public int getIdentNumber(  )
    {
        return identNumber;
    }

    @Override
    public byte getVersion(  )
    {
        return version;
    }

    @Override
    public Iterator<ResponseFrameContainer> iterator(  )
    {
        return responseFrameContainers.iterator(  );
    }

    @Override
    public StatusCode[] getStatus(  )
    {
        return status;
    }

    @Override
    public void setStatus( StatusCode[] status )
    {
        this.status = status;
    }

    @Override
    public short getSignature(  )
    {
        return signature;
    }

    @Override
    public void setSignature( short signature )
    {
        this.signature = signature;
    }

    @Override
    public short getAccessnumber(  )
    {
        return accessnumber;
    }

    @Override
    public void setAccessnumber( short accessnumber )
    {
        this.accessnumber = accessnumber;
    }

    @Override
    public ResponseFrameContainer getResponseFrameContainer( int index )
    {
        return responseFrameContainers.get( index );
    }

    @Override
    public ResponseFrameContainer[] getResponseFrameContainers(  )
    {
        return responseFrameContainers.toArray( new ResponseFrameContainer[responseFrameContainers.size(  )] );
    }

    @Override
    public int getResponseFrameContainerCount(  )
    {
        return responseFrameContainers.size(  );
    }

    public GenericDevice(  )
    {
    }

    public GenericDevice( byte address, String manufacturer, MBusMedium medium, byte version, int identNumber )
    {
        super(  );
        this.manufacturer = manufacturer;
        this.medium = medium;
        this.address = address;
        this.version = version;
        this.identNumber = identNumber;
    }

    public GenericDevice( UserDataResponse udResp, Frame requestFrame )
    {
        this( udResp.getAddress(  ),
              udResp.getManufacturer(  ),
              udResp.getMedium(  ),
              udResp.getVersion(  ),
              udResp.getIdentNumber(  ) );

        ResponseFrameContainer rfc = new ResponseFrameContainer(  );
        rfc.setResponseFrame( udResp );
        rfc.setRequestFrame( requestFrame );
        responseFrameContainers.add( rfc );
    }

    @Override
    public String getManufacturer(  )
    {
        return manufacturer;
    }

    @Override
    public MBusMedium getMedium(  )
    {
        return medium;
    }
}
