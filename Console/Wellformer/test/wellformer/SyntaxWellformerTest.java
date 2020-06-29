/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wellformer;

import prx.wellformer.SyntaxWellformer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Eden
 */
public class SyntaxWellformerTest {

    public SyntaxWellformerTest() {
    }

    /**
     * Test of check method, of class SyntaxWellformer.
     */
    @Test
    public void testCheck() {
        System.out.println("check");
        SyntaxWellformer wellformer = new SyntaxWellformer();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Attribute không value", "<h1 checked> YEAH</h1>");
        map.put("Value không bọc trong cặp nháy", "<h1 aa= aa> <img a=a />YEAH</h1>");
        map.put("Attribute dính liền nhau", "<h1 a=\"1\"b='2'c=3>YEAH</h1>");
        map.put("Empty element", "<h1><img src=\"\"><br><hr/></h1>");
        map.put("Lỗi đóng mở thẻ", "<li><a>Sach moi</a></h3>");
        List expectedResult = new ArrayList();
        expectedResult.add("<h1 checked=\"true\">YEAH</h1>");
        expectedResult.add("<h1 aa=\"aa\"><img a=\"a\"/>YEAH</h1>");
        expectedResult.add("<h1 a=\"1\" b=\"2\" c=\"3\">YEAH</h1>");
        expectedResult.add("<h1><img src=\"\"/><br/><hr/></h1>");
        expectedResult.add("<li><a>Sach moi</a></li>");
        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            assertEquals(expectedResult.get(index), wellformer.check(entry.getValue()));
            index++;
        }
    }

    /**
     * Test of convertAttribute method, of class SyntaxWellformer.
     */
    @Test
    public void testConvertAttribute() {
    }

}
