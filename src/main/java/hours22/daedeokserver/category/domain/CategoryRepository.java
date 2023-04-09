package hours22.daedeokserver.category.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAllByCategoryContains(String category, Pageable pageable);
    Optional<Category> findByCategory(String category);
}
