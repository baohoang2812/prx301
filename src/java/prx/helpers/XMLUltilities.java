/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prx.helpers;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Gia Bảo Hoàng
 */
public class XMLUltilities implements Serializable {

    //JAXB
    public static void generateClass(String packageName, String schemaPath, String outputPath) throws SAXException, IOException {
        SchemaCompiler sc = XJC.createSchemaCompiler();
        sc.forcePackageName(packageName);
        File schema = new File(schemaPath);
        InputSource is = new InputSource(schema.toURI().toString());
        sc.parseSchema(is);
        S2JJAXBModel model = sc.bind();
        JCodeModel code = model.generateCode(null, null);
        code.build(new File(outputPath));
    }

    //TrAX 
    public static void transformXML(String xslPath, String xmlPath, String outputPath)
            throws IOException, TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        StreamSource xsltFile = new StreamSource(xslPath);
        Templates template = transformerFactory.newTemplates(xsltFile);
        Transformer transformer = template.newTransformer();
        StreamSource xmlFile = new StreamSource(xmlPath);
        StreamResult output = new StreamResult(new FileOutputStream(outputPath));
        transformer.transform(xmlFile, output);
    }
}
