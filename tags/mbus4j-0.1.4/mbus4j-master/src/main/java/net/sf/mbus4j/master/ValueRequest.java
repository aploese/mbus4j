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
package net.sf.mbus4j.master;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author aploese
 */
public class ValueRequest<T>
    implements Iterable<ValueRequestPointLocator<T>>
{
    private List<ValueRequestPointLocator<T>> points = new ArrayList<ValueRequestPointLocator<T>>(  );

    public boolean add( ValueRequestPointLocator<T> value )
    {
        return points.add( value );
    }

    @Override
    public Iterator<ValueRequestPointLocator<T>> iterator(  )
    {
        return points.iterator(  );
    }
}
