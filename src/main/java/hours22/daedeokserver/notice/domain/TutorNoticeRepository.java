package hours22.daedeokserver.notice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorNoticeRepository extends JpaRepository<TutorNotice, Long> {
    Page<TutorNotice> findAllByTitleContains(String title, Pageable pageable);
    Optional<TutorNotice> findFirstByIdGreaterThan(Long id);
    Page<TutorNotice> findByIdLessThan(Long id, Pageable pageable);
}
