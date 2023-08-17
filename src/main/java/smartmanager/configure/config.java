package smartmanager.configure;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;



@Configuration
@EnableWebSecurity

public class config 
{
    
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
    
     
    
    @Bean
    public UserDetailsService userDetailsService()
    {
        return new UserDetailServiceimpl();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    
    
    
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
           .csrf().disable()
            .authorizeHttpRequests((authz) -> {
            try {
                authz
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll().and().formLogin()
                        .loginPage("/signin")
                        .defaultSuccessUrl("/user/dashboard", true);
                
                
                
                 http
                          
    .logout()
       .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
            } catch (Exception ex) {
                Logger.getLogger(config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            )
            .httpBasic(withDefaults());
       
        
            
        return http.build();
    }
}
