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
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        System.out.println("type " + type);
        if (type.equals("ALL")) {
            return this.callRepository.findCallsByStatus("IN_CALL", paging);
        }
        return this.callRepository.findAllByStatusAndType("IN_CALL", type, paging);
    }

    @Override
    public Map<Integer, String> getDurationCallByType(String type) {
        List<Call> calls = callRepository.findCallsByType(type);
        Map<Integer, Long> mapStatDuration = new HashMap<>();



        for(Call c: calls){
            Instant start = c.getStartTime();
            Instant end = c.getEndTime();

            Duration diffCallTime = Duration.between(end, start);
            long callDuration = diffCallTime.toMinutes();


            LocalDateTime startTime = LocalDateTime.ofInstant(c.getStartTime(), ZoneId.systemDefault());
            int day = startTime.getDayOfMonth();

            mapStatDuration.put(day, callDuration);
        }





        return null;
    }

    private void setCallStatus(Call call){
        call.setStatus("IN_CALL");
        call.setStartTime(Instant.now());
    }

    private void endCallStatus(Call call){
        call.setStatus("END_CALL");
        call.setEndTime(Instant.now());
    }

    private double calculateDuration(List<Call> calls){
        double totalDuration = 0;



        return totalDuration;
    }

}
