package lt.viko.eif.nlavkart.internetshop.util;

import lt.viko.eif.nlavkart.internetshop.util.container.BooleanStringPair;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * SchemaUtil class.
 */
public class SchemaUtil {

    /**
     * Private No-Args constructor to prevent class creation.
     */
    private SchemaUtil() {
    }

    /**
     * Validates XML against DTD.
     *
     * @param xmlPath path to XML file
     * @param dtdPath path to DTD file
     * @return BooleanStringPair with boolean value and name of DTD file
     */

    public static BooleanStringPair validateAgainstDTD(String xmlPath, String dtdPath) {
        try {
            File dtdFile = new File(dtdPath);
            String dtdContent = new String(java.nio.file.Files.readAllBytes(dtdFile.toPath()));

            File tempFile = new File("src\\main\\java\\lt\\viko\\eif\\nlavkart\\internetshop\\util\\tempdtd.xml");
            if (!tempFile.exists()) {
                tempFile.createNewFile();
            } else {
                tempFile.delete();
                tempFile.createNewFile();
            }

            File xmlFile = new File(xmlPath);
            String xmlContent = new String(java.nio.file.Files.readAllBytes(xmlFile.toPath()));
            int firstLineBreak = xmlContent.indexOf("\n") + 1;
            String xmlContent2ndHalf;
            String xmlContent1stHalf;

            xmlContent1stHalf = xmlContent.substring(0, firstLineBreak);
            xmlContent1stHalf += dtdContent + "\n";
            xmlContent2ndHalf = xmlContent.substring(firstLineBreak);
            xmlContent = xmlContent1stHalf + xmlContent2ndHalf;

            FileWriter writer = new FileWriter(tempFile);
            writer.write(xmlContent);
            writer.close();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                }
            });
            builder.parse(tempFile);

            tempFile.delete();
            return new BooleanStringPair(true, new File(dtdPath).getName().substring(0, new File(dtdPath).getName().indexOf(".")));
        } catch (IOException | SAXException e) {
            return new BooleanStringPair(false, "");
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates XML against XSD.
     *
     * @param xmlPath path to XML file
     * @param xsdPath path to XSD file
     * @return BooleanStringPair with boolean value and name of XSD file
     */
    public static BooleanStringPair validateAgainstXSD(String xmlPath, String xsdPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            return new BooleanStringPair(true, new File(xsdPath).getName().substring(0, new File(xsdPath).getName().indexOf(".")));
        } catch (SAXException | IOException e) {
            return new BooleanStringPair(false, null);
        }
    }
}
