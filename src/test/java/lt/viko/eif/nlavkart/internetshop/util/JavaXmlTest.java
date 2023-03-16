package lt.viko.eif.nlavkart.internetshop.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lt.viko.eif.nlavkart.internetshop.models.Account;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class JavaXmlTest {

    @Test
    void createMarshaller() {
        /// Assign
        Marshaller marshaller;
        Class<?> marshalledClass = Account.class;

        /// Act
        try {
            marshaller = JAXBContext.newInstance(marshalledClass).createMarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        /// Assert
        Assert.isTrue(marshaller != null, "Marshaller is null");
        Assert.isInstanceOf(Marshaller.class, marshaller, "Marshaller is not Marshaller class");
    }

    @Test
    void createUnmarshaller() {
        /// Assign
        Unmarshaller unmarshaller;
        Class<?> marshalledClass = Account.class;

        /// Act
        try {
            unmarshaller = JAXBContext.newInstance(marshalledClass).createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        /// Assert
        Assert.isTrue(unmarshaller != null, "Unmarshaller is null");
        Assert.isInstanceOf(Unmarshaller.class, unmarshaller, "Unmarshaller is not Unmarshaller class");
    }

    @Test
    void save() {
        /// Assign
        String path = "src\\test\\java\\lt\\viko\\eif\\nlavkart\\internetshop\\services\\testSave.xml";
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<account id=\"1\">\n" +
                "    <username>test</username>\n" +
                "    <password>test</password>\n" +
                "</account>\n";
        Class<?> marshalledClass = Account.class;
        Account account = new Account();
        account.setId(1);
        account.setUsername("test");
        account.setPassword("test");
        Marshaller marshaller;
        try {
            marshaller = JAXBContext.newInstance(marshalledClass).createMarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        /// Act
        try {
            FileWriter fileWriter = new FileWriter(path);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(account, fileWriter);
            fileWriter.close();
        } catch (IOException | JAXBException e) {
            throw new RuntimeException(e);
        }

        /// Assert
        String fileContent;
        try {
             fileContent = Files.readString(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.isTrue(fileContent.equals(content), "File content is not equal to expected content");
    }

    @Test
    void load() {
        /// Assign
        String path = "src\\test\\java\\lt\\viko\\eif\\nlavkart\\internetshop\\services\\testLoad.xml";
        Account account = new Account();
        account.setId(1);
        account.setUsername("test");
        account.setPassword("test");
        Class<?> marshalledClass = Account.class;
        Unmarshaller unmarshaller;
        try {
            unmarshaller = JAXBContext.newInstance(marshalledClass).createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        /// Act
        Account account1;
        try{
            Object object = unmarshaller.unmarshal(new File(path));
            account1 = (Account) object;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        /// Assert
        Assert.isTrue(account1.getId() == account.getId(),
                "Account id is not equal to expected id");
        Assert.isTrue(account1.getUsername().equals(account.getUsername()),
                "Account username is not equal to expected username");
        Assert.isTrue(account1.getPassword().equals(account.getPassword()),
                "Account password is not equal to expected password");
    }
}