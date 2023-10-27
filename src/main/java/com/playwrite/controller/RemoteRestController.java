package com.playwrite.controller;


import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.playwrite.Field;
import com.playwrite.HtmlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
//import com.jayway.jsonpath.JsonPath;

@RestController
public class RemoteRestController {


    protected Logger logger = LoggerFactory.getLogger(RemoteRestController.class);
    protected Playwright playwright = Playwright.create();


    @PostMapping("/updateFieldData")
    public ResponseEntity<String> updateFieldData(HttpSession session, @RequestBody Field field) {
        System.out.println(field);
        Page page = (Page) session.getAttribute("page");
        page.locator(field.getXpath()).clear();
        page.locator(field.getXpath()).type(field.getValue());
        return new ResponseEntity<String>("{\"ret\":\"ok\"}", HttpStatus.OK);
    }

    @PostMapping("/click")
    public ResponseEntity<String> click(HttpServletRequest request, HttpSession session, @RequestBody Field field) {
        System.out.println(field);
        Page page = (Page) session.getAttribute("page");
        page.locator(field.getXpath()).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        int pos = request.getRequestURL().indexOf(request.getRequestURI());

        String next = request.getRequestURL().substring(0, pos);
        String nextUrl = next + "/next";

        return new ResponseEntity<String>("{\"url\":\"" + nextUrl + "\"}", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<String> init(HttpServletResponse response,Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        Page page = (Page) session.getAttribute("page");

        String[] args = //{"Hello", "World"};
                {
                        //'--proxy-server=200.32.51.179:8080',
                        //'--proxy-server=131.221.64.62:8090',
                        "--ignore-certificate-errors",
                        "--no-sandbox",
                        //'--disable-setuid-sandbox',
                        "--window-size=1920,1080",
                        "--disable-dev-shm-usage",
                        //   '--proxy-server='+conf.vpnServer ,
                        "--disable-accelerated-2d-canvas",
                        "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36",
                        "--disable-gpu"};


        if (page == null) {
            System.out.println(page);
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)
                    //     .setArgs(Arrays.asList(args))
            );
            BrowserContext context = browser.newContext();
            page = context.newPage();
            // page.setDefaultTimeout(100000);
        }
        page.navigate("https://redeem.microsoft.com");

        //  page.waitForLoadState(LoadState.DOMCONTENTLOADED );
        page.waitForLoadState(LoadState.NETWORKIDLE);
        session.setAttribute("page", page);

        System.out.println(page.content());


        page.evaluate("() => {" +
                "for (const script of document.documentElement.querySelectorAll('script')) script.remove(); " +
                "return document.documentElement.outerHTML; " +
                "}"
        );

        //page.content();
        //System.out.println(page.title());
        System.out.println(page.content());

        //model.addAttribute("page", page);

        String contect = page.content().replace("</head>", "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js\"></script><script src='script/custom.js'></script></head>");
        contect = contect.replace("type=\"submit\"", "type=\"button\"");
        String host = HtmlUtil.getHost(page);
        System.out.println(host);

        //page.waitForLoadState(d);
        page.waitForLoadState(LoadState.LOAD);
//                NETWORKIDLE);
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods" ,"*");

        return new ResponseEntity<String>(contect, HttpStatus.OK);

    }


    @GetMapping("/next")
    public ResponseEntity<String> next(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        @SuppressWarnings("unchecked")

        Page page = (Page) session.getAttribute("page");
        //  page.waitForLoadState(LoadState.LOAD);
        //page.waitForLoadState(LoadState.NETWORKIDLE);

        if (page == null) {
            //return new ResponseEntity<String>(content, HttpStatus.TEMPORARY_REDIRECT);
            response.sendRedirect("/");
        }
        System.out.println("page.title(): " + page.title());
        //Sign in to your Microsoft account
        //Microsoft account(
       // if (!page.title().equalsIgnoreCase("Microsoft account | Redeem your code or gift card")) {


            // page.waitForSelector("script");
            page.evaluate("() => {" +
                    "for (const script of document.documentElement.querySelectorAll('script')) script.remove(); " +
                    "return document.documentElement.outerHTML; " +
                    "}"
            );
       // }
        int pos = request.getRequestURL().indexOf(request.getRequestURI());

        String homeScript = request.getRequestURL().substring(0, pos);
        String scriptUrl = homeScript + "/script/custom.js";
        System.out.println(request.getRequestURL());

        List<Frame> frames = page.frames();
        for (Frame frame : frames) {
            frame.waitForLoadState();
        }

        String content = page.content();
        content = content.replace("</head>", "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js\"></script><script src='" + scriptUrl + "'></script></head>");
        //cmdSystem.out.println(content);
        content = content.replace("type=\"submit\"", "type=\"button\"");
        System.out.println("**********************************************************************8");
        System.out.println(content);
        System.out.println("**********************************************************************8");
        Document doc = Jsoup.parse(content);
        Elements iframes = doc.getElementsByTag("iframe");
        int iframeNo = 0;
        for (Element frame : iframes) {
            frame.attr("src", "/iframe/" + iframeNo);
            iframeNo++;
        }
        System.out.println("page.url()");
        System.out.println(page.url());
        String host = HtmlUtil.getHost(page);
        HtmlUtil.fixLinks(doc, host);
 //       page.waitForLoadState(LoadState.NETWORKIDLE);
 //       page.waitForLoadState(LoadState.LOAD);
 //       page.waitForLoadState(LoadState.DOMCONTENTLOADED);
//
        //return new ResponseEntity<String>(content, HttpStatus.OK);
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods" ,"*");
        return new ResponseEntity<String>(doc.outerHtml(), HttpStatus.OK);
    }


    @GetMapping("/iframe/{frameNo}")
    public ResponseEntity<String> getFramNo(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable int frameNo) throws IOException {

        System.out.println("/iframe/{frameNo} : " + frameNo);
        Page page = (Page) session.getAttribute("page");

        List<Frame> frames = page.frames();
//        for (Frame frame : frames) {
        //           frame.waitForLoadState();
        //      }

        //List<Frame> frames = page.childFrames();
        Frame frame = frames.get(frameNo);
        //Frame frame = frames.childFrames();
        String content = frame.content();

        int pos = request.getRequestURL().indexOf(request.getRequestURI());
        String homeScript = request.getRequestURL().substring(0, pos);
        String scriptUrl = homeScript + "/script/custom.js";
        content = content.replace("</head>", "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js\"></script><script src='" + scriptUrl + "'></script></head>");

        Document doc = Jsoup.parse(content);
        Elements iframes = doc.getElementsByTag("iframe");
        int iframeNo = 0;
        for (Element iframe : iframes) {
            iframe.attr("src", "/iframe/" + frameNo + "/" + iframeNo);
            iframeNo++;
        }
        String host = HtmlUtil.getFrameHost(frame);
        HtmlUtil.fixLinks(doc, host);
        HtmlUtil.fixScript(doc, host);

        System.out.println("page.url()");
        System.out.println(page.url());

        return new ResponseEntity<String>(doc.outerHtml(), HttpStatus.OK);
    }


    @GetMapping("/iframe/{frameNo}/{inerframeNo}")
    public ResponseEntity<String> getInnerFramNo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                                 @PathVariable int frameNo,
                                                 @PathVariable int inerframeNo) throws IOException {
        System.out.println("/iframe/{frameNo} : " + frameNo + "/" + inerframeNo);

        Page page = (Page) session.getAttribute("page");

        List<Frame> frames = page.frames();
//        for (Frame frame : frames) {
//            frame.waitForLoadState();
//        }


        Frame frame = frames.get(frameNo);

        List<Frame> innerFrame = frame.childFrames();
        innerFrame.get(inerframeNo).waitForLoadState();


        System.out.println("page.url()");
        System.out.println(page.url());


        String content = innerFrame.get(inerframeNo).content();
        int pos = request.getRequestURL().indexOf(request.getRequestURI());
        String homeScript = request.getRequestURL().substring(0, pos);
        String scriptUrl = homeScript + "/script/custom.js";
        content = content.replace("</head>", "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js\"></script><script src='" + scriptUrl + "'></script></head>");


        Document doc = Jsoup.parse(content);
        String host = HtmlUtil.getFrameHost(innerFrame.get(inerframeNo));
        HtmlUtil.fixLinks(doc, host);
        HtmlUtil.fixScript(doc, host);

        //return new ResponseEntity<String>(content, HttpStatus.OK);
        return new ResponseEntity<String>(doc.outerHtml(), HttpStatus.OK);
    }


    @GetMapping("/productList")
    public ResponseEntity<String> createRestData() throws IOException, InterruptedException {
        return new ResponseEntity<String>("ggggg", HttpStatus.OK);

    }


}






