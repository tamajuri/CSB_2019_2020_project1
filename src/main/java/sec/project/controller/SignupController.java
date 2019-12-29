package sec.project.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sec.project.domain.Signup;
import sec.project.inSecureDto.InSecureDto;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    
    @Autowired
    private InSecureDto inSecureDto;
    
    @PostConstruct
    public void init() {
        try {
            inSecureDto.init();
        } catch (SQLException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        try {
            //signupRepository.save(new Signup(name, address));
            inSecureDto.lisaaSignup(new Signup(name, address));
        } catch (SQLException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "done";
    }
    
    @GetMapping("/very-secret-url-to-signups")
    @ResponseBody
    public String allUsers() {
        List<Signup> signups = new ArrayList<>();
        try {
            //signups = signupRepository.findAll();
            signups = inSecureDto.allSignups();
        } catch (SQLException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return signups.stream().map(s -> s.toString()).collect(Collectors.joining("<br />"));
    }
    
    @GetMapping("/hae")
    public String hae(Model model, Authentication authentication) {
        
        model.addAttribute("haetut", null);
        return "hae";
    }
    
    @PostMapping("/hae")
    public String vastaaHakuun(@RequestParam String name, Model model) {
        List<Signup> haetut = new ArrayList<>();
        try {
            //haetut = signupRepository.findAll();
            haetut = inSecureDto.unsafeHaeSignup(name);
        } catch (SQLException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("************************************************");
        System.out.println(haetut.stream().map(s -> s.toString()).collect(Collectors.joining("\n")));
        model.addAttribute("haetut", haetut);
        return "tulokset";
    }

}