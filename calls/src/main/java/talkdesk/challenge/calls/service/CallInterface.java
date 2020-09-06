package talkdesk.challenge.calls.service;

import org.springframework.data.domain.Page;
import talkdesk.challenge.calls.model.Call;

import java.util.List;

public interface CallInterface {

    List<Call> getAllOngoingCalls();
    List<Call> getAllCalls();
    void saveCall(Call calls);
    void endCallById(long id);
    void deleteCallByID(long id);
    Page<Call> findPaginated(int pageNumber, int pageSize);
}
