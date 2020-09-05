package talkdesk.challenge.calls.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import talkdesk.challenge.calls.model.Call;

/**
 * Call Repository
 * allow persistence into the database
 */
@Repository
public interface CallRepository extends JpaRepository<Call, Long> {

    void deleteCallByCallerNumber (String callerNumber);

    void deleteCallByCalleeNumber (String calleeNumber);

    Page<Call> findByType(String type, Pageable pageable);

}
