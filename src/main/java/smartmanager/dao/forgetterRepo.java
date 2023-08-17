package smartmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import smartmanager.entities.Forgetter;


public interface forgetterRepo extends JpaRepository<Forgetter,Integer>
{
    @Query("select u from Forgetter u where u.email=:email")
    public Forgetter getForgetByName(@Param("email") String email);
}
