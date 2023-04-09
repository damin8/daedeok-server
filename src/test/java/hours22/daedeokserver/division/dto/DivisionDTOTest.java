package hours22.daedeokserver.division.dto;

import hours22.daedeokserver.division.domain.Division;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DivisionDTOTest {

    @Test
    void 소속_중복_제거() {
        Division dto = new Division("고등부", "1학년");
        Division dto2 = new Division("고등부", "2학년");
        Division dto3 = new Division("고등부", "3학년");
        Division dto4 = new Division("소년부", "1학년");
        Division dto5 = new Division("소년부", "2학년");
        Division dto6 = new Division("소년부", "3학년");

        List<DivisionDTO> divisionDTOList = DivisionDTO.of(Arrays.asList(dto, dto2, dto3, dto4, dto5, dto6));

        assertThat(divisionDTOList)
                .extracting("first_division")
                .containsExactly("고등부", "소년부");
    }

    @Test
    void 소속_NULL_테스트() {
        Division dto = new Division("고등부", "1학년");
        Division dto2 = new Division("고등부", null);
        Division dto3 = new Division("고등부", "3학년");

        List<DivisionDTO> divisionDTOList = DivisionDTO.of(Arrays.asList(dto, dto2, dto3));

        boolean flag = DivisionDTO.checkDivision("고등부", null, divisionDTOList);
        boolean flag2 = DivisionDTO.checkDivision(null, null, divisionDTOList);
        boolean flag3 = DivisionDTO.checkDivision("고등부", "1학년", divisionDTOList);

        assertThat(flag).isFalse();
        assertThat(flag2).isFalse();
        assertThat(flag3).isTrue();

        dto = new Division("고등부", null);

        divisionDTOList = DivisionDTO.of(Arrays.asList(dto));

        flag = DivisionDTO.checkDivision("고등부", null, divisionDTOList);
        flag2 = DivisionDTO.checkDivision(null, null, divisionDTOList);
        flag3 = DivisionDTO.checkDivision("고등부", "1학년", divisionDTOList);

        assertThat(flag).isTrue();
        assertThat(flag2).isFalse();
        assertThat(flag3).isTrue();

        divisionDTOList = DivisionDTO.of(new ArrayList<>());

        flag = DivisionDTO.checkDivision("고등부", null, divisionDTOList);
        flag2 = DivisionDTO.checkDivision(null, null, divisionDTOList);
        flag3 = DivisionDTO.checkDivision("고등부", "1학년", divisionDTOList);

        assertThat(flag).isTrue();
        assertThat(flag2).isTrue();
        assertThat(flag3).isTrue();
    }

    @Test
    void 리스트_테스트() {
        Division division = new Division("1", "2");
        Division division2 = new Division("1", "2");
        List<Division> divisionList = Arrays.asList(division2, division);

        for (Division temp : divisionList)
            temp.update("2");

        for (Division temp : divisionList)
            assertThat(temp.getFirstDivision()).isEqualTo("2");
    }
}