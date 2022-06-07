package md.accounting.ipko;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.SneakyThrows;
import md.accounting.ipko.domain.AccountHistory;

import java.io.File;
import java.io.FileReader;

import static jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static java.lang.Boolean.TRUE;

public final class Deser
{
    public void serialize(AccountHistory accountHistory, String path) throws Exception
    {
        JAXBContext context = JAXBContext.newInstance(AccountHistory.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(JAXB_FORMATTED_OUTPUT, TRUE);
        marshaller.marshal(accountHistory, new File(path));
    }

    @SneakyThrows
    public AccountHistory deserialize(String path)
    {
        JAXBContext context = JAXBContext.newInstance(AccountHistory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object object = unmarshaller.unmarshal(new FileReader(path));
        return (AccountHistory) object;
    }
}
