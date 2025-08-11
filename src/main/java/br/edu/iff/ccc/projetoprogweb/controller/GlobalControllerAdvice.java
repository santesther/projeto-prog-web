package br.edu.iff.ccc.projetoprogweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private SessionAttributes userSession;

    @ModelAttribute("userSession")
    public SessionAttributes getUserSession() {
        return userSession;
    }
}
