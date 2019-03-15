/*
 * AuthServerUserDetailsService.java
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

package org.nerdcoding.firstuploader.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Service tries to find for given user credentials the appropriate {@link UserDetails}.
 */
@Service
public class AuthServerUserDetailsService implements UserDetailsService {

    /**
     * Temporary use static in-memory map as user database.
     */
    private static final Map<String, UserDetails> IN_MEMORY_USER_DATABASE = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServerUserDetailsService(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void setUp() {
        IN_MEMORY_USER_DATABASE.put("bob",  User.builder()
                .username("bob")
                .password(passwordEncoder.encode("secret123"))
                .authorities(Collections.emptyList())
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (!IN_MEMORY_USER_DATABASE.containsKey(username)) {
            throw new UsernameNotFoundException(String.format(
                    "User with username %s was not found.", username
            ));
        }

        return IN_MEMORY_USER_DATABASE.get(username);
    }
}