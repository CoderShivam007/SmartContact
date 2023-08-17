package smartmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import smartmanager.entities.User;

public interface UserRepository extends JpaRepository<User,Integer>
{
    @Query("select u from User u where u.email=:email")
    public User getUserByName(@Param("email") String email);
    
    @Query("SELECT COUNT(*) FROM Contact as c WHERE c.user.id = :id")
    public int getContactNumber(@Param("id") int id);
}
