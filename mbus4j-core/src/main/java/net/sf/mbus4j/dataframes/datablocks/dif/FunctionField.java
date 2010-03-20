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
package net.sf.mbus4j.dataframes.datablocks.dif;


/**
 *
 * @author arnep@users.sourceforge.net
 * @version $Id$
 */
public enum FunctionField
{INSTANTANEOUS_VALUE( 0x00, "Instantaneous value" ),
    MAXIMUM_VALUE( 0x10, "Maximum value" ),
    MINIMUM_VALUE( 0x20, "Minimum value" ),
    VALUE_DURING_ERROR_STATE( 0x30, "Value during error state" );

    private final String label;
    public final byte code;

    private FunctionField( int code, String label )
    {
        this.label = label;
        this.code = (byte) code;
    }

    @Override
    public String toString(  )
    {
        return label;
    }

    /**
     * @return the label
     */
    public String getLabel(  )
    {
        return label;
    }

    public static FunctionField fromLabel( String label )
    {
        for ( FunctionField value : values(  ) )
        {
            if ( value.getLabel(  ).equals( label ) )
            {
                return value;
            }
        }

        return valueOf( label );
    }
}
