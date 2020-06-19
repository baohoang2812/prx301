/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prx.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Gia Bảo Hoàng
 */
public class HttpUtilsTest {

    public HttpUtilsTest() {
    }

    /**
     * Test of getContent method, of class HttpUtils.
     */
    @Test
    public void testGetContent() {
        String result = null;
        try {
            result = HttpUtils.getContent("https://pibook.vn/moi-phat-hanh");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(result);
    }

}
