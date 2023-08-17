package smartmanager.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import smartmanager.dao.FileRepository;
import smartmanager.dao.UserRepository;
import smartmanager.entities.Contact;
import smartmanager.entities.User;

@RestController
public class SearchController 
{
    @Autowired
    private UserRepository userrepo;
    
    @Autowired
    private FileRepository contactrepo;
    
    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchhandler(@PathVariable("query") String query, Principal principal)
    {
        System.out.println(query);
        User user = this.userrepo.getUserByName(principal.getName());
        
        List<Contact> contacts = this.contactrepo.findByNameContainingAndUser(query, user);
        
        return ResponseEntity.ok(contacts);
    }
}
