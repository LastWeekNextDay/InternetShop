package lt.viko.eif.nlavkart.internetshop.util;

import lt.viko.eif.nlavkart.internetshop.util.container.BooleanStringPair;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;

class SchemaUtilTest {

    @Test
    void validateAgainstDTD() {
        // Assign
        String dtdPath = "src\\main\\java\\lt\\viko\\eif\\nlavkart\\internetshop\\dtd\\account.dtd";
        String xmlPath = "src\\test\\java\\account.xml";
        boolean validated;

        // Act
        try {
            File dtdFile = new File(dtdPath);
            String dtdContent = new String(java.nio.file.Files.readAllBytes(dtdFile.toPath()));

            File tempFile = new File("src\\test\\java\\lt\\viko\\eif\\nlavkart\\internetshop\\util\\tempdtd.xml");
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
            validated = true;
        } catch (IOException | SAXException e) {
            validated = false;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        // Assert
        assertTrue(validated);
    }

    @Test
    void validateAgainstXSD() {
        // Assign
        String xsdPath = "src\\main\\java\\lt\\viko\\eif\\nlavkart\\internetshop\\xsd\\account.xsd";
        String xmlPath = "src\\test\\java\\account.xml";
        boolean validated;

        // Act
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            validated = true;
        } catch (SAXException | IOException e) {
            validated = false;
        }

        // Assert
        assertTrue(validated);
    }
}