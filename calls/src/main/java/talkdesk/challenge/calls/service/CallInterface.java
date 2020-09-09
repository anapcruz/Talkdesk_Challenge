package talkdesk.challenge.calls.service;

import org.springframework.data.domain.Page;
import talkdesk.challenge.calls.model.Call;

import java.util.Map;

public interface CallInterface {

    /**
     * When a new call is created, this function is called to save this call in a list
     * This list stores all current calls, i.e., calls with the status equals "ONGOING_CALL"
     * @param calls new call
     */
    void saveCall(Call calls);

    /**
     * Get id of the call and updates the status and end timestamp.
     * @param id call id
     */
    void endCallById(long id);

    /**
     * Delete call by id of the database
     * @param id call id
     */
    void deleteCallByID(long id);

    /**
     * Returns the page of the call by type.
     * @param pageNumber number of pages represented in the table
     * @param pageSize max number of the rows per "view" of the table
     * @param type call type
     * @param status call status (ONGOING_CALL or ENDED_CALL)
     * @return pagination of the list of calls
     */
    Page<Call> findCallPagination(int pageNumber, int pageSize, String type, String status);

    /**
     * Call duration by type
     * @param type call type
     * @return map that contains the day and the total call duration
     */
    Map<String, String> getDurationCallByType(String type);

    /**
     * Get the total number of calls.
     * @return total number of calls
     */
    Long getTotalNumberOfCalls();

    /**
     * Get the number of calls by the caller or callee number.
     * @param CallerOrCallee caller or caller number
     * @return map of maps, i.e., the key is the date of the call and assigned it there is the caller or
     * the caller number and the related occurrences
     */
    Map<String, Map<String, Long>> getTotalCallsByCallerOrCalleeNumber(String CallerOrCallee);

    /**
     * Get total call cost by type.
     * Outbound calls cost 0.05 per minute after the first 5 minutes. The first 5 minutes cost 0.10.
     * Inbound calls are free.
     * @return a map that contains the day and total cost
     */
    Map<String, Double> getTotalCostByType();
}
