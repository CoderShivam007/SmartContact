package smartmanager.controller;

import jakarta.servlet.http.HttpSession;
import java.util.Random;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import smartmanager.dao.EmailService;
import smartmanager.dao.UserRepository;
import smartmanager.dao.forgetterRepo;
import smartmanager.entities.Forgetter;
import smartmanager.entities.User;
import smartmanager.helper.Message;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userrepo;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    
    @Autowired
    private EmailService emailservice;
    
    @Autowired
    private forgetterRepo forgetrepo;
    
    

    @GetMapping("/")
    public String test(Model m) {
        m.addAttribute("title", "Home - Smart Content Manager");
        return "home";
    }

    @GetMapping("/signup")
    public String sign(Model m) {
        m.addAttribute("title", "SignUp - Smart Content Manager");
        m.addAttribute("user", new User());
        return "signup";
    }
    
    @GetMapping("/removeMessage")
    public String removeMessage(HttpSession session) {
        session.removeAttribute("message");
        return "redirect:/signup";
    }
    
    @GetMapping("/removeMessage2")
    public String removeMessage2(HttpSession session) {
        session.removeAttribute("message");
        return "redirect:/user/contact";
    }
    
    @GetMapping("/removeMessage3")
    public String removeMessage3(HttpSession session) {
        session.removeAttribute("message");
        return "redirect:/user/dashboard";
    }
    
    @GetMapping("/removeMessage4")
    public String removeMessage4(HttpSession session) {
        session.removeAttribute("message");
        return "redirect:/user/password";
    }

    @PostMapping("/register")
    public String registeruser(@Valid @ModelAttribute("user") User user, BindingResult result,  @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,  Model m, HttpSession session) {
        try {
            if (!agreement)
            {
                System.out.println("not agree on terms and condition...");
                throw new Exception("not agree on terms and condition..." );
            }
            
            if(result.hasErrors())
            {
                System.out.println("result" + result.toString());
                return "signup";
            }
            
            

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImage("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println(agreement);
            System.out.println(user);
        
            this.userrepo.save(user);
            m.addAttribute("user", new User());
            session.setAttribute("message",new Message("SuccessFully Registered!!! ","alert-success"));
            
        } 
        catch (Exception e)
        {
            m.addAttribute("user", user);
            session.setAttribute("message",new Message("Something Went Wrong!!! " + e.getMessage(),"alert-danger"));
            
        }
      return "signup";
    }
    
    @RequestMapping("/signin")
    public String customLogin(Model m)
    {
        m.addAttribute("title", "Login Page");
        return "login";
    }
    
    // Getting Rendering of OTP form
@GetMapping("/get")
public String getter()
{
    return "getrender";
}

@PostMapping("/verify")
public String verify(@RequestParam("to") String toemail,HttpSession session)
{
    System.out.println("to:" +toemail);
    Random random = new Random();
        int min = (int) Math.pow(10, 5);
        int max = (int) Math.pow(10, 6) - 1;
        int ran =random.nextInt(max - min + 1) + min;
        
      
        String otp = String.format("%06d", ran);
        
        Forgetter forget = this.forgetrepo.getForgetByName(toemail);
        if(forget == null)
        {
            Forgetter f = new Forgetter();
        f.setEmail(toemail);
        f.setOtp(otp);
        this.forgetrepo.save(f);
        }
        
          else
        {
            forget.setOtp(otp);
            this.forgetrepo.save(forget);
        }
        
        
        
        
    this.emailservice.sendEmail(toemail,otp);
    session.setAttribute("email", toemail);
    
    return "verifier";
}

@PostMapping("/verifier/{email}")
public String verifyotp(@PathVariable("email") String Email,  @RequestParam("otp") String otp,HttpSession session)
{
    Forgetter forgetter = this.forgetrepo.getForgetByName(Email);
    if(otp.equals(forgetter.getOtp()))
    {
        return "redirect:/get";
    }
    
    else
    {
        session.setAttribute("message",new Message("Wrong Otp Entered!!! Check Once Again. ","alert-danger"));
        return "login";
    }
    
}
    
    

}
