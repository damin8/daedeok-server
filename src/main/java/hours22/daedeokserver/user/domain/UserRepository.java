package hours22.daedeokserver.user.domain;

import hours22.daedeokserver.division.domain.Division;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByPhoneNumAndPassword(String phoneNum, String password);
    Optional<User> findByPhoneNum(String phoneNum);
    Page<User> findAllByRoleIsNot(Role role, Pageable pageable);
    Page<User> findAllByNameContainsAndRoleIsNot(String name, Role role, Pageable pageable);
    List<User> findAllByDivision(Division division);
    boolean existsByPhoneNum(String phoneNum);
}
