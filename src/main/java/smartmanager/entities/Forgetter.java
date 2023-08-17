package smartmanager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Forgetter 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
@NotEmpty(message = "Email cannot be empty")
    private String email;
    
    private String otp;

    public Forgetter(int id, String email, String otp) {
        this.id = id;
        this.email = email;
        this.otp = otp;
    }

    public Forgetter(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public Forgetter() {
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    
    
}
