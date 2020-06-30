/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prx.utils;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Gia Bảo Hoàng
 */
public class XMLUtils {

    public static Document parseStringToDOM(String content) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringReader reader = new StringReader(content);
        InputSource is = new InputSource(reader);
        Document doc = builder.parse(is);
        return doc;
    }

    public static XPath getXPath() {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        return xpath;
    }

    public static void TransformDOMToFile(Node node, String filePath) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        Source src = new DOMSource(node);
        Result result = new StreamResult(filePath);
        transformer.transform(src, result);
    }

    //JAXB
    //file: schema/ dtd
    public static void generateClass(String packageName, File file, String outputPath) throws SAXException, IOException {
        SchemaCompiler sc = XJC.createSchemaCompiler();
        sc.forcePackageName(packageName);
        InputSource is = new InputSource(file.toURI().toString());
        sc.parseSchema(is);
        S2JJAXBModel model = sc.bind();
        JCodeModel code = model.generateCode(null, null);
        code.build(new File(outputPath));
    }

    public static <T> T unmarshall(Class<T> clazz, File xmlFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return clazz.cast(unmarshaller.unmarshal(xmlFile));
    }

    public static <T> void marshall(T obj, File xmlFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(obj, xmlFile);
    }

    //TrAX 
    public static void transformXML(String xslPath, String xmlPath, String outputPath)
            throws IOException, TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        StreamSource xslFile = new StreamSource(xslPath);
        Templates template = transformerFactory.newTemplates(xslFile);
        Transformer transformer = template.newTransformer();
        StreamSource xmlFile = new StreamSource(xmlPath);
        StreamResult output = new StreamResult(new FileOutputStream(outputPath));
        transformer.transform(xmlFile, output);
    }

    public static String transformFromString(String xslPath, String content)
            throws IOException, TransformerConfigurationException, TransformerException {
        StringReader reader = new StringReader(content);
        StringWriter writer = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        StreamSource xslFile = new StreamSource(xslPath);
        Templates template = transformerFactory.newTemplates(xslFile);
        Transformer transformer = template.newTransformer();
        StreamSource source = new StreamSource(reader);
        StreamResult output = new StreamResult(writer);
        transformer.transform(source, output);
        return writer.toString();
    }

    public static boolean isXMLValidate(String xsdPath, String xmlPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (SAXException | IOException e) {
            return false;
        }
        return true;
    }
}
