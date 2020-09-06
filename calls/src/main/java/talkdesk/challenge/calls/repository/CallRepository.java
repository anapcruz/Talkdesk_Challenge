package talkdesk.challenge.calls.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import talkdesk.challenge.calls.model.Call;

import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<Call, Long> {

    List<Call> findCallByStatus(String status);
}
