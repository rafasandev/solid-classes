package com.example.market_api.core.user.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.market_api.common.base.AuditableEntity;
import com.example.market_api.core.contact_info.model.ContactInfo;
import com.example.market_api.core.role.model.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@SuperBuilder
public class User extends AuditableEntity implements UserDetails {

    @Column(unique = true)
    private String email;
    private String password;
    private boolean active;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactInfo> contacts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public void setStatus(boolean isActive) {
        this.active = isActive;
    }

    public boolean userHasContactInfoFilled() {
        return contacts != null && !contacts.isEmpty();
    }

    public void addContact(ContactInfo contact) {
        contacts.add(contact);
    }

    public void removeContact(ContactInfo contact) {
        contacts.remove(contact);
        contact.setProfile(null);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null)
            return Collections.emptyList();

        return roles.stream().map(role -> {
            switch (role.getName()) {
                case ADMIN:
                    return new SimpleGrantedAuthority("ROLE_ADMIN");
                case COMPANY:
                    return new SimpleGrantedAuthority("ROLE_COMPANY");
                case INDIVIDUAL:
                    return new SimpleGrantedAuthority("ROLE_INDIVIDUAL");
                default:
                    return new SimpleGrantedAuthority("ROLE_" + role.getName().name());
            }
        }).toList();
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
