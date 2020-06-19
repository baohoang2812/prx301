/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wellformer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import prx.utils.TextUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import prx.utils.HttpUtils;

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

    @Test
    public void testWellformed() {
        String[] urls = {"https://pibook.vn/moi-phat-hanh", "https://www.vinabook.com/sach-moi-phat-hanh", "https://meta.vn/may-khoan-c681"};
        String pageContent = null;
        for (String url : urls) {
            try {
                pageContent = HttpUtils.getContent(url);
            } catch (IOException e) {
                // skip
            }
            pageContent = TextUtils.refineHtml(pageContent);
            boolean result = checkWellformXML(pageContent);
            if (result) {
                System.out.println(url + " đã well-form");
            }
            assertEquals(true, result);
        }
    }

    private boolean checkWellformXML(String src) {
        // use DocumentBuilder + SAX parser to check 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
            }
        });
        try {
            builder.parse(new ByteArrayInputStream(src.getBytes(StandardCharsets.UTF_8)));
            return true;
        } catch (SAXException | IOException e) {
            return false;
        }
    }

}
