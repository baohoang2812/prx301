/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wellformer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import prx.utils.HttpUtils;
import prx.utils.TextUtils;

/**
 *
 * @author Eden
 */
public class Wellformer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<String> urlList = new ArrayList();
        urlList.add("https://www.coursera.org/browse/computer-science");
        String pageContent = null;
        try {
            for (String page : urlList) {
                pageContent = HttpUtils.getContent(page);
                // test output
                // [BHG] TODO Delete on finish project
                PrintWriter pw = new PrintWriter("rawPageContent.html");
                pw.print(pageContent);
                pw.close();
                pageContent = TextUtils.refineHtml(pageContent);
                pw = new PrintWriter("refinedPageContent.html");
                pw.print(pageContent);
                pw.close();
                boolean isWellformed = checkWellformXML(pageContent);
                if(isWellformed){
                    System.out.println(page + " đã well-form");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkWellformXML(String src) {
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
