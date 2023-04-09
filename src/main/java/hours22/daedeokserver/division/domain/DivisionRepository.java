package hours22.daedeokserver.division.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DivisionRepository extends JpaRepository<Division, Long> {
    Optional<Division> findByFirstDivisionAndSecondDivision(String firstDivision, String secondDivision);
    List<Division> findByFirstDivision(String firstDivision);
    boolean existsByFirstDivision(String firstDivision);
    void deleteAllByFirstDivision(String firstDivision);
}
