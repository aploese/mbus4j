package net.sf.mbus4j.master.ui;

/*
 * #%L
 * mbus4j-master-ui
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

import java.util.logging.Logger;
import net.sf.mbus4j.master.MBusMaster;

import javax.swing.AbstractListModel;
import javax.swing.SwingUtilities;
import net.sf.mbus4j.devices.GenericDevice;
import net.sf.mbus4j.log.LogUtils;
import org.aspectj.lang.Aspects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 *
 * @author aploese
 */
public class DevicesListModel extends AbstractListModel {

    private final static Logger LOG = LogUtils.getMasterLogger();
    private MBusMaster master;

    @Override
    public int getSize() {
        return (master == null) ? 0 : master.deviceCount();
    }

    @Override
    public Object getElementAt(int index) {
        return master.getDevice(index);
    }

    public void setMaster(MBusMaster master) {
        DevicesChangeAspect.unregister(this.master, this);
        final int oldSize = getSize();
        this.master = null;

        if (oldSize > 0) {
            fireIntervalRemoved(this, 0, oldSize);
        }

        this.master = master;

        if (getSize() > 0) {
            fireIntervalAdded(this,
                    0,
                    getSize());
        }
        DevicesChangeAspect.register(this.master, this);
    }

    public MBusMaster getMaster() {
        return master;
    }

    @Aspect("pertarget(target(net.sf.mbus4j.master.MBusMaster))")
    public static class DevicesChangeAspect {

        public final static void unregister(MBusMaster master, DevicesListModel dlm) {
            if (master != null) {
                DevicesChangeAspect dca = Aspects.aspectOf(DevicesChangeAspect.class, master);
                if (dca.dlm == dlm) {
                    dca.dlm = null;
                }
            }
        }

        public final static void register(MBusMaster master, DevicesListModel dlm) {
            if (master != null) {
                Aspects.aspectOf(DevicesChangeAspect.class, master).dlm = dlm;
            }
        }
        public DevicesListModel dlm;

        @Pointcut("execution(* net.sf.mbus4j.master.MBusMaster.addDevice(..))")
        public void addDevice() {
        }

        @Pointcut("execution(* net.sf.mbus4j.master.MBusMaster.removeDevice(..))")
        public void removeDevice() {
        }

        @Pointcut("execution(* net.sf.mbus4j.master.MBusMaster.clearDevices(..))")
        public void clearDevices() {
        }

        @Around("addDevice()")
        public Object addDeviceImpl(ProceedingJoinPoint pjp) throws Throwable {
            final Object ret = pjp.proceed();
            if (dlm != null) {
                LOG.fine("Device added");
                final GenericDevice d = (GenericDevice) pjp.getArgs()[0];
                final MBusMaster m = (MBusMaster) pjp.getTarget();
                final int i = m.deviceIndexOf(d);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        dlm.fireIntervalAdded(dlm, i, i);
                    }
                });
            }
            return ret;
        }

        @Around("removeDevice()")
        public Object removeDeviceImpl(ProceedingJoinPoint pjp) throws Throwable {
            if (dlm == null) {
                return pjp.proceed();
            }
            final GenericDevice d = (GenericDevice) pjp.getArgs()[0];
            final MBusMaster m = (MBusMaster) pjp.getTarget();
            final int i = m.deviceIndexOf(d);
            final Object ret = pjp.proceed();
            LOG.fine("Device removed");
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    dlm.fireIntervalRemoved(dlm, i, i);
                }
            });
            return ret;
        }

        @Around("clearDevices()")
        public Object clearDevicesImpl(ProceedingJoinPoint pjp) throws Throwable {
            if (dlm == null) {
                return pjp.proceed();
            }
            final MBusMaster m = (MBusMaster) pjp.getTarget();
            final int i = m.deviceCount();
            final Object ret = pjp.proceed();
            LOG.fine("All devices removed");
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    dlm.fireIntervalRemoved(dlm, 0, i);
                }
            });
            return ret;
        }
    }
}
