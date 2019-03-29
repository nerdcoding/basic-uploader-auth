/*
 * UserService.java
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

package org.nerdcoding.basicuploader.auth.service;

import org.nerdcoding.basicuploader.auth.persistence.model.Role;
import org.nerdcoding.basicuploader.auth.persistence.model.User;
import org.nerdcoding.basicuploader.auth.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User regsiterNewUser(final User user) {
        // TODO Check required fields
        return userRepository.save(new User.Builder()
                .withUsername(user.getUsername())
                .withPassword(passwordEncoder.encode(user.getPassword()))
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withEmail(user.getEmail())
                .withDayOfBirth(user.getDayOfBirth())
                .withRegistrationDate(LocalDate.now())
                .withRoles(Collections.singletonList(Role.USER))
                .withEnabled(true)
                .withLocked(false)
                .withCredentialsExpired(false)
                .build());
    }

}
