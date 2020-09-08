package talkdesk.challenge.calls.service;

import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.repository.CallRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CallService implements CallInterface {

    @Autowired
    private CallRepository callRepository;


    /*@Override
    public List<Call> getAllOngoingCalls() {
        return callRepository.findCallByStatus("IN_CALL");
    }*/

    @Override
    public List<Call> getAllOngoingCalls() {
        return null;
    }

    @Override
    public List<Call> getAllCalls() {
        return callRepository.findAll();
    }

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
        //check if caller or callee number exists in the list with the status "IN_CALL"
        //in case of true an exception is triggered
        for (Call c:  cal){
            if(c.getCallerNumber().equals(calls.getCallerNumber()) && c.getStatus().equals("IN_CALL"))
                throw new IllegalArgumentException("Caller number " + calls.getCallerNumber() + " is in call!");
            else if(c.getCalleeNumber().equals(calls.getCalleeNumber()) && c.getStatus().equals("IN_CALL"))
                throw new IllegalArgumentException("Callee number " + calls.getCalleeNumber() + " is in call!");
        }
        setCallStatus(calls);
        this.callRepository.save(calls);
    }

    @Override
    public void endCallById(long id) {
        Optional<Call> optional = callRepository.findById(id);
        Call call;

        if(optional.isPresent()){
            call = optional.get();
        }else{
            throw new RuntimeException("Call id not found for id :: " + id);
        }

        endCallStatus(call);
        this.callRepository.save(call);
    }

    @Override
    public void deleteCallByID(long id) {
        this.callRepository.deleteById(id);
    }

    @Override
    public Page<Call> findPaginatedOngoingCall(int pageNumber, int pageSize, String type) {
        Pageable paging = PageRequest.of(pageNumber-1, pageSize);
        if (type.equals("ALL")) {
            return this.callRepository.findCallsByStatus("IN_CALL", paging);
        }
        return this.callRepository.findAllByStatusAndType("IN_CALL", type, paging);
    }

    @Override
    public Map<String, String> getDurationCallByType(String type) {
        List<Call> calls = callRepository.findCallsByTypeAndStatus(type, "END_CALL");
        Map<String, Long> mapStatDuration = new HashMap<>();
        Map<String, String> mapDurationCall = new HashMap<>();

        for(Call c: calls){
            //duration call time
            long duration = c.getEndTime().getTime() - c.getStartTime().getTime();

            String date = getDate(c.getStartTime());

            if(!mapStatDuration.containsKey(date))
                mapStatDuration.put(date,duration);
            mapStatDuration.put(date, mapStatDuration.get(date)+duration);
        }

        //Format time
        for (Map.Entry<String, Long> entry : mapStatDuration.entrySet()) {
            mapDurationCall.put(entry.getKey(),timeToString(entry.getValue()));
        }

        return mapDurationCall;
    }

    @Override
    public Long getTotalNumberOfCalls() {
        return callRepository.count();
    }

    @Override
    public Map<String, Map<String, Long>> getTotalCallsByCallerNumber(String CallerOrCallee) {
        List<Call> calls = callRepository.findAll();
        Map<String, Map<String, Long>> mapCallsByCaller = new HashMap<>();

        Map<String, Long> mapCalls = new HashMap<>();

        if(CallerOrCallee.equals("Caller"))
            calls.stream().map(Call::getCallerNumber).forEach(callerNb -> mapCalls.put(callerNb, mapCalls.getOrDefault(callerNb, (long) 0) + 1));
        else if(CallerOrCallee.equals("Callee"))
            calls.stream().map(Call::getCalleeNumber).forEach(calleeNb -> mapCalls.put(calleeNb, mapCalls.getOrDefault(calleeNb, (long) 0) + 1));

        calls.stream().map(Call::getStartTime).forEach(callDate -> {
            String date = getDate(callDate);
            mapCallsByCaller.put(date, mapCalls);
        });

        return mapCallsByCaller;
    }

    @Override
    public Long getTotalCallsByCalleeNumber(String calleeNumber) {
        return callRepository.countCallsByCalleeNumber(calleeNumber);
    }


    private void setCallStatus(Call call){
        call.setStatus("IN_CALL");
        call.setStartTime(new Timestamp(System.currentTimeMillis()));
    }

    private void endCallStatus(Call call){
        call.setStatus("END_CALL");
        call.setEndTime(new Timestamp(System.currentTimeMillis()));
    }

    private String getDate(Timestamp time){
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(time.getTime());

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return String.format("%s-%s-%s",day, month, year);
    }

    private String timeToString(long time){
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time));
    }

}
