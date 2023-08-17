package smartmanager.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import smartmanager.dao.UserRepository;
import smartmanager.entities.User;

public class UserDetailServiceimpl implements UserDetailsService
{
     @Autowired
        private UserRepository userrepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
       User user = userrepo.getUserByName(username);
       if(user==null)
       {
           throw new UsernameNotFoundException("Couldn't Find Such User!!!");
       }
       
       CustomUserDetail customUserDetail = new CustomUserDetail(user);
        return customUserDetail;
    }
    
}
