/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.master;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author aploese
 */
public class ValueRequest<T> implements Iterable<ValueRequestPointLocator<T>>{
    private List<ValueRequestPointLocator<T>> points = new ArrayList<ValueRequestPointLocator<T>>();

    public boolean add(ValueRequestPointLocator<T> value) {
        return points.add(value);
    }

    @Override
    public Iterator<ValueRequestPointLocator<T>> iterator() {
        return points.iterator();
    }

}
