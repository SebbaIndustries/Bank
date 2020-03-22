package me.sebbaindustries.bank.local;


import me.sebbaindustries.bank.Core;
import org.bukkit.entity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BankData {

    private Player p;

    public BankData(Player p) {
        this.p = p;
    }

    public boolean doesBankExists() {
        return getBankAcc() != null;
    }

    public void createBankAccount() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
            Document document = domBuilder.parse(Core.gCore.fileManager.bankDataXML);
            Element root = document.getDocumentElement();

            Element newServer = document.createElement("bank");
            newServer.setTextContent("0");
            newServer.setAttribute("id", p.getName().toLowerCase());
            root.appendChild(newServer);

            // Save XML to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(document),
                    new StreamResult(Core.gCore.fileManager.bankDataXML));
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public void modifyBank(int amount) {
        int current = getBankAcc();
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
            Document document = domBuilder.parse(Core.gCore.fileManager.bankDataXML);

            // Modify DOM tree (simple version)
            NodeList rowNodes = document.getElementsByTagName("bank");
            for (int i = 0; i < rowNodes.getLength(); i++) {
                Node rowNode = rowNodes.item(i);
                if (rowNode.getAttributes().getNamedItem("id").getNodeValue().equalsIgnoreCase(p.getName().toLowerCase())) {
                    rowNode.setTextContent(String.valueOf(current + (amount)));
                }
            }

            // Save XML to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(document),
                    new StreamResult(Core.gCore.fileManager.bankDataXML));
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public Integer getBankAcc() {
        try {
            XMLInputFactory iFactory = XMLInputFactory.newInstance();
            XMLStreamReader sReader = iFactory.createXMLStreamReader(new FileReader(Core.gCore.fileManager.bankDataXML));
            while (sReader.hasNext()) {
                sReader.next();
                if (sReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                    if (sReader.getLocalName().equalsIgnoreCase("bank")) {
                        if (sReader.getAttributeCount() > 0) {
                            String id = sReader.getAttributeValue(null, "id");
                            if (id.equalsIgnoreCase(p.getName())) {
                                Integer returnTemp = Integer.parseInt(sReader.getElementText());
                                return returnTemp;
                            }
                        }
                    }
                }
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
