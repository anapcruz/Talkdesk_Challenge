package talkdesk.challenge.calls.service;

import org.springframework.stereotype.Service;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.model.ActualCall;
import talkdesk.challenge.calls.repository.CallRepository;

import java.time.Instant;
import java.util.*;

@Service
public class CallService implements ICallService{


    private final CallRepository callRepository;

    /**
     * List of current calls
     */
    LinkedList<ActualCall> currentCalls = new LinkedList<>();

    public CallService(CallRepository callRepository) { this.callRepository = callRepository; }


    /**
     * When a new call is created, this function is called to save this call in a list
     * This list stores all current calls, i.e., calls with the status equals "IN_CALL"
     * @param calls new call
     */
    public void createCalls(Call calls){
        //verify if any parameter is empty
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
        callRepository.save(calls);
    }

    /**
     * Set status of the call
     * @param call current call
     */
    public void setCallStatus(Call call){
        call.setStartTime(Instant.now());
        call.setStatus("IN_CALL");
    }

    /**
     * Return a list of the active calls
     * @return list of calls
     */
    public List<ActualCall> activeCalls(){
        return new ArrayList<>(currentCalls);
    }

    /**
     * Function called when a call is finished, i.e., the call status change to "END_CALL"
     * @param callerNumber phone number of the caller
     */
    public void endCall(String callerNumber){
        for (ActualCall c:  currentCalls){
            if(c.getCallerNumber().equals(callerNumber)){
                //mudar para exceções
                c.setCallStatus("END_CALL");
                saveCall(c, Instant.now());
            }
            else
                throw new IllegalArgumentException("The caller number " + callerNumber + " does not exist!");
        }
    }

    /**
     * Function called when connection is finished. After that, the call will be stored into the database
     * @param calls call finished
     * @param endTime end timestamp of the call
     */
    private void saveCall(ActualCall calls, Instant endTime){
        Call c = new Call(calls.getCallerNumber(), calls.getCalleeNumber(), calls.getStartTime(), endTime, calls.getCallType());
        //store into database
        callRepository.save(c);
    }

}
