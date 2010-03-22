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

import java.io.Serializable;

/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public interface MBusResponseFramesContainer
    extends Iterable<ResponseFrameContainer>,
            Serializable
{
    void setAddress( byte address );

    void setIdentNumber( int identNumber );

    void setVersion( byte version );

    boolean isAcd(  );

    void setAcd( boolean acd );

    boolean isDfc(  );

    void setDfc( boolean dfc );

    String getManufacturer(  );

    void setManufacturer( String manufacturer );

    MBusMedium getMedium(  );

    void setMedium( MBusMedium medium );

    byte getAddress(  );

    int getIdentNumber(  );

    byte getVersion(  );

    UserDataResponse.StatusCode[] getStatus(  );

    void setStatus( UserDataResponse.StatusCode[] status );

    short getSignature(  );

    void setSignature( short signature );

    short getAccessnumber(  );

    void setAccessnumber( short accessnumber );

    ResponseFrameContainer getResponseFrameContainer( int index );

    ResponseFrameContainer[] getResponseFrameContainers(  );

    int getResponseFrameContainerCount(  );
}
