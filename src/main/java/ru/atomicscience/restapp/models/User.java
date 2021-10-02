package ru.atomicscience.restapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

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

    // This annotation will disable password field from being included in serialization,
    // but will not stop it from being included in deserialized objects
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String aboutMe;
    private String address;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER; // A default value for all created users
    @JsonIgnore
    // Field stores the last token user has authenticated with and is nullified when
    // the token is invalidated, disabling authentication with said token.
    // Token is hashed with the same algorithm as the password
    private String lastValidToken;

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
