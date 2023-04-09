package hours22.daedeokserver.qna.domain;

import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.category.domain.QNACategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QNARepository extends JpaRepository<QNA, Long> {
    Page<QNA> findAllByTitleContains(String title, Pageable pageable);
    Page<QNA> findAllByCategory(QNACategory category, Pageable pageable);
    Page<QNA> findAllByCategoryAndTitleContains(QNACategory category, String title, Pageable pageable);
    Optional<QNA> findFirstByIdGreaterThan(Long id);
    Page<QNA> findByIdLessThan(Long id, Pageable pageable);
}
