package com.alatai.mini.web;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 加载配置文件实现
 *
 * @author saihou
 * @version 1.0
 * @date 2023/10/04 23:17
 */
public class ClassPathXmlResource implements Resource {

    private Document document;

    private Element rootElement;

    private Iterator<Element> elementIterator;

    public ClassPathXmlResource(URL xmlPath) {
        SAXReader saxReader = new SAXReader();

        try {
            this.document = saxReader.read(xmlPath);
            this.rootElement = document.getRootElement();
            this.elementIterator = this.rootElement.elementIterator();
        } catch (DocumentException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public boolean hasNext() {
        return this.elementIterator.hasNext();
    }

    @Override
    public Object next() {
        return this.elementIterator.next();
    }
}
