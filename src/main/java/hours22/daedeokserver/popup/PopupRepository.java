package hours22.daedeokserver.popup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopupRepository extends JpaRepository<Popup, Long> {
    void deleteAllByIdNotIn(List<Long> idList);
}
