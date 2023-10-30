package com.playwrite.controller;

import com.microsoft.playwright.Page;
import com.playwrite.Client;
import com.playwrite.HtmlUtil;
import com.playwrite.ServicesProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerMapping;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WrapperHandler {

    @Autowired
    private ServicesProperties servicesProperties;

    @Value("${serverName}")
    private String serverName;


    private static Logger log = LoggerFactory.getLogger(WrapperHandler.class);




    /*
        @Autowired
        private LdapTemplate ldapTemplate;

    */


    public ResponseEntity<byte[]> wrapper(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        Client client = (Client) session.getAttribute("httpClient");
        Page page = (Page) session.getAttribute("page");

        boolean first = false;
        if (client == null) {
            client = new Client();
            session.setAttribute("httpClient", client);
            first = true;
        }

        //Object body =null;
        String body = null;
        //HttpEntity<Object> requestEntity = null;
        //HttpHeaders headers = new HttpHeaders();
        Map<String, String> urlmap = servicesProperties.getUtlMaps();
        Map<String, String> stringReplaceMap = servicesProperties.getStringReplace();
        Map<String, String> domainReplaceMap = servicesProperties.getDomainReplace();

        // System.out.println(urlmap);
        // System.out.println(serverName);

        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        //  String fullPath = request.getRequestURL().toString();
        System.out.println("fullPath : " + fullPath);
        //System.out.println("request.getRequestURL() :" + request.getRequestURL());
        String qs = request.getQueryString();
        System.out.println("qs:" + qs);
        //System.out.println(qs.length());
        String url = "";

        //String host = HtmlUtil.getHost(page);

        url = url + "?" + qs;
        System.out.println("url :" + url);

        url="https://account.microsoft.com/bundles/scripts/jquery?v=8K7yq-V9xvWPsp4ocxHkO98bJtmRcUb-eMhrWteWLo01";

        URI pUri = URI.create(url);
        Enumeration<String> headersName = request.getHeaderNames();


        if (request.getHeader("content-type") != null && request.getHeader("content-type").startsWith("multipart/form-data")) {
            System.out.println("fileupload");
            //  requestEntity = new HttpEntity<>(body, headers);

        } else {
//          //  List<MediaType> conList = new ArrayList();
            //   conList.add(MediaType.APPLICATION_JSON);
            // headers.setAccept(conList );


            body =

                    request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        }

        try {

            HttpRequestBase requestBase = null;

            switch (request.getMethod()) {

                case "GET":
                    requestBase = new HttpGet(pUri);
                    break;
                case "POST":
                    requestBase = new HttpPost(pUri);
                    ((HttpPost) requestBase).setEntity(new StringEntity(body, "utf8"));
                    break;
                case "PUT":
                    requestBase = new HttpPut(pUri);
                    ((HttpPut) requestBase).setEntity(new StringEntity(body, "utf8"));
                    break;
                default:
                    break;
            }


            while (headersName.hasMoreElements()) {
                String headerName = headersName.nextElement();
                String headerValue = request.getHeader(headerName);

                if (
                    //   !headerName.equalsIgnoreCase("Cookie") &&
                        !headerName.equalsIgnoreCase("host") && !headerName.equalsIgnoreCase("content-length")) {
                    //if (headerName.equalsIgnoreCase("Cookie")) {
                    //    System.out.println("cookie: " + headerValue);
                    //    headerValue = headerValue.replace("NoExtension;" ,"NoExtension; SSOCOOKIEPULLED=1;");
                    // }

                    if(url.contains("browser.events.data.microsoft.com") &&  !headerName.equalsIgnoreCase("Origin") ){
                        requestBase.addHeader("Origin", "https://account.microsoft.com");
                    }else {

                        if (!(url.contains("consumers/oauth2/v2.0/authorize")
                                && (
                                headerName.equalsIgnoreCase("Sec-Fetch-Site")
                                        || headerName.equalsIgnoreCase("Referer")
                        )
                        ))



                            requestBase.addHeader(headerName, headerValue);
                    }
                }
                //   System.out.println("headerName :" + headerName +"  ,headerValue :" +headerValue );
            }


/*
            while (headersName.hasMoreElements()) {
                String headerName = headersName.nextElement();
                String headerValue = request.getHeader(headerName);

                if (!headerName.equalsIgnoreCase("host") && !headerName.equalsIgnoreCase("content-length")) {
                    //if (headerName.equalsIgnoreCase("Cookie")) {
                    //   System.out.println("cookie: " + headerValue);
                    //   headerValue = headerValue.replace("NoExtension;", "NoExtension; SSOCOOKIEPULLED=1;");
                    // }

                    if (url.contains("browser.events.data.microsoft.com") && !headerName.equalsIgnoreCase("Origin")) {
                        requestBase.addHeader("Origin", "https://account.microsoft.com");
                    } else {

                        if (!(url.contains("consumers/oauth2/v2.0/authorize") && (headerName.equalsIgnoreCase("Sec-Fetch-Site") || headerName.equalsIgnoreCase("Referer"))))


                            if (first || !headerName.equalsIgnoreCase("Cookie")) {

                                requestBase.addHeader(headerName, headerValue);
                            }
                    }
                }
                //   System.out.println("headerName :" + headerName +"  ,headerValue :" +headerValue );
            }
*/

            System.out.println("pUri :" + pUri);
            System.out.println("requestBase.getAllHeaders() :");
/*
            if (url.contains("consumers/oauth2/v2.0/authorize")) {
                requestBase.addHeader("Host", "login.microsoftonline.com");
                requestBase.addHeader("Sec-Fetch-Site", "cross-site");
                requestBase.addHeader("Referer", "https://account.microsoft.com/");
                System.out.println(requestBase);

            }
  */
            //  CloseableHttpResponse httpResponse = httpClient.execute(requestBase);
            HttpResponse httpResponse = client.execute(requestBase);


            String content = null;
            HttpEntity entity = httpResponse.getEntity();
    /*
            if (entity != null) {
                content = EntityUtils.toString(entity);
                // System.out.println("content: " + content);

                if (//!rpUri.toLowerCase().contains("MSALBrowserBundleName".toLowerCase())
                        !rpUri.toLowerCase().contains("discovery/instance".toLowerCase())) {
                    for (Map.Entry<String, String> entry : stringReplaceMap.entrySet()) {
                        //   System.out.println(entry.getKey() + "/" + entry.getValue());
                        content = content.replace(entry.getKey(), entry.getValue());

                    }
                }
                if (!pUri.toString().endsWith(".gif")) System.out.println("content: " + content);

            }
                // login.live.com
*/
            Header[] resHeaders = httpResponse.getAllHeaders();
            if (resHeaders.length > 45) {
                System.out.println("----------");
            }
            System.out.println("resHeaders.length : " + resHeaders.length);
            //response =
            //       restTemplate.exchange(pUri, HttpMethod.valueOf(request.getMethod()), requestEntity, byte[].class);
            int numCookis = 0;

            HttpHeaders retHeaders = new HttpHeaders();
            //HttpHeaders resHeaders = response.getHeaders();
            for (Header header : resHeaders) {
                String headerValue = header.getValue();
                String tmp = headerValue;
                if (header.getName().equalsIgnoreCase("Location")) {
                    for (Map.Entry<String, String> entry : stringReplaceMap.entrySet()) {

                        String str = entry.getKey().trim();
                        String replace = entry.getValue().trim();
                        headerValue = headerValue.replace(str, replace);
                    }

                }


                    retHeaders.add(header.getName(), headerValue);
 //               }

            }
            System.out.println("fullPath url :" + fullPath);
            System.out.println("pUrl :" + pUri);
            System.out.println("numCookis :" + numCookis);


            System.out.println("retHeaders.size() :" + retHeaders.size());

            if (content != null) {
                return new ResponseEntity<>(content.getBytes(), retHeaders, httpResponse.getStatusLine().getStatusCode());
            } else {
                // return new ResponseEntity<>( null, httpResponse.getStatusLine().getStatusCode());
                return new ResponseEntity<>(null, retHeaders, HttpStatus.resolve(204));
            }
        } catch (HttpClientErrorException hte) {
            HttpHeaders retHeaders = new HttpHeaders();
            HttpHeaders resHeaders = hte.getResponseHeaders();
            resHeaders.forEach((headerName, value) -> {
                //     if (!headerName.equalsIgnoreCase("Access-Control-Allow-Origin")
                //             && !headerName.equalsIgnoreCase("Set-Cookie")

                //    ) {
                retHeaders.add(headerName, value.get(0));
                //  }
            });
            System.out.println(hte.getResponseBodyAsByteArray());
            String res = hte.getResponseBodyAsString();
            //  System.out.println("res" + res);
            //res = res.replaceAll("login\\.live\\.com", "loginLiveCom");
            //      System.out.println("res" + res);
            //return new ResponseEntity<>(hte.getResponseBodyAsByteArray(), retHeaders, hte.getStatusCode());
            System.out.println("retHeaders.size() :" + retHeaders.size());
            return new ResponseEntity<>(res.getBytes(), retHeaders, hte.getStatusCode());

        }


        //return response;
    }


}