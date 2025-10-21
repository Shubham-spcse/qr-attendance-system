package com.attendance;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
    public static void main(String[] args) throws LifecycleException {
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));

        String webappDirLocation = "src/main/webapp/";
        Context ctx = tomcat.addWebapp("/qr-attendance-system", new File(webappDirLocation).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
