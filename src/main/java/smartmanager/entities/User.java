package smartmanager.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.Email;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class User 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @NotBlank(message = "cannot be blank")
    @Size(min = 2,max = 50,message = "minimum 2 and maximum 50 characters allowed")
    private String name;
    
    @Column(unique = true)
    
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
@NotEmpty(message = "Email cannot be empty")
    private String email;
    
    private String password;
    private String role;
    private boolean enabled;
    private String image;
    @Column(length = 500)
    private String about;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",orphanRemoval = true)
    private List<Contact> contact = new ArrayList<>();

    public User(int id, String name, String email, String password, String role, boolean enabled, String image, String about) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.image = image;
        this.about = about;
    }

    public User(String name, String email, String password, String role, boolean enabled, String image, String about) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.image = image;
        this.about = about;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Contact> getContact() {
        return contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }
@Override
public String toString() {
    return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", role='" + role + '\'' +
            ", enabled=" + enabled +
            ", image='" + image + '\'' +
            ", about='" + about + '\'' +
            '}';
}
    
}
