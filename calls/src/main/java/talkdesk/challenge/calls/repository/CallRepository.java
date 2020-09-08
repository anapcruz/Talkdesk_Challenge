package talkdesk.challenge.calls.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import talkdesk.challenge.calls.model.Call;

import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<Call, Long> {

    Page<Call> findAllByStatusAndType(String status, String type, Pageable pageable);
    Page<Call> findCallsByStatus(String status, Pageable pageable);
    List<Call> findCallsByType(String type);
}
