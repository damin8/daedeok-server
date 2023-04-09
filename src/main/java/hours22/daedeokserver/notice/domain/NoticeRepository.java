package hours22.daedeokserver.notice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByTitleContains(String title, Pageable pageable);
    Optional<Notice> findFirstByIdGreaterThan(Long id);
    Page<Notice> findByIdLessThan(Long id, Pageable pageable);
}
