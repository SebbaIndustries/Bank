package me.sebbaindustries.bank.utils;

import me.sebbaindustries.bank.Core;
import org.bukkit.entity.Player;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sebbaindustries
 * @version 1.0
 * @see XMLInputFactory
 * @see XMLStreamException
 * @see XMLStreamReader
 * @see FileNotFoundException
 * @see FileReader
 */
public class Messages {

    public void sendMessage(Player p, String messageName) {
        p.sendMessage(getMessage(messageName));
    }

    private String getPrefix() {
        try {
            XMLInputFactory iFactory = XMLInputFactory.newInstance();
            XMLStreamReader sReader = iFactory.createXMLStreamReader(new FileReader(Core.gCore.fileManager.messagesXML));
            while (sReader.hasNext()) {
                sReader.next();
                if (sReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                    if (sReader.getLocalName().equalsIgnoreCase("prefix")) {
                        return readPrefix(sReader);
                    }
                }
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Reads all messages inside <msg></msg> element and looks for right language
     * @param sReader Instance
     * @return String with right message or null if it's not found
     * @throws XMLStreamException Handled in public class getPrefix()
     */
    private String readPrefix(XMLStreamReader sReader) throws XMLStreamException {
        while (sReader.hasNext()) {
            //Move to next event
            sReader.next();

            //Check if its 'START_ELEMENT'
            if (sReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                //msg tag - opened
                if (sReader.getLocalName().equalsIgnoreCase("msg")) {
                    // return sReader.getElementText();
                    if (sReader.getAttributeCount() > 0) {
                        // checks if element has lang attribute
                        String id = sReader.getAttributeValue(null, "lang");

                        // if attribute is same as lang tag it returns a message
                        if (id.equalsIgnoreCase(Core.gCore.LANG)) return Color.chat(sReader.getElementText());
                    }
                }
            }
        }
        return null;
    }

    /**
     * Looks for message string in messages.xml file and returns with right language and prefix boolean
     * @param messageName Name of a message
     * @return Translated and finalised message
     */
    public String getMessage(String messageName){
        try {
            XMLInputFactory iFactory = XMLInputFactory.newInstance();
            XMLStreamReader sReader = iFactory.createXMLStreamReader(new FileReader(Core.gCore.fileManager.messagesXML));
            while (sReader.hasNext()) {
                //Move to next event
                sReader.next();

                //Check if its 'START_ELEMENT'
                if (sReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                    //message tag - opened
                    if (sReader.getLocalName().equalsIgnoreCase("message")) {

                        //Read attributes within message tag
                        if (sReader.getAttributeCount() > 0) {
                            String id = sReader.getAttributeValue(null, "name");

                            // check if id is same to message name
                            if (messageName.equalsIgnoreCase(id)) {
                                return readMsg(sReader);
                            }
                        }
                    }
                }
            }
            return null;
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads all messages inside <msg></msg> element and looks for right language
     * @param sReader Instance
     * @return String with right message or null if it's not found
     * @throws XMLStreamException Handled in public class getMessage()
     */
    private String readMsg(XMLStreamReader sReader) throws XMLStreamException {
        while (sReader.hasNext()) {
            //Move to next event
            sReader.next();

            //Check if its 'START_ELEMENT'
            if (sReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                //msg tag - opened
                if (sReader.getLocalName().equalsIgnoreCase("msg")) {
                    // return sReader.getElementText();
                    if (sReader.getAttributeCount() > 0) {
                        // checks if element has lang attribute
                        String id = sReader.getAttributeValue(null, "lang");

                        // if attribute is same as lang tag it returns a message
                        if (id.equalsIgnoreCase(Core.gCore.LANG)) return Color.chat(getPrefix() + sReader.getElementText());
                    }
                }
            }
        }
        return null;
    }

    public List<String> getHelpList(String tag){
        List<String> helpList = new ArrayList<>();
        tag = "help line " + tag;
        try {
            XMLInputFactory iFactory = XMLInputFactory.newInstance();
            XMLStreamReader sReader = iFactory.createXMLStreamReader(new FileReader(Core.gCore.fileManager.messagesXML));
            while (sReader.hasNext()) {
                //Move to next event
                sReader.next();

                //Check if its 'START_ELEMENT'
                if (sReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                    //message tag - opened
                    if (sReader.getLocalName().equalsIgnoreCase("message")) {

                        //Read attributes within message tag
                        if (sReader.getAttributeCount() > 0) {
                            String id = sReader.getAttributeValue(null, "name");

                            // check if id is same to message name
                            if (tag.equalsIgnoreCase(id)) {
                                helpList.add(readMsg(sReader));
                            }
                        }
                    }
                }
            }
            return helpList;
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
