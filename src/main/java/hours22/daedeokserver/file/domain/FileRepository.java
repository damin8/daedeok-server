package hours22.daedeokserver.file.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByParentId(String parentId);
    void deleteAllByParentId(String parentIdd);
}
