package hours22.daedeokserver.lecture.domain.plan;

import hours22.daedeokserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanUserRepository extends JpaRepository<PlanUser, Long> {
    Optional<List<PlanUser>> findPlanUsersByPlan_Id(Long id);
    Optional<PlanUser> findPlanUserByPlan_IdAndUser_Id(Long planId, Long userId);
    Optional<PlanUser> findPlanUserByUserAndPlan(User user, Plan plan);
    void deletePlanUsersByUserIdAndPlanIn(Long userId, List<Plan> planList);
}
