package br.edu.iff.ccc.projetoprogweb.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "principal")
public class MainViewController {
    
    @GetMapping("/")
    public String getHomePage(){
        return "index.html";
    }
}
