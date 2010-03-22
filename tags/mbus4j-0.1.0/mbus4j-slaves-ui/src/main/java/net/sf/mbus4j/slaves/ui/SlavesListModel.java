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
package net.sf.mbus4j.slaves.ui;

import net.sf.mbus4j.slaves.Slaves;

import javax.swing.AbstractListModel;

/**
 *
 * @author aploese
 */
class SlavesListModel
    extends AbstractListModel
{
    private Slaves slaves;

    @Override
    public int getSize(  )
    {
        return ( slaves == null ) ? 0 : slaves.getSalvesSize(  );
    }

    @Override
    public Object getElementAt( int index )
    {
        return slaves.getSlave( index );
    }

    public void setSlaves( Slaves slaves )
    {
        final int oldSize = getSize(  );
        this.slaves = null;

        if ( oldSize > 0 )
        {
            fireIntervalRemoved( this, 0, oldSize );
        }

        this.slaves = slaves;

        if ( getSize(  ) > 0 )
        {
            fireIntervalAdded( this,
                               0,
                               getSize(  ) );
        }
    }

    public Slaves getSlaves(  )
    {
        return slaves;
    }
}
