/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wellformer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Eden
 */
public class TextUtilsTest {
    
    public TextUtilsTest() {
    }

    /**
     * Test of refineHtml method, of class TextUtils.
     */
    @Test
    public void testRefineHtml() {
        
        System.out.println("refineHtml");
        String src = "";
        String expResult = "";
        String result = TextUtils.refineHtml(src);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBody method, of class TextUtils.
     */
    @Test
    public void testGetBody() {
        
    }

    /**
     * Test of removeMiscellaneousTags method, of class TextUtils.
     */
    @Test
    public void testRemoveMiscellaneousTags() {
        
    }
    
}
