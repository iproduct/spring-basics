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

package demo.spring.cookiesession.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "CookiesServlet",
        urlPatterns = {"/cookies"}
)
public class CookiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        for (int i = 1; i <= 3; i++) {
            // default maxAge is -1, indicating that cookie lives till the end of current browser session
            Cookie cookie = new Cookie("session-cookie-" + i,
                    "session-cookie-value-" + i);
            resp.addCookie(cookie);
            // this cookie will be valid for an hour
            cookie = new Cookie("persistent-cookie-" + i,"persistent-cookie-value-" + i);
            cookie.setMaxAge(3600);
            resp.addCookie(cookie);
        }
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String docType =
                "<!DOCTYPE html>\n";
        String title = "Active Cookies";
        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<BODY BGCOLOR=\"#FcF0E6\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n" +
                "<table border=1 align=\"center\">\n" +
                "<tr bgcolor=\"#FEAc00\"><th>Cookie Name</th><th>Cookie Value</th></tr>\n");
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            out.println("<tr><td colspan=2>No cookies sent");
        } else {
            Cookie cookie;
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                out.println("<tr><td>" + cookie.getName() + "<td>" + cookie.getValue() + "</td></tr>\n");
            }
        }
        out.println("</table></body></html>");
    }
}
