package com.capgemini.wsb.fitnesstracker.user.internal;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Query searching users by email address. It matches by exact match.
     *
     * @param email email of the user to search
     * @return {@link Optional} containing found user or {@link Optional#empty()} if none matched
     */
    default Optional<User> findByEmail(String email) {
        return findAll().stream()
                        .filter(user -> Objects.equals(user.getEmail(), email))
                        .findFirst();
    }

    default Collection<User> findUserOlderThanX(int age) {
        return findAll().stream()
                .filter(user -> {
                    LocalDate birthdate = user.getBirthdate();
                    if (birthdate == null) {
                        return false;
                    }
                    int userAge = Period.between(birthdate, LocalDate.now()).getYears();
                    return userAge > age;
                })
                .collect(Collectors.toList());
    }
}
