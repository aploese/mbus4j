/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.mbus4j.master;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aploese
 */
public class ValueRequest {
    private List<ValueRequestPointLocator> points = new ArrayList<ValueRequestPointLocator>();

    public boolean add(ValueRequestPointLocator value) {
        return points.add(value);
    }

}
