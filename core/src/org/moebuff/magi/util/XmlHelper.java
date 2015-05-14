package org.moebuff.magi.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

/**
 * XML助手
 *
 * @author MuTo
 */
public class XmlHelper {
    private String path;
    private Document document;
    private Element root;

    public XmlHelper(String path) {
        this.path = path;

        document = getDocument(this.path);
        root = document.getRootElement();
    }

    public static Document getDocument(String path) {
        try {
            return new SAXReader().read(path);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static Element getRoot(String path) {
        return getDocument(path).getRootElement();
    }

    public List getRootElements(String name) {
        return root.elements(name);
    }

    // Properties
    // -------------------------------------------------------------------------

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Element getRoot() {
        return root;
    }

    public void setRoot(Element root) {
        this.root = root;
    }
}
