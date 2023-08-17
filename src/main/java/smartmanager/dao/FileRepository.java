package smartmanager.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import smartmanager.entities.Contact;
import smartmanager.entities.User;

public interface FileRepository extends JpaRepository<Contact, Integer> {
    // Additional methods can be added here if needed
    
    @Query("from Contact as c where c.user.id=:uid")
    public Page<Contact> findContactsByUser(@Param("uid")int uid, Pageable pageable);
    
    public List<Contact> findByNameContainingAndUser(String name,User user);
}
