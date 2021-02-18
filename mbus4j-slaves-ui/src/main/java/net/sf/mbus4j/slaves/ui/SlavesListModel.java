package net.sf.mbus4j.slaves.ui;

/*
 * #%L
 * mbus4j-slaves-ui
 * %%
 * Copyright (C) 2009 - 2014 MBus4J
 * %%
 * mbus4j - Drivers for the M-Bus protocol - http://mbus4j.sourceforge.net/
 * Copyright (C) 2009-2014, mbus4j.sf.net, and individual contributors as indicated
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
 * #L%
 */

import net.sf.mbus4j.slaves.Slaves;

import javax.swing.AbstractListModel;
import net.sf.mbus4j.slaves.Slave;
import org.aspectj.lang.Aspects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 *
 * @author Arne Plöse
 */
class SlavesListModel
        extends AbstractListModel {

    private Slaves slaves;

    @Override
    public int getSize() {
        return (slaves == null) ? 0 : slaves.getSalvesSize();
    }

    @Override
    public Object getElementAt(int index) {
        return slaves.getSlave(index);
    }

    public void setSlaves(Slaves slaves) {

        SlavesChangeAspect.unregister(this.slaves, this);

        final int oldSize = getSize();
        this.slaves = null;

        if (oldSize > 0) {
            fireIntervalRemoved(this, 0, oldSize);
        }

        this.slaves = slaves;

        if (getSize() > 0) {
            fireIntervalAdded(this,
                    0,
                    getSize());
        }

        SlavesChangeAspect.register(this.slaves, this);
    }

    public Slaves getSlaves() {
        return slaves;
    }

    @Aspect("pertarget(target(net.sf.mbus4j.slaves.Slaves))")
    public static class SlavesChangeAspect {

        public final static void unregister(Slaves slaves, SlavesListModel slm) {
            if (slaves != null) {
                SlavesChangeAspect sca = Aspects.aspectOf(SlavesChangeAspect.class, slaves);
                if (sca.slm == slm) {
                    sca.slm = null;
                }
            }
        }

        public final static void register(Slaves slaves, SlavesListModel slm) {
            if (slaves != null) {
                Aspects.aspectOf(SlavesChangeAspect.class, slaves).slm = slm;
            }
        }

        public SlavesListModel slm;

        @Pointcut("execution(* net.sf.mbus4j.slaves.Slaves.addSlave(..))")
        public void addSlave() {
        }

        @Pointcut("execution(* net.sf.mbus4j.slaves.Slaves.removeSlave(..))")
        public void removeSlave() {
        }

        @Around("addSlave()")
        public Object addSlaveImpl(ProceedingJoinPoint pjp) throws Throwable {
            Object ret = pjp.proceed();
            if (slm != null) {
                final Slave s = (Slave) pjp.getArgs()[0];
                final Slaves sl = (Slaves) pjp.getTarget();
                slm.fireIntervalAdded(slm, sl.slaveIndexOf(s), sl.slaveIndexOf(s));
            }
            return ret;
        }

        @Around("removeSlave()")
        public Object removeSlaveImpl(ProceedingJoinPoint pjp) throws Throwable {
            if (slm == null) {
                return pjp.proceed();
            }
            final Slave s = (Slave) pjp.getArgs()[0];
            final Slaves sl = (Slaves) pjp.getTarget();
            final int i = sl.slaveIndexOf(s);
            final Object ret = pjp.proceed();
            slm.fireIntervalRemoved(slm, i, i);
            return ret;
        }

    }
}
