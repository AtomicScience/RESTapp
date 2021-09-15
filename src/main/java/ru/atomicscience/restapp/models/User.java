package ru.atomicscience.restapp.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import ru.atomicscience.restapp.security.UserRole;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    private String login;

    private String firstName;
    private String lastName;

    private Date   birthday;

    private String password;

    private String aboutMe;
    private String address;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public Collection<GrantedAuthority> getAuthorities() {
        return UserRole.getAuthoritiesForRole(this.role);
    }
}
