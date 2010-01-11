/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class ObjectOutputStreamTest {

    public static class Base implements Serializable {
        private String string;
        private int integer;
        private transient int intTransient;
        
    }

    public static class Base1 extends Base {

        private void writeObject(ObjectOutputStream s) throws IOException {

        }

        private void readObject(ObjectInputStream s) throws IOException {

        }

    }

    public static class Base2 extends Base {

        public static final String UTF_STR = "XXXXXXXXXXXXXXXXXXXXXXXX";

        private void writeObject(ObjectOutputStream s) throws IOException {
            s.writeUTF(UTF_STR);
        }

        private void readObject(ObjectInputStream s) throws IOException {
            assertEquals(UTF_STR, s.readUTF());
        }

    }

    public void assertBase(Base b, Base b0) {
         assertEquals(b.string, b0.string);
         assertEquals(b.integer, b0.integer);
    }

    private ObjectOutputStream oos;
    private ByteArrayOutputStream bos;

    public ObjectOutputStreamTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        bos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(bos);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testBase() throws Exception {
        System.out.println("testBase");
        Base b = new Base();
        b.string = "Hello World";
        b.integer = 42;
        b.intTransient = 44;
        oos.writeObject(b);
        oos.flush();
        byte[] buf = bos.toByteArray();
        System.out.println(bos.toString());
        System.out.println(Arrays.toString(buf));
        assertArrayEquals(new byte[]{-84, -19, 0, 5, 115, 114, 0, 27, 79, 98, 106, 101, 99, 116, 79, 117, 116, 112, 117, 116, 83, 116, 114, 101, 97, 109, 84, 101, 115, 116, 36, 66, 97, 115, 101, 26, 68, -90, 81, 77, -100, -63, 44, 2, 0, 2, 73, 0, 7, 105, 110, 116, 101, 103, 101, 114, 76, 0, 6, 115, 116, 114, 105, 110, 103, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 120, 112, 0, 0, 0, 42, 116, 0, 11, 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100}, buf);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
        Base b1 = (Base)ois.readObject();
        assertBase(b, b1);
    }


    @Test
    public void testBase1() throws Exception {
        System.out.println("testBase1");
        Base b = new Base1();
        b.string = "Hello World";
        b.integer = 42;
        b.intTransient = 44;
        oos.writeObject(b);
        oos.flush();
        byte[] buf = bos.toByteArray();
        System.out.println(bos.toString());
        System.out.println(Arrays.toString(buf));
        assertArrayEquals(new byte[] {-84, -19, 0, 5, 115, 114, 0, 28, 79, 98, 106, 101, 99, 116, 79, 117, 116, 112, 117, 116, 83, 116, 114, 101, 97, 109, 84, 101, 115, 116, 36, 66, 97, 115, 101, 49, 6, -85, -81, -30, 118, -66, -78, 107, 3, 0, 0, 120, 114, 0, 27, 79, 98, 106, 101, 99, 116, 79, 117, 116, 112, 117, 116, 83, 116, 114, 101, 97, 109, 84, 101, 115, 116, 36, 66, 97, 115, 101, 26, 68, -90, 81, 77, -100, -63, 44, 2, 0, 2, 73, 0, 7, 105, 110, 116, 101, 103, 101, 114, 76, 0, 6, 115, 116, 114, 105, 110, 103, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 120, 112, 0, 0, 0, 42, 116, 0, 11, 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 120}, buf);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
        Base1 b1 = (Base1)ois.readObject();
        assertBase(b, b1);
    }

    @Test
    public void testBase2() throws Exception {
        System.out.println("testBase2");
        Base b = new Base2();
        b.string = "Hello World";
        b.integer = 42;
        b.intTransient = 44;
        oos.writeObject(b);
        oos.flush();
        byte[] buf = bos.toByteArray();
        System.out.println(bos.toString());
        System.out.println(Arrays.toString(buf));
        assertArrayEquals(new byte[]{-84, -19, 0, 5, 115, 114, 0, 28, 79, 98, 106, 101, 99, 116, 79, 117, 116, 112, 117, 116, 83, 116, 114, 101, 97, 109, 84, 101, 115, 116, 36, 66, 97, 115, 101, 50, 21, -29, 56, 28, 71, 48, 10, -7, 3, 0, 0, 120, 114, 0, 27, 79, 98, 106, 101, 99, 116, 79, 117, 116, 112, 117, 116, 83, 116, 114, 101, 97, 109, 84, 101, 115, 116, 36, 66, 97, 115, 101, 26, 68, -90, 81, 77, -100, -63, 44, 2, 0, 2, 73, 0, 7, 105, 110, 116, 101, 103, 101, 114, 76, 0, 6, 115, 116, 114, 105, 110, 103, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 120, 112, 0, 0, 0, 42, 116, 0, 11, 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 119, 26, 0, 24, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 88, 120}, buf);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
        Base2 b1 = (Base2)ois.readObject();
        assertBase(b, b1);
    }
}