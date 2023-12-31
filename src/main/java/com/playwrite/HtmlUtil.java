package com.playwrite;

import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlUtil {

    static public String getHost(Page page) {
        String url = page.url();
        int posHost = url.indexOf('/', 10);
        String host = url.substring(0, posHost);
        System.out.println(url);
        System.out.println(host);
        return host;

    }

    static public String getFrameHost(Frame frame) {
        String url = frame.url();
        int posHost = url.indexOf('/', 10);
        String host = url.substring(0, posHost);
        System.out.println(url);
        System.out.println(host);
        return host;

    }

    static public void fixLinks(Document doc, String host) {
        Elements links = doc.getElementsByTag("link");
        for (Element link : links) {
            String href = link.attr("href");
            if (href.startsWith("/")) {
                href = host + href;
                link.attr("href" ,href);
            }
        }
        Elements as = doc.getElementsByTag("a");
        for (Element a : as) {
            String href = a.attr("href");
            if (href.startsWith("/")) {
                href = host + href;
                a.attr("href" ,href);
            }
        }
    }

    static public void fixScript(Document doc, String host) {
        Elements scripts = doc.getElementsByTag("script");
        for (Element script : scripts) {
            String src = script.attr("src");
            if (src.startsWith("/")) {
                System.out.println(src);
                src = host + src;
                System.out.println(src);
               // script.attr("src" ,src);
            }
        }

    }
}
