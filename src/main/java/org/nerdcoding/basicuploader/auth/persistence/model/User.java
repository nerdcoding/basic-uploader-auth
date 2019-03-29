/*
 * Userdata.java
 *
 * Copyright (c) 2019, Tobias Koltsch. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 and
 * only version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-2.0.html>.
 */

package org.nerdcoding.basicuploader.auth.persistence.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Information about a application user who is applicable to login at the authorization server.
 */
@Entity
@Table(name = "USER_DATA")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "pk_sequence_generator", sequenceName = "PK_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence_generator")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name= "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name= "PASSWORD", nullable = false)
    private String password;

    @Column(name= "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name= "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name= "EMAIL", nullable = false, unique = true)
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name= "DAY_OF_BIRTH")
    private LocalDate dayOfBirth;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name= "REGISTRATION_DATE", nullable = false)
    private LocalDate registrationDate;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "ROLE", joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Collection<Role> roles;

    @Column(name= "ENABLED", nullable = false)
    private boolean enabled;

    @Column(name= "LOCKED", nullable = false)
    private boolean locked;

    @Column(name= "CREDENTIALS_EXPIRED", nullable = false)
    private boolean credentialsExpired;

    public User() {} // for instantiation by JPA

    private User(final Builder builder) {
        username = builder.username;
        password = builder.password;
        firstName = builder.firstName;
        lastName = builder.lastName;
        email = builder.email;
        dayOfBirth = builder.dayOfBirth;
        registrationDate = builder.registrationDate;
        roles = builder.roles;
        enabled = builder.enabled;
        locked = builder.locked;
        credentialsExpired = builder.credentialsExpired;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public LocalDate getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(final LocalDate dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(final LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    public void setLocked(final boolean locked) {
        this.locked = locked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    public void setCredentialsExpired(final boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    @Override
    public boolean isAccountNonExpired() {
        // always return true - there is not account expiration in this application
        return true;
    }

    public static final class Builder {
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private LocalDate dayOfBirth;
        private LocalDate registrationDate;
        private Collection<Role> roles;
        private boolean enabled;
        private boolean locked;
        private boolean credentialsExpired;

        public Builder() {
        }

        public Builder withUsername(final String val) {
            username = val;
            return this;
        }

        public Builder withPassword(final String val) {
            password = val;
            return this;
        }

        public Builder withFirstName(final String val) {
            firstName = val;
            return this;
        }

        public Builder withLastName(final String val) {
            lastName = val;
            return this;
        }

        public Builder withEmail(final String val) {
            email = val;
            return this;
        }

        public Builder withDayOfBirth(final LocalDate val) {
            dayOfBirth = val;
            return this;
        }

        public Builder withRegistrationDate(final LocalDate val) {
            registrationDate = val;
            return this;
        }

        public Builder withRoles(final Collection<Role> val) {
            roles = val;
            return this;
        }

        public Builder withEnabled(final boolean val) {
            enabled = val;
            return this;
        }

        public Builder withLocked(final boolean val) {
            locked = val;
            return this;
        }

        public Builder withCredentialsExpired(final boolean val) {
            credentialsExpired = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
