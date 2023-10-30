package com.playwrite;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "urls")
public class ServicesProperties {

    private Map<String ,String > urlMaps = new HashMap<>();
    private Map<String ,String > stringReplace = new HashMap<>();
    private Map<String ,String >  domainReplace = new HashMap<>();


    //private Map<String ,String >  locationReplace = new HashMap<>();

    public Map<String, String> getDomainReplace() {
        return domainReplace;
    }

    public void setDomainReplace(Map<String, String> domainReplace) {
        this.domainReplace = domainReplace;
    }

    public Map<String, String> getStringReplace() {
        return stringReplace;
    }

    public void setStringReplace(Map<String, String> stringReplace) {
        this.stringReplace = stringReplace;
    }



    public Map<String, String> getUtlMaps() {
        return urlMaps;
    }



    public void setUrlMaps(Map<String, String> urlMaps) {
        this.urlMaps = urlMaps;
    }


//    public Map<String, String> getLocationReplace() {
//        return locationReplace;
//    }
//
//    public void setLocationReplace(Map<String, String> locationReplace) {
//        this.locationReplace = locationReplace;
//    }


}
