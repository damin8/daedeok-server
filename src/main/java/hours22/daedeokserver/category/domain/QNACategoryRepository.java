package hours22.daedeokserver.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QNACategoryRepository extends JpaRepository<QNACategory, Long> {
    Optional<QNACategory> findByCategory(String category);

}
