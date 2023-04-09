package hours22.daedeokserver.guide.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long> {
    Page<Guide> findAllByTitleContains(String keyword, Pageable pageable);
    Optional<Guide> findFirstByIdGreaterThan(Long id);
    Page<Guide> findByIdLessThan(Long id, Pageable pageable);
}
