/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prx.helpers;

import java.io.FileOutputStream;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Eden
 */
public class TrAXHelper {
    public static void transform(String xslPath,String xmlPath, String outputPath) throws Exception{
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        StreamSource xsltFile = new StreamSource(xslPath);
        Templates template = transformerFactory.newTemplates(xsltFile);
        Transformer transformer = template.newTransformer();
        StreamSource xmlFile = new StreamSource(xmlPath);
        StreamResult output = new StreamResult(new FileOutputStream(outputPath));
        transformer.transform(xmlFile, output);
    }
}
