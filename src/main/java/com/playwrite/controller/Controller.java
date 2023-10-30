package com.playwrite.controller;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*"
       // ,        allowCredentials = "true"



)
public class Controller {

    @Autowired
    private WrapperHandler wrapperHandler;





    /**
     * Wrapper
     *
     *
/*     */
    @SuppressWarnings("rawtypes")

    @CrossOrigin(origins = "*")
    @RequestMapping(value = { "/XXXXuniblends/**" ,"/XXXbundles/**"})
    public ResponseEntity wrapper(HttpServletRequest request , HttpServletResponse response , HttpSession session) throws Exception {
        return wrapperHandler.wrapper(request ,response,session);
    }

}