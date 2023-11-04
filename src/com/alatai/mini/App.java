package com.alatai.mini;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 * @author saihou
 * @version 1.0
 * @date 2023/10/30 21:49
 */
public class App {

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        String webappDirLocation = "WebContent";
        StandardContext context = (StandardContext) tomcat.addWebapp("/",
                new File(webappDirLocation).getAbsolutePath());
        Connector connector = new Connector();
        connector.setPort(8080);
        tomcat.setConnector(connector);
        tomcat.start();
        tomcat.getServer().await();
    }
}
