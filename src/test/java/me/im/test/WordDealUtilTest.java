package me.im.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WordDealUtilTest {
    private String expected;
    private String target;

    @Parameters
    public static Collection words() {
        return Arrays.asList(new Object[][]{
            {"employee_info", "employeeInfo"},// 测试一般的处理情况
            {null, null},// 测试 null 时的处理情况
            {"", ""},// 测试空字符串时的处理情况
            {"employee_info", "EmployeeInfo"},// 测试当首字母大写时的情况
            {"employee_info_a", "employeeInfoA"},// 测试当尾字母为大写时的情况
            {"employee_a_info", "employeeAInfo"}// 测试多个相连字母大写时的情况
        });
    }

    /**
     * 参数化测试必须的构造函数
     *
     * @param expected     期望的测试结果，对应参数集中的第一个参数
     * @param target       测试数据，对应参数集中的第二个参数  
     */
    public WordDealUtilTest(String expected, String target) {
        this.expected = expected;
        this.target = target;
    }

    /**
     * 测试将 Java 对象名称到数据库名称的转换
     */
    @Test
    public void wordFormat4DB() {
        assertEquals(expected, WordDealUtil.wordFormat4DB(target));
    }
}

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//
//import org.junit.Test;
//
//public class WordDealUtilTest {
//
//    // 测试 wordFormat4DB 正常运行的情况
//    @Test
//    public void testWordFormat4DBNormal() {
//        String target = "employeeInfo";
//        String result = WordDealUtil.wordFormat4DB(target);
//
//        assertEquals("employee_info", result);
//    }
//
//    // 测试 null 时的处理情况
//    @Test
//    public void testWordFormat4DBNull() {
//        String target = null;
//        String result = WordDealUtil.wordFormat4DB(target);
//
//        assertNull(result);
//    }
//
//    // 测试空字符串的处理情况
//    @Test
//    public void wordFormat4DBEmpty() {
//        String target = "";
//        String result = WordDealUtil.wordFormat4DB(target);
//
//        assertEquals("", result);
//    }
//
//    // 测试当首字母大写时的情况
//    @Test
//    public void wordFormat4DBegin() {
//        String target = "EmployeeInfo";
//        String result = WordDealUtil.wordFormat4DB(target);
//
//        assertEquals("employee_info", result);
//    }
//
//    // 测试当尾字母为大写时的情况
//    @Test
//    public void wordFormat4DBEnd() {
//        String target = "employeeInfoA";
//        String result = WordDealUtil.wordFormat4DB(target);
//
//        assertEquals("employee_info_a", result);
//    }
//
//    // 测试多个相连字母大写时的情况
//    @Test
//    public void wordFormat4DBTogether() {
//        String target = "employeeAInfo";
//        String result = WordDealUtil.wordFormat4DB(target);
//
//        assertEquals("employee_a_info", result);
//    }
//}
