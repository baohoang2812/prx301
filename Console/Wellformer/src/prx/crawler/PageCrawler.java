/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prx.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import prx.utils.HttpUtils;
import prx.utils.TextUtils;
import prx.utils.XMLUtils;

/**
 *
 * @author Gia Bảo Hoàng
 */
public class PageCrawler {

    private String baseURL;
    private int pageCount;
    private String navigationPath;
    private Set<String> pageDetailLinkSet;
    private String expression;

    public PageCrawler() {
        this.pageDetailLinkSet = new HashSet();
    }

    public PageCrawler(String baseURL, int pageCount, String navigationPath, Set<String> pageDetailLinkSet, String expression) {
        this.baseURL = baseURL;
        this.pageCount = pageCount;
        this.navigationPath = navigationPath;
        this.pageDetailLinkSet = new HashSet();
        this.expression = expression;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public Set<String> getPageDetailLinkSet() {
        return pageDetailLinkSet;
    }

    public void setPageDetailLinkSet(Set<String> pageDetailLinkSet) {
        this.pageDetailLinkSet = pageDetailLinkSet;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getNavigationPath() {
        return navigationPath;
    }

    public void setNavigationPath(String navigationPath) {
        this.navigationPath = navigationPath;
    }

    public String getBaseUrl() {
        return baseURL;
    }

    public String getUrl() {
        return baseURL;
    }

    public void setUrl(String url) {
        this.baseURL = url;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void crawl() throws IOException {
        System.out.println("Crawling Page Detail Link... ");
        parsePageDetail();
        System.out.println("Finished Get Page Detail Links.");
    }

    private String preprocessPageContent(String url) throws IOException {
        String pageContent = null;
        pageContent = HttpUtils.getContent(url);
        return TextUtils.refineHtml(pageContent);
    }

    private String constructLink(int pageIndex) {
        return baseURL + navigationPath + pageIndex;
    }

    private void parseAllPageCount() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        for (int pageIndex = 0; pageIndex <= pageCount; pageIndex++) {
            String pageContent = preprocessPageContent(constructLink(pageIndex));
            Document doc = XMLUtils.parseStringToDOM(pageContent);
            retrieveDetailLinks(doc, this.expression);
        }
    }

    private void parsePageDetail() {
        System.out.println("Parsing Page " + baseURL + " Detail");
        for (String link : pageDetailLinkSet) {
            try {

                String content = preprocessPageContent(link);
                // TODO apply xslt from HTML to XML
            } catch (IOException e) {
                System.out.println("!!! Parsing Page Detail ERROR !!!");
                System.out.println("Page: " + link);
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // TODO change Name
    // Get all links in page
    private void retrieveDetailLinks(Document doc, String expression) throws XPathExpressionException {
        XPath xpath = XMLUtils.getXPath();
        // list of attribute nodes
        NodeList linkList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
        int i = 0;
        while (i < linkList.getLength()) {
            // TODO check for position
            pageDetailLinkSet.add(linkList.item(i).getNodeValue());
            i++;
        }
    }

    //[BHG] TODO consider remove
    public boolean checkWellformXML(String src) {
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
        // TODO[BHG, refactor ErrorHandler]
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
