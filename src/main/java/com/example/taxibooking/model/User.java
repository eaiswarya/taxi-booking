package com.example.taxibooking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
//
// import java.util.Collection;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User /*implements UserDetails*/ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private Double accountBalance;

    //    @Override
    //    public Collection<? extends GrantedAuthority> getAuthorities() {
    //        return null;
    //    }
    //
    //    @Override
    //    public String getPassword() {
    //        return password;
    //    }
    //
    //    @Override
    //    public String getUsername() {
    //        return null;
    //    }
    //
    //    @Override
    //    public boolean isAccountNonExpired() {
    //        return false;
    //    }
    //
    //    @Override
    //    public boolean isAccountNonLocked() {
    //        return false;
    //    }
    //
    //    @Override
    //    public boolean isCredentialsNonExpired() {
    //        return false;
    //    }
    //
    //    @Override
    //    public boolean isEnabled() {
    //        return false;
}
