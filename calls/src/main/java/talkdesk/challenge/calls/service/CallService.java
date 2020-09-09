package talkdesk.challenge.calls.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.repository.CallRepository;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CallService implements CallInterface {

    @Autowired
    private CallRepository callRepository;

    /**
     * When a new call is created, this function is called to save this call in a list
     * This list stores all current calls, i.e., calls with the status equals "ONGOING_CALL"
     * @param calls new call
     */
    @Override
    public void saveCall(Call calls) {
        if (calls.getCallerNumber().equals(""))
            throw new IllegalArgumentException("Caller number should not be empty");
        if (calls.getCalleeNumber().equals(""))
            throw new IllegalArgumentException("Callee number should not be empty");
        if (calls.getType().equals(""))
            throw new IllegalArgumentException("Call type should not be empty");
        if (calls.getCallerNumber().equals(calls.getCalleeNumber()))
            throw new IllegalArgumentException("Caller number should be different to callee number");

        List<Call> cal = callRepository.findAll();
        //check if caller or callee number exists in the list with the status "ONGOING_CALL"
        //in case of true an exception is triggered
        for (Call c:  cal){
            if(c.getCallerNumber().equals(calls.getCallerNumber()) && c.getStatus().equals("ONGOING_CALL"))
                throw new IllegalArgumentException("Caller number " + calls.getCallerNumber() + " is in call!");
            else if(c.getCalleeNumber().equals(calls.getCalleeNumber()) && c.getStatus().equals("ONGOING_CALL"))
                throw new IllegalArgumentException("Callee number " + calls.getCalleeNumber() + " is in call!");
        }
        //update the status and the start timestamp of the call
        setCallStatus(calls);

        //save call
        this.callRepository.save(calls);
    }

    /**
     * Get id of the call and updates the status and end timestamp.
     * @param id call id
     */
    @Override
    public void endCallById(long id) {
        Optional<Call> optional = callRepository.findById(id);
        Call call;

        //check if the select id exists
        if(optional.isPresent()){
            call = optional.get();
        }else{
            throw new RuntimeException("Call id not found for id :: " + id);
        }

        //update the status and the end timestamp of the call
        endCallStatus(call);

        //save call (updates the status and end timestamp of the call id)
        this.callRepository.save(call);
    }

    /**
     * Delete call by id of the database
     * @param id call id
     */
    @Override
    public void deleteCallByID(long id) {
        this.callRepository.deleteById(id);
    }

    /**
     * Returns the page of the call by type.
     * @param pageNumber number of pages represented in the table
     * @param pageSize max number of the rows per "view" of the table
     * @param type call type
     * @param status call status (ONGOING_CALL or ENDED_CALL)
     * @return pagination of the list of calls
     */
    @Override
    public Page<Call> findCallPagination(int pageNumber, int pageSize, String type, String status) {
        Pageable paging = PageRequest.of(pageNumber-1, pageSize);
        //By default all calls are presented, defined by the string "ALL"
        if (type.equals("ALL")) {
            //get all calls
            return this.callRepository.findCallsByStatus(status, paging);
        }
        //get all call by type
        return this.callRepository.findAllByStatusAndType(status, type, paging);
    }

    /**
     * Call duration by type
     * @param type call type
     * @return map that contains the day and the total call duration
     */
    @Override
    public Map<String, String> getDurationCallByType(String type) {
        List<Call> calls = callRepository.findCallsByTypeAndStatus(type, "ENDED_CALL");
        Map<String, Long> mapStatDuration = new HashMap<>();
        Map<String, String> mapDurationCall = new HashMap<>();

        for(Call c: calls){
            //duration call time
            long duration = c.getEndTime().getTime() - c.getStartTime().getTime();
            //get date (day-month-year)
            String date = getDate(c.getStartTime());

            //check if the date exists in the map
            if(!mapStatDuration.containsKey(date))
                //add the date and the call duration
                mapStatDuration.put(date,duration);
            //assign to the existing key (date), an increase of the call duration
            mapStatDuration.put(date, mapStatDuration.get(date)+duration);
        }

        //Format time to visualize the correct total call duration
        for (Map.Entry<String, Long> entry : mapStatDuration.entrySet()) {
            mapDurationCall.put(entry.getKey(),timeToString(entry.getValue()));
        }
        return mapDurationCall;
    }

    /**
     * Get the total number of calls.
     * @return total number of calls
     */
    @Override
    public Long getTotalNumberOfCalls() {
        //count all calls
        return callRepository.count();
    }

    /**
     * Get the number of calls by the caller or callee number.
     * @param CallerOrCallee caller or caller number
     * @return map of maps, i.e., the key is the date of the call and assigned it there is the caller or
     * the caller number and the related occurrences
     */
    @Override
    public Map<String, Map<String, Long>> getTotalCallsByCallerOrCalleeNumber(String CallerOrCallee) {
        //list of all calls
        List<Call> calls = callRepository.findAll();

        //map that will be stored the date of the call, and the caller or callee number and the number of occurrences
        Map<String, Map<String, Long>> mapCallsByCaller = new HashMap<>();

        //map that will be stored the caller or callee number and the number of occurrences
        Map<String, Long> mapCalls = new HashMap<>();

        //check the received string
        if(CallerOrCallee.equals("Caller"))
            //stores the caller number and its occurrences
            calls.stream().map(Call::getCallerNumber).forEach(callerNb -> mapCalls.put(callerNb, mapCalls.getOrDefault(callerNb, (long) 0) + 1));
        else if(CallerOrCallee.equals("Callee"))
            //stores the callee number and its occurrences
            calls.stream().map(Call::getCalleeNumber).forEach(calleeNb -> mapCalls.put(calleeNb, mapCalls.getOrDefault(calleeNb, (long) 0) + 1));


        calls.stream().map(Call::getStartTime).forEach(callDate -> {
            //get the date of the call
            String date = getDate(callDate);
            //assign the date, the caller/callee number and its occurrences, previously saved
            mapCallsByCaller.put(date, mapCalls);
        });

        return mapCallsByCaller;
    }

    /**
     * Get total call cost by type.
     * Outbound calls cost 0.05 per minute after the first 5 minutes. The first 5 minutes cost 0.10.
     * Inbound calls are free.
     * @return a map that contains the day and total cost
     */
    @Override
    public Map<String, Double> getTotalCostByType() {
        //map that will be stored the date and the total cost
        Map<String, Double> mapTotalCost = new HashMap<>();
        //list of call filtering by type and ended status
        List<Call> callList = callRepository.findCallsByTypeAndStatus("OUTBOUND", "ENDED_CALL");

        for (Call c: callList){
            //duration call time
            long diffTime = c.getEndTime().getTime() - c.getStartTime().getTime();
            //convert the duration time to minutes
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffTime)%TimeUnit.HOURS.toMinutes(1);

            //totalCost defined by default with 0.10 because the first 5 minutes have a constant value
            double totalCost = 0.10;
            //after the five minutes there is an increase of 0.05 per minute
            if(minutes > 5)
                totalCost = (minutes- 5)*0.05;

            //check if the date exists in the map
            if(!mapTotalCost.containsKey(getDate(c.getStartTime()))){
                //add the date and the total call cost
                mapTotalCost.put(getDate(c.getStartTime()), totalCost);
            }else{
                //assign to the existing key (date), an increase of the total call cost
                mapTotalCost.put(getDate(c.getStartTime()), mapTotalCost.get(getDate(c.getStartTime())) + totalCost);
            }
        }
        return mapTotalCost;
    }

    /**
     * Auxiliary function to update the status and the start timestamp of the call, when a new call is created
     * @param call current call
     */
    private void setCallStatus(Call call){
        call.setStatus("ONGOING_CALL");
        call.setStartTime(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Auxiliary function to update the status and the end timestamp of the call, when a call is ended
     * @param call current call
     */
    private void endCallStatus(Call call){
        call.setStatus("ENDED_CALL");
        call.setEndTime(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Auxiliary function to get and format the date
     * @param time timestamp
     * @return date string
     */
    private String getDate(Timestamp time){
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(time.getTime());

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return String.format("%s-%s-%s",day, month, year);
    }

    /**
     * Auxiliary function to return time into String
     * @param time required time
     * @return time into String
     */
    private String timeToString(long time){
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time));
    }

}
