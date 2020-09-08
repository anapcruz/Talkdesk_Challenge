package talkdesk.challenge.calls.service;

import org.springframework.data.domain.Page;
import talkdesk.challenge.calls.model.Call;

import java.util.List;
import java.util.Map;

public interface CallInterface {

    List<Call> getAllOngoingCalls();
    List<Call> getAllCalls();
    void saveCall(Call calls);
    void endCallById(long id);
    void deleteCallByID(long id);
    Page<Call> findPaginatedOngoingCall(int pageNumber, int pageSize, String type);
    Map<Integer, String> getDurationCallByType(String type);
}
