package fr.cda.cdafinalprojectbackend.entity;

import fr.cda.cdafinalprojectbackend.document.DBUserImage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@DynamicUpdate
public class DBUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            unique = true,
            nullable = false,
            name = "id"
    )
    private UUID id;

    @Column(
            unique = false,
            nullable = false,
            name = "nickname"
    )
    private String nickname;

    @Column(
            unique = true,
            nullable = false,
            name = "email"
    )
    private String email;

    @Column(
            unique = false,
            nullable = false,
            name = "password"
    )
    private String password;

    @Column(
            unique = false,
            nullable = true,
            name = "description"
    )
    private String description;

    @Column(
            unique = false,
            nullable = true,
            name = "profile_picture"
    )
    private String profilePicture;

    @Column(nullable = true)
    private String city;

    @Column(
            nullable = false,
            unique = false,
            name = "is_active"
    )
    private Boolean isActive;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dog> dogs = new ArrayList<>();
    
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Walk> createdWalks = new ArrayList<>();
    
    @ManyToMany(mappedBy = "participants")
    private Set<Walk> walks = new HashSet<>();

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.getType()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
