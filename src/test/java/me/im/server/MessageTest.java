package me.im.server;

import me.im.server.socket.Message;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class MessageTest {
    /**
     * Rigorous Test.
     */
    @Test
    public void testGetRandom() {

        String qq = Message.getRandom();
        String qq2 = Message.getRandom();

        if (qq.length() != 7) {
            assertTrue(false);
            return;
        }

        if (qq2.length() != 7) {
            assertTrue(false);
            return;
        }

        if (qq.equals(qq2)) {
            assertTrue(false);
            return;
        }

        System.out.println(qq);
        System.out.println(qq2);

        assertTrue(true);
    }
}
