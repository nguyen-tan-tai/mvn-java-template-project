package caphe.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XmlParser {
    public static void main(String args[]) {
        try {
            new XmlParser().parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse() throws FileNotFoundException, XMLStreamException {
        String filename = "C:/Users/tanta/Desktop/ACCOUNT_REPORT_20200115.xml";
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(javax.xml.stream.XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        XMLStreamReader xmlr = factory.createFilteredReader(
                factory.createXMLStreamReader(filename, new FileInputStream(filename)), new MyStreamFilter());
        XMLEventReader eventReader = factory.createXMLEventReader(xmlr);
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                StartElement startElement = event.asStartElement();
                if (!"row".equals(startElement.getName().getLocalPart())) {
                    continue;
                }
                System.out.println(startElement.toString());
            }
        }
    }

    public class MyStreamFilter implements javax.xml.stream.StreamFilter {
        public boolean accept(XMLStreamReader reader) {
            if (!reader.isStartElement() && !reader.isEndElement()) {
                return false;
            } else {
                return true;
            }
        }
    }
}
