package com.alatai.mini.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Iterator;

/**
 * 读取 XML 文件配置
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/18 20:39
 */
public class ClassPathXmlResource implements Resource {

    private final Document document;
    private final Element rootElement;
    private final Iterator<Element> elementIterator;

    public ClassPathXmlResource(String filename) {
        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(filename);
        try {
            this.document = saxReader.read(xmlPath);
            this.rootElement = document.getRootElement();
            this.elementIterator = this.rootElement.elementIterator();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
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
