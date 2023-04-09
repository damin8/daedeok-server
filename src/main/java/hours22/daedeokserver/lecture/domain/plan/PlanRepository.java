package hours22.daedeokserver.lecture.domain.plan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<List<Plan>> findPlansByLecture_Id(Long id);
}
