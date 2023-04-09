package hours22.daedeokserver.division.dto;

import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.division.domain.LectureDivision;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class DivisionDTO {
    private String first_division;
    private List<String> second_division;

    public static List<DivisionDTO> of(List<Division> divisionList) {
        Map<String, List<String>> map = new HashMap<>();
        List<DivisionDTO> response = new ArrayList<>();

        for (Division division : divisionList) {
            if (division == null)
                continue;

            String firstDivision = division.getFirstDivision();
            String secondDivision = division.getSecondDivision();

            if (map.containsKey(firstDivision)) {
                if (secondDivision == null)
                    continue;

                List<String> temp = map.get(firstDivision);
                List<String> secondDivisionList = new ArrayList<>(temp);
                secondDivisionList.add(secondDivision);
                map.replace(firstDivision, secondDivisionList);

            } else {
                if (secondDivision == null) map.put(firstDivision, new ArrayList<>());

                else map.put(firstDivision, Arrays.asList(secondDivision));
            }
        }

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            List<String> value = entry.getValue();
            Collections.sort(value);
            response.add(new DivisionDTO(entry.getKey(), value));
        }

        return response;
    }

    public static List<DivisionDTO> ofLectureDivision(List<LectureDivision> lectureDivisionList) {
        List<Division> divisionList = Optional.ofNullable(lectureDivisionList).orElseGet(Collections::emptyList).stream()
                .map(LectureDivision::getDivision)
                .collect(Collectors.toList());

        return of(divisionList);
    }

    public List<Division> toEntity() {
        List<Division> divisionList = new ArrayList<>();

        for (String str : second_division) {
            divisionList.add(new Division(first_division, str));
        }

        divisionList.add(new Division(first_division, null));
        return divisionList;
    }

    public static boolean checkDivision(String firstDivision, String secondDivision, List<DivisionDTO> divisionList) {
        if (divisionList == null || divisionList.size() == 0)
            return true;

        for (DivisionDTO division : divisionList) {
            if (division.getFirst_division().equals(firstDivision)) {
                List<String> secondDivisionList = division.getSecond_division();

                if (secondDivisionList == null || secondDivisionList.size() == 0)
                    return true;

                if (secondDivisionList.contains(secondDivision))
                    return true;
            }
        }

        return false;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateResponse {
        private String first_division;
        private List<SecondDivisionDTO> second_division_list;

        public static UpdateResponse of(List<Division> divisionList) {
            Map<String, List<SecondDivisionDTO>> map = new HashMap<>();
            String key = null;

            for (Division division : divisionList) {
                if (division == null)
                    continue;

                String firstDivision = division.getFirstDivision();
                key = firstDivision;
                String secondDivision = division.getSecondDivision();

                if (map.containsKey(firstDivision)) {
                    if (secondDivision == null)
                        continue;

                    List<SecondDivisionDTO> temp = map.get(firstDivision);
                    List<SecondDivisionDTO> secondDivisionList = new ArrayList<>(temp);
                    secondDivisionList.add(new SecondDivisionDTO(division.getId(), secondDivision));
                    map.replace(firstDivision, secondDivisionList);

                } else {
                    if (secondDivision == null) map.put(firstDivision, new ArrayList<>());

                    else map.put(firstDivision, Arrays.asList(new SecondDivisionDTO(division.getId(), secondDivision)));
                }
            }

            List<SecondDivisionDTO> value = map.get(key);
            value.sort(new Comparator<SecondDivisionDTO>() {
                @Override
                public int compare(SecondDivisionDTO left, SecondDivisionDTO right) {
                    if (left.getId() > right.getId()) {
                        return -1;
                    } else if (left.getId() < right.getId()) {
                        return 1;
                    }
                    return 0;
                }
            });

            return new UpdateResponse(key, value);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateRequest {
        private String before;
        private String after;
        private List<SecondDivisionDTO> delete_second_division_list;
        private List<SecondDivisionDTO> update_second_division_list;
    }
}
