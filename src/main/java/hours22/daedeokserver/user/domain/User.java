package hours22.daedeokserver.user.domain;

import hours22.daedeokserver.common.domain.BaseTime;
import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.user.dto.UserRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNum;
    private String name;
    @Lob
    private String password;
    private String duty;
    @ManyToOne
    @JoinColumn(name = "division_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Division division;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String phoneNum, String name, String password, String duty, Division division, Role role) {
        this.id = id;
        this.phoneNum = phoneNum;
        this.name = name;
        this.password = password;
        this.duty = duty;
        this.division = division;
        this.role = role;
    }

    public String getRoleName() {
        return role.name();
    }

    public void update(UserRequest.Update request) {
        this.name = request.getName();
        this.duty = request.getDuty();
    }

    public void update(UserRequest.UpdateAdmin request, Division division) {
        this.division = division;
        this.name = request.getName();
        this.duty = request.getDuty();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateDivision(Division division){
        this.division = division;
    }
}
