package com.nxt.im.server;

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

    if (qq.length() != 7) {
      assertTrue(false);
      return;
    }

    String qq2 = Message.getRandom();

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
