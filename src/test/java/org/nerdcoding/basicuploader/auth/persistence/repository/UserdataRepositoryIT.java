/*
 * UserdataRepositoryIT.java
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

package org.nerdcoding.basicuploader.auth.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nerdcoding.basicuploader.auth.persistence.model.Role;
import org.nerdcoding.basicuploader.auth.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


/**
 * Spring based integration test for {@link UserdataRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserdataRepositoryIT {

    private static final String USERNAME = "bspencer";
    private static final String PASSWORD = "secret4711";
    private static final String FIRST_NAME = "Bud";
    private static final String LAST_NAME = "Spencer";
    private static final String EMAIL = "bud.spencer@yahoo.com";
    private static final LocalDate DAY_OF_BIRTH = LocalDate.of(1929, 10, 31);
    private static final LocalDate REGISTRATION_DATE = LocalDate.of(2018, 5, 15);
    private static final Collection<Role> ROLES = Arrays.asList(Role.USER, Role.ADMIN);
    private static final boolean ENABLED = true;
    private static final boolean LOCKED = true;
    private static final boolean CREDENTIALS_EXPIRED = true;

    @Autowired
    private UserdataRepository subject;

    private Long testUserId;

    @Before
    public void setUp() {
        final User saved = subject.save(new User.Builder()
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withEmail(EMAIL)
                .withDayOfBirth(DAY_OF_BIRTH)
                .withRegistrationDate(REGISTRATION_DATE)
                .withRoles(ROLES)
                .withEnabled(ENABLED)
                .withLocked(LOCKED)
                .withCredentialsExpired(CREDENTIALS_EXPIRED)
                .build());
        testUserId = saved.getId();
    }

    @Test
    public void testFindById_whenSearchedWithExistingUserId_thenUserIsFound() {
        final Optional<User> found = subject.findById(testUserId);
        assertThat(found).isNotEmpty();
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.getId()).isEqualTo(testUserId));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.getUsername()).isEqualTo(USERNAME));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.getPassword()).isEqualTo(PASSWORD));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.getFirstName()).isEqualTo(FIRST_NAME));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.getLastName()).isEqualTo(LAST_NAME));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.getEmail()).isEqualTo(EMAIL));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.getDayOfBirth()).isEqualTo(DAY_OF_BIRTH));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.getRegistrationDate()).isEqualTo(REGISTRATION_DATE));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.getRoles()).containsAll(ROLES));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.isEnabled()).isEqualTo(ENABLED));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.isLocked()).isEqualTo(LOCKED));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.isCredentialsExpired()).isEqualTo(CREDENTIALS_EXPIRED));
        assertThat(found).hasValueSatisfying(
                user -> assertThat(user.isCredentialsNonExpired()).isEqualTo(!CREDENTIALS_EXPIRED));
    }

    @Test
    public void testFindById_whenSearchedWithNotExistingUserId_thenReturnsEmptyOptional() {
        final Optional<User> result = subject.findById(4711L);
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindById_whenSearchedWithNull_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> subject.findById(null))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("must not be null");
    }

}