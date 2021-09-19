package ru.atomicscience.restapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import ru.atomicscience.restapp.security.UserRole;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * The main entity of the service
 */
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String login;

    private String firstName;
    private String lastName;

    @Temporal(TemporalType.DATE)
    private Date   birthday;

    @JsonIgnore
    private String password;

    private String aboutMe;
    private String address;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Transient
    @JsonIgnore
    public boolean isFull() {
        return  login     != null &&
                firstName != null &&
                lastName  != null &&
                birthday  != null &&
                password  != null &&
                aboutMe   != null &&
                address   != null;
    }

    @Transient
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return UserRole.getAuthoritiesForRole(this.role);
    }
}
