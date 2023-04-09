package hours22.daedeokserver.lecture.service;

import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.BusinessException;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.lecture.domain.Status;
import hours22.daedeokserver.lecture.domain.Type;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.lecture.domain.plan.Plan;
import hours22.daedeokserver.lecture.domain.plan.PlanRepository;
import hours22.daedeokserver.lecture.domain.plan.PlanUser;
import hours22.daedeokserver.lecture.domain.plan.PlanUserRepository;
import hours22.daedeokserver.lecture.dto.plan.*;
import hours22.daedeokserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanUserRepository planUserRepository;

    @Transactional(readOnly = true)
    public PlanUserResponse find(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.PLAN_NOT_FOUND));
        List<PlanUser> planUserList = planUserRepository.findPlanUsersByPlan_Id(planId).orElse(new ArrayList<>());
        long count = planUserList.size();
        Type type = plan.getType();

        return new PlanUserResponse(count, type, plan.getWeek(), PlanUserResponse.Summary.of(planUserList));
    }

    @Transactional
    public void update(Lecture lecture, List<Long> deletePlanList, List<User> userList, List<PlanRequest.Update> request) {

        for (Long planId : deletePlanList)
            planRepository.deleteById(planId);

        for (PlanRequest.Update planRequest : request) {
            Plan plan = planRequest.getId() == null ? null : planRepository.findById(planRequest.getId()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.PLAN_NOT_FOUND));

            if (plan == null) {
                plan = planRequest.toPlan(lecture);
                planRepository.save(plan);

                List<PlanUser> planUserList = new ArrayList<>();

                for (User user : userList)
                    planUserList.add(new PlanUser(user, plan, null));

                savePlanUser(planUserList);
            } else plan.update(planRequest);
        }
    }

    @Transactional(readOnly = true)
    public OnlineResponse findOnline(User user, Long planId) {
        PlanUser planUser = planUserRepository.findPlanUserByPlan_IdAndUser_Id(planId, user.getId()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (planUser.getPlan().getLecture().getStatus().equals(Status.FINISH))
            throw new BusinessException(ErrorCode.LECTURE_FINISHED);

        if (!planUser.getPlan().check())
            throw new BusinessException(ErrorCode.TIME_MISMATCH);

        return OnlineResponse.of(planUser);
    }

    @Transactional(readOnly = true)
    public List<PlanResponse.Attendance> findAttendance(User user, Long lecture_id) {
        List<PlanResponse.Attendance> attendanceList = new ArrayList<>();
        List<Plan> planList = planRepository.findPlansByLecture_Id(lecture_id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        for (Plan plan : planList) {
            PlanUser planUser = planUserRepository.findPlanUserByUserAndPlan(user, plan).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
            attendanceList.add(PlanResponse.Attendance.of(plan, planUser));
        }

        attendanceList.sort(new Comparator<PlanResponse.Attendance>() {
            @Override
            public int compare(PlanResponse.Attendance left, PlanResponse.Attendance right) {
                if (left.getWeek() < right.getWeek()) {
                    return -1;
                } else if (left.getWeek() > right.getWeek()) {
                    return 1;
                }
                return 0;
            }
        });

        return attendanceList;
    }

    @Transactional
    public void attendance(User user, Long planId) {
        PlanUser planUser = planUserRepository.findPlanUserByPlan_IdAndUser_Id(planId, user.getId()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Type type = planUser.getPlan().getType();

        if (!planUser.getPlan().validate())
            throw new BusinessException(ErrorCode.TIME_MISMATCH);

        if (planUser.getPlan().getLecture().getStatus().equals(Status.FINISH))
            throw new BusinessException(ErrorCode.LECTURE_FINISHED);

        if (type.equals(Type.ZOOM))
            planUser.updateStatus(Status.COMPLETE);

        else throw new BusinessException(ErrorCode.ATTENDANCE_FAIL);
    }

    @Transactional
    public void attendance(Long planId, Long userId) {
        PlanUser planUser = planUserRepository.findPlanUserByPlan_IdAndUser_Id(planId, userId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Type type = planUser.getPlan().getType();

        if (planUser.getPlan().getLecture().getStatus().equals(Status.FINISH))
            throw new BusinessException(ErrorCode.LECTURE_FINISHED);

        if (type.equals(Type.OFFLINE) || type.equals(Type.ZOOM))
            planUser.updateStatus(Status.COMPLETE);

        else throw new BusinessException(ErrorCode.ATTENDANCE_FAIL);
    }

    @Transactional
    public boolean completeCheck(Long userId, Long lecture_id) {
        List<Plan> planList = planRepository.findPlansByLecture_Id(lecture_id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        for (Plan plan : planList) {
            PlanUser planUser = planUserRepository.findPlanUserByPlan_IdAndUser_Id(plan.getId(), userId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

            if (planUser.getStatus() == null || !planUser.getStatus().equals(Status.COMPLETE))
                return false;
        }

        return true;
    }

    @Transactional
    public void attendance(Long planId) {
        List<PlanUser> planUserList = planUserRepository.findPlanUsersByPlan_Id(planId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (planUserList.get(0).getPlan().getLecture().getStatus().equals(Status.FINISH))
            throw new BusinessException(ErrorCode.LECTURE_FINISHED);

        for (PlanUser planUser : planUserList) {
            Type type = planUser.getPlan().getType();

            if (type.equals(Type.OFFLINE))
                planUser.updateStatus(Status.COMPLETE);

            else throw new BusinessException(ErrorCode.ATTENDANCE_FAIL);
        }
    }

    @Transactional
    public void save(List<Plan> planList) {
        planRepository.saveAll(planList);
    }

    @Transactional
    public void savePlanUser(List<PlanUser> planList) {
        planUserRepository.saveAll(planList);
    }

    @Transactional
    public void saveDuration(User user, Long planId, DurationRequest request) {
        PlanUser planUser = planUserRepository.findPlanUserByPlan_IdAndUser_Id(planId, user.getId()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (planUser.getPlan().getLecture().getStatus().equals(Status.FINISH))
            throw new BusinessException(ErrorCode.LECTURE_FINISHED);

        planUser.updateDuration(request.getDuration());

        if (planUser.getDuration() == 100f)
            planUser.updateStatus(Status.COMPLETE);
    }

    @Transactional
    public void delete(Long userId, Lecture lecture) {
        List<Plan> planList = findPlans(lecture.getId());
        planUserRepository.deletePlanUsersByUserIdAndPlanIn(userId, planList);
    }

    @Transactional(readOnly = true)
    public List<Plan> findPlans(Long id) {
        return planRepository.findPlansByLecture_Id(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PLAN_NOT_FOUND));
    }
}
