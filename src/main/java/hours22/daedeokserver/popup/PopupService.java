package hours22.daedeokserver.popup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupRepository repository;

    @Transactional(readOnly = true)
    public PopupResponse find() {
        List<Popup> list = repository.findAll();

        return new PopupResponse(PopupResponse.Summary.of(list));
    }

    @Transactional
    public void save(PopupRequest.Update request) {
        List<PopupRequest> popupRequestList = request.getPopup_list();
        List<Long> unchangedIdList = request.getUnchanged_popup_id_list();

        if(unchangedIdList.size() == 0)
            repository.deleteAll();

        else
            repository.deleteAllByIdNotIn(unchangedIdList);

        List<Popup> list = new ArrayList<>();

        for (PopupRequest popupRequest : popupRequestList)
            list.add(popupRequest.toEntity());


        repository.saveAll(list);
    }
}
