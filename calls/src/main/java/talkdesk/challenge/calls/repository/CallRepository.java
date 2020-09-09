package talkdesk.challenge.calls.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import talkdesk.challenge.calls.model.Call;

import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<Call, Long> {
    /**
     * Returns a page of calls filtering by status, type of call
     * @param status call status
     * @param type call type
     * @param pageable page
     * @return page of calls by status and type
     */
    Page<Call> findAllByStatusAndType(String status, String type, Pageable pageable);

    /**
     * Returns a page of calls filtering by status
     * @param status call status
     * @param pageable page
     * @return page of calls by status
     */
    Page<Call> findCallsByStatus(String status, Pageable pageable);

    /**
     * Returns a list of all calls by type and status
     * @param type call type
     * @param status call ststus
     * @return list of calls
     */
    List<Call> findCallsByTypeAndStatus(String type, String status);

}
