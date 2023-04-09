package hours22.daedeokserver.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
    Optional<BoardCategory> findByCategory(String category);
}
