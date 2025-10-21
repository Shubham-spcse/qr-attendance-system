package com.attendance;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class Main {
    public static void main(String[] args) throws LifecycleException {
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));
        tomcat.getConnector();
        
        // Use the WAR file directly
        String warPath = "target/qr-attendance-system.war";
        Context ctx = tomcat.addWebapp("/qr-attendance-system", new java.io.File(warPath).getAbsolutePath());
        
        System.out.println("Starting Tomcat on port " + port);
        tomcat.start();
        tomcat.getServer().await();
    }
}
