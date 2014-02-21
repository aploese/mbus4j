/* jSSC (Java Simple Serial Connector) - serial port communication library.
 * © Alexey Sokolov (scream3r), 2010-2014.
 *
 * This file is part of jSSC.
 *
 * jSSC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jSSC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSSC.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you use jSSC in public project you can inform me about this by e-mail,
 * of course if you want it.
 *
 * e-mail: scream3r.org@gmail.com
 * web-site: http://scream3r.org | http://code.google.com/p/java-simple-serial-connector/
 */
package net.sf.mbus4j.serial;

import java.io.IOException;

/**
 *
 * @author scream3r
 */
public class SerialPortException extends IOException {

    public enum Type { 
    PORT_ALREADY_OPENED("Port already opened"),
    PORT_NOT_OPENED("Port not opened"),
    NULL_NOT_PERMITTED("Null not permitted"),
    PORT_BUSY("Port busy"),
    PORT_NOT_FOUND("Port not found"),
    PERMISSION_DENIED("Permission denied"),
    INCORRECT_SERIAL_PORT("Incorrect serial port");
    
    public final String value;

        private Type(String name) {
            this.value = name;
        }
        
        @Override
        public String toString() {
            return value;
        }
    
    }
    
    private final String portName;
    private final String methodName;
    private final Type exceptionType;

    public SerialPortException(String portName, String methodName, Type exceptionType){
        super("Port name - " + portName + "; Method name - " + methodName + "; Exception type - " + exceptionType + ".");
        this.portName = portName;
        this.methodName = methodName;
        this.exceptionType = exceptionType;
    }

    /**
     * Getting port name during operation with which the exception was called
     */
    public String getPortName(){
        return portName;
    }

    /**
     * Getting method name during execution of which the exception was called
     */
    public String getMethodName(){
        return methodName;
    }

    /**
     * Getting exception type
     */
    public Type getExceptionType(){
        return exceptionType;
    }
    }
