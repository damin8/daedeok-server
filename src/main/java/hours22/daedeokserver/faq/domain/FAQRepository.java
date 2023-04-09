package hours22.daedeokserver.faq.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    Page<FAQ> findAllByTitleContains(String keyword, Pageable pageable);
    Optional<FAQ> findFirstByIdGreaterThan(Long id);
    Page<FAQ> findByIdLessThan(Long id, Pageable pageable);
}
