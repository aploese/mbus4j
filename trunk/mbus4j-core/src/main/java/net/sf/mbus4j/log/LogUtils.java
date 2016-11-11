package net.sf.mbus4j.log;

/*
 * #%L
 * mbus4j-core
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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author aploese
 */
public class LogUtils {

    final static public String LOG_PREFIX = "net.sf.mbus4j.log";

    final static public Logger getCoreLogger() {
        return Logger.getLogger(LOG_PREFIX + ".core.Core");
    }

    final static public Logger getEncoderLogger() {
        return Logger.getLogger(LOG_PREFIX + ".core.Encoder");
    }

    final static public Logger getDecoderLogger() {
        return Logger.getLogger(LOG_PREFIX + ".core.Decoder");
    }

    public static Logger getMasterLogger() {
        return Logger.getLogger(LOG_PREFIX + ".master.Master");
    }

    public static Logger getSlaveLogger() {
        return Logger.getLogger(LOG_PREFIX + ".slave.Slave");
    }

    public static void initJulFromResource(String loggingproperties) {
        try {
            LogManager.getLogManager().readConfiguration(LogUtils.class.getClassLoader().getResourceAsStream(loggingproperties));
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Can't read new logger configuration", e);
        }
    }

    public static void initJul() {
        try {
            LogManager.getLogManager().readConfiguration();
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Can't read default logger configuration", e);
        }
    }

}
