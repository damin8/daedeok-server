package hours22.daedeokserver.division.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstDivision;
    private String secondDivision;

    public Division(String firstDivision, String secondDivision) {
        this.firstDivision = firstDivision;
        this.secondDivision = secondDivision;
    }

    public void update(String firstDivision){
        this.firstDivision = firstDivision;
    }

    public void updateSecondDivision(String secondDivision){
        this.secondDivision = secondDivision;
    }
}
