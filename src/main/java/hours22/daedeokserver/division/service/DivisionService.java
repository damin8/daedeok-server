package hours22.daedeokserver.division.service;

import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.division.domain.DivisionRepository;
import hours22.daedeokserver.division.domain.LectureDivision;
import hours22.daedeokserver.division.domain.LectureDivisionRepository;
import hours22.daedeokserver.division.dto.DivisionDTO;
import hours22.daedeokserver.division.dto.SecondDivisionDTO;
import hours22.daedeokserver.exception.ErrorCode;
import hours22.daedeokserver.exception.business.BusinessException;
import hours22.daedeokserver.exception.business.EntityNotFoundException;
import hours22.daedeokserver.lecture.domain.lecture.Lecture;
import hours22.daedeokserver.user.domain.User;
import hours22.daedeokserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DivisionService {

    private final DivisionRepository divisionRepository;
    private final UserRepository userRepository;
    private final LectureDivisionRepository lectureDivisionRepository;

    @Transactional(readOnly = true)
    public List<DivisionDTO> find() {
        List<Division> divisionList = divisionRepository.findAll();
        return DivisionDTO.of(divisionList);
    }

    @Transactional(readOnly = true)
    public DivisionDTO.UpdateResponse find(String firstDivision) {
        List<Division> divisionList = divisionRepository.findByFirstDivision(firstDivision);

        if (divisionList.size() == 0)
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND);

        return DivisionDTO.UpdateResponse.of(divisionList);
    }

    @Transactional(readOnly = true)
    public Division find(String firstDivision, String secondDivision) {
        return divisionRepository.findByFirstDivisionAndSecondDivision(firstDivision, secondDivision).orElseThrow(() -> new EntityNotFoundException(ErrorCode.DIVISION_NOT_FOUND));
    }

    @Transactional
    public void delete(String firstDivision) {
        divisionRepository.deleteAllByFirstDivision(firstDivision);
    }

    @Transactional
    public void save(DivisionDTO request) {
        if (divisionRepository.existsByFirstDivision(request.getFirst_division()))
            throw new BusinessException(ErrorCode.VALUE_CONFLICT_DIVISION);

        divisionRepository.saveAll(request.toEntity());
    }

    @Transactional
    public void saveLectureDivision(Lecture lecture, List<DivisionDTO> divisionDTOList) {
        if (divisionDTOList == null || divisionDTOList.size() == 0) {
            lectureDivisionRepository.save(LectureDivision.builder()
                    .lecture(lecture)
                    .division(null)
                    .build());
            return;
        }

        List<LectureDivision> lectureDivisionList = new ArrayList<>();

        for (DivisionDTO divisionDTO : divisionDTOList) {
            if(divisionDTO.getSecond_division() == null){
                Division division = divisionRepository.findByFirstDivisionAndSecondDivision(divisionDTO.getFirst_division(), null).orElseThrow(() -> new EntityNotFoundException(ErrorCode.DIVISION_NOT_FOUND));
                lectureDivisionList.add(LectureDivision.builder()
                        .lecture(lecture)
                        .division(division)
                        .build());
                continue;
            }

            for (String secondDivision : divisionDTO.getSecond_division()) {
                Division division = divisionRepository.findByFirstDivisionAndSecondDivision(divisionDTO.getFirst_division(), secondDivision).orElseThrow(() -> new EntityNotFoundException(ErrorCode.DIVISION_NOT_FOUND));
                lectureDivisionList.add(LectureDivision.builder()
                        .lecture(lecture)
                        .division(division)
                        .build());
            }
        }

        lectureDivisionRepository.saveAll(lectureDivisionList);
    }

    @Transactional
    public void updateLectureDivision(Lecture lecture, List<DivisionDTO> divisionDTOList) {
        lectureDivisionRepository.deleteAllByLecture(lecture);
        saveLectureDivision(lecture, divisionDTOList);
    }

    @Transactional
    public void update(DivisionDTO.UpdateRequest request) {
        if (request.getDelete_second_division_list() != null) {
            List<Division> deleteDivisionList = new ArrayList<>();
            List<SecondDivisionDTO> secondDivisionDTOList = request.getDelete_second_division_list();
            Division nullDivision = divisionRepository.findByFirstDivisionAndSecondDivision(request.getBefore(), null).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

            for (SecondDivisionDTO secondDivisionDTO : secondDivisionDTOList) {
                Division division = divisionRepository.findById(secondDivisionDTO.getId()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
                deleteDivisionList.add(division);

                List<LectureDivision> lectureDivisionList = lectureDivisionRepository.findAllByDivision(division);
                for (LectureDivision lectureDivision : lectureDivisionList)
                    lectureDivision.updateDivision(nullDivision);

                List<User> userList = userRepository.findAllByDivision(division);
                for (User user : userList)
                    user.updateDivision(nullDivision);

            }

            divisionRepository.deleteAll(deleteDivisionList);
        }

        if (request.getAfter() != null) {
            if (divisionRepository.existsByFirstDivision(request.getAfter()))
                throw new BusinessException(ErrorCode.VALUE_CONFLICT_DIVISION);

            List<Division> divisionList = divisionRepository.findByFirstDivision(request.getBefore());

            for (Division division : divisionList) {
                division.update(request.getAfter());
            }
        }

        if (request.getUpdate_second_division_list() != null) {
            List<SecondDivisionDTO> secondDivisionDTOList = request.getUpdate_second_division_list();

            for (SecondDivisionDTO secondDivisionDTO : secondDivisionDTOList) {
                if (secondDivisionDTO.getId() != null) {
                    Division division = divisionRepository.findById(secondDivisionDTO.getId()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
                    division.updateSecondDivision(secondDivisionDTO.getSecond_division());
                } else {
                    String firstDivision = request.getAfter() == null ? request.getBefore() : request.getAfter();
                    Division division = new Division(firstDivision, secondDivisionDTO.getSecond_division());
                    divisionRepository.save(division);
                }
            }
        }
    }
}
