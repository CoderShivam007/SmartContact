package smartmanager.controller;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import smartmanager.dao.FileRepository;
import smartmanager.dao.UserRepository;
import smartmanager.entities.Contact;
import smartmanager.entities.User;
import smartmanager.helper.Message;
import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import smartmanager.dao.OrderRepo;
import smartmanager.entities.PaymentOrder;


@Controller
@RequestMapping("/user")
@CrossOrigin
public class UserController {

   
    @Autowired
    private UserRepository userrepo;
    
    @Autowired
    private FileRepository contactrepo;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private OrderRepo orderrepo;
    // creating payment order
  
    @ModelAttribute
    public void CommonData(Model m, Principal principal) {
        String username = principal.getName();
        User user = this.userrepo.getUserByName(username);
        m.addAttribute("user", user);
    }

// =================== Looking at dashboard ===================
    @RequestMapping("/dashboard")
    public String user_dashboard(Model m, Principal principal) {
        return "dashboard";
    }

// =================== Adding Contact( Only providing a new null contact ojject )
    @RequestMapping("/contact")
    public String AddContact(Model m) {
        m.addAttribute("title", "Add Contact");
        m.addAttribute("contact", new Contact());
        return "contact";
    }
    
 //====================== showing All Contacts according to pagination===================  
    @GetMapping("/show/{page}")
    public String showContacts(@PathVariable("page") Integer page,Model m,Principal principal)
    {
        m.addAttribute("title","Show Contacts");
        
       String name = principal.getName();
       User user = this.userrepo.getUserByName(name);
       
        Pageable p = PageRequest.of(page, 5);
       
       Page<Contact> list = this.contactrepo.findContactsByUser(user.getId(),p);
        m.addAttribute("list",list);
        m.addAttribute("currentpage",page);
        m.addAttribute("totalpage",list.getTotalPages());
        return "showcontact";
    }

// ============================= Processing Contact Start ===============================
    
    @PostMapping("/process_contact")
    public String ProcessContact(@ModelAttribute Contact contact, @RequestParam("profileimage") MultipartFile file,Principal principal, 
            BindingResult bindingResult,HttpSession session) throws IOException 
    {
        try
        {
             System.out.println(contact);
             
             if(file.isEmpty())
             {
                 System.out.println("please select a images...");
                 contact.setProfile("contact.png");
             }
             
             else
             {
                 contact.setProfile(file.getOriginalFilename());
                File f = new ClassPathResource("static/images").getFile();
                
                 java.nio.file.Path path = Paths.get(f.getAbsolutePath() + File.separator + file.getOriginalFilename());
                
                Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
               
             }
             
             
            System.out.println(file);
            String name = principal.getName();
            User user = this.userrepo.getUserByName(name);
            contact.setUser(user);
         
            user.getContact().add(contact);
            
            this.userrepo.save(user);
            
            session.setAttribute("message", new Message("Your Contact is added Successfully!!!", "success"));
            return "contact";
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something Went Wrong!!! Can't Added", "danger"));
            return "contact";
        }
           
    } 
    
// ============================= Processing Contact End ===============================

    @GetMapping("/view/{id}")
    public String viewpneContact(@PathVariable("id") Integer id,Principal principal,Model m)
    {
        Optional<Contact> opcontact = this.contactrepo.findById(id);
        Contact contact = opcontact.get();
        
        String Username=principal.getName();
        User user = this.userrepo.getUserByName(Username);
        
        if(user.getId() == contact.getUser().getId())
        {
            m.addAttribute("mycontact",contact);
            m.addAttribute("title",contact.getName());
        }
       
       
        return "viewone";
    }
    
 // ========================== Deleting Contacts ============================
    
    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") Integer id,Principal principal,HttpSession session) throws IOException
    {
        Optional<Contact> opcontact = this.contactrepo.findById(id);
        Contact contact = opcontact.get();
        
         
        String Username=principal.getName();
        User user = this.userrepo.getUserByName(Username);
        
        
        if(user.getId() == contact.getUser().getId())
        {
           // to find the path of that image and delete it 
            
            String image = contact.getProfile();
           User ur = this.userrepo.getUserByName(principal.getName());
           ur.getContact().remove(contact);
           
           if(!"contact.png".equals(image))
           {
               File f = new ClassPathResource("static/images").getFile();
            System.out.println(f);
             Path path= Paths.get(f + "/" + image);
             Files.delete(path);
           }
           
           this.userrepo.save(ur);
          // m.addAttribute("message",new Message());
           session.setAttribute("message",new Message("Your Contact Deleted Sucessfully!!!","alert-success"));
         return "dashboard";
        }
       
         return "dashboard";
    }
    
// Updating a existing Contact
@GetMapping("/update/{id}")
public String updatecontact(@PathVariable("id") Integer id,Principal principal,Model m)
{
         Optional<Contact> opcontact = this.contactrepo.findById(id);
        Contact contact = opcontact.get();
        
        String Username=principal.getName();
        User user = this.userrepo.getUserByName(Username);
        
        if(user.getId() == contact.getUser().getId())
        {
           m.addAttribute("mycontact", contact);
            m.addAttribute("title",contact.getName());
        }
       
    return "updatecontact";
}

// ===================== Processing Update contact form =====================
@PostMapping("/updateprocess")
public String updateprocess(@ModelAttribute Contact contact,@RequestParam("cid") Integer id,@RequestParam("profileimage") MultipartFile file,Principal principal) throws IOException
{
  System.out.println(contact);
  Contact con = this.contactrepo.findById(id).get();
  System.out.println(con);
    String Username=principal.getName();
        User user = this.userrepo.getUserByName(Username);
        con.setName(contact.getName());
        con.setNickname(contact.getNickname());
        con.setEmail(contact.getEmail());
        con.setPhone(contact.getPhone());
        con.setWork(contact.getWork());
        con.setUser(user);
        
        if(file.isEmpty())
        {
            con.setProfile(con.getProfile());
        }
        
        else
        {
            con.setProfile(file.getOriginalFilename());
            
            File f = new ClassPathResource("static/images").getFile();
            
            java.nio.file.Path path = Paths.get(f.getAbsolutePath() + File.separator + file.getOriginalFilename());
            
            Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
            
        }
        
        this.contactrepo.save(con);

        
    return "redirect:/user/update/"+contact.getCid();
}

// ===================== Profile of the User =====================
@GetMapping("/userprofile/{id}")
public String profile(@PathVariable("id") Integer id,Principal principal,Model m)
{
    String name = principal.getName();
       User user = this.userrepo.getUserByName(name);
       
       int total = this.userrepo.getContactNumber(id);
    m.addAttribute("total",total);
    m.addAttribute("user",user);
    return "profile";
}

// =========================== Open Setting handler =============================

@GetMapping("/password")
public String passwordchange()
{
    return "passwordhandle";
}

@PostMapping("/passwordhandler")
public String passwordhandler(@RequestParam("old") String oldpassword, @RequestParam("new") String newpassword,Principal principal,HttpSession session,Model m)
{
    System.out.println("old:" + oldpassword);
    System.out.println("new:" + newpassword);
    
    String username = principal.getName();
    User user = this.userrepo.getUserByName(username);
    
    System.out.println("current:" + user.getPassword());
    
    if(this.bCryptPasswordEncoder.matches(oldpassword, user.getPassword()))
    {
        user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
        this.userrepo.save(user);
        
        session.setAttribute("message", new Message("Password Updated Successfully!!!", "alert-success"));
        
        m.addAttribute("num",3);
        System.out.println("matches...");
        return "redirect:/user/dashboard";
    }
    
    else
    {
        session.setAttribute("message", new Message("Enter Your Current Password Correctly 👿👿👿👿  ", "alert-danger"));
        session.setAttribute("num",4);
        System.out.println("not exact matches...");
         return "redirect:/user/password";
    }
   
}

@PostMapping("/paymentorder")
@ResponseBody
public String orderpayment(@RequestBody Map<String,Object> data,Principal principal) throws RazorpayException
{
    System.out.println("payment executed");
    System.out.println(data);
   int amount = Integer.parseInt(data.get("amount").toString());
   System.out.println(amount);
   
   
   RazorpayClient client = new RazorpayClient("rzp_test_qMQL538IvidV7n", "IMG9znvTxtDO8BhSCwsvsKlK");
   
    JSONObject ob = new JSONObject();
    ob.put("amount", amount*100);
    ob.put("currency", "INR");
    ob.put("receipt", "txn_785314");
    
    // creating order on razorpay
    Order order = client.orders.create(ob);
    System.out.println(order);
    
    // Saving order data in database
    PaymentOrder po = new PaymentOrder();
    
    
    po.setAmount(amount+"");
    po.setOrderid(order.get("id"));
    po.setPaymentid(null);
    po.setStatus("created");
    po.setUser(this.userrepo.getUserByName(principal.getName()));
    po.setReceipt(order.get("receipt"));
    
    this.orderrepo.save(po);
    
    return order.toString(); 
}

@PostMapping("/updateorder")
public ResponseEntity<?> updateorder(@RequestBody Map<String,Object> data)
{
    System.out.println(data);
    
    PaymentOrder po = this.orderrepo.findByOrderid(data.get("order_id").toString());
    System.out.println(po);
    po.setPaymentid(data.get("payment_id").toString());
    po.setOrderid(data.get("order_id").toString());
    po.setStatus(data.get("status").toString());
    this.orderrepo.save(po);
    System.out.println("order updated");
    
    return ResponseEntity.ok(Map.of("msg","updated"));
}

}
