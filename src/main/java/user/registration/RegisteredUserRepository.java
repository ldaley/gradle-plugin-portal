package user.registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {

    RegisteredUser findByUsername(String username);

    RegisteredUser findByEmail(String email);

}
