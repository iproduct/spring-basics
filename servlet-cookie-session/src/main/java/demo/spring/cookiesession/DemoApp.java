// Copyright 2020 (c) IPT - Intellectual Products & Technologies Ltd.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package demo.spring.cookiesession;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;

public class DemoApp {
    private static Logger logger = Logger.getLogger(DemoApp.class.getSimpleName());
    private static int PORT = 8080;

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);

        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.getConnector(); // Trigger the creation of the default connector

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        System.out.println("Application base directory: " + new File("./" + webappDirLocation).getAbsolutePath());

        // Declare an location for your "WEB-INF/classes" dir
        // Servlet 3.x annotations will work
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();
        logger.info("Embedded Tomcat server started on port " + PORT);
        tomcat.getServer().await();
    }
}
