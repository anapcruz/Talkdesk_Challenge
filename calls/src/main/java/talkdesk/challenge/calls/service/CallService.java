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
    public void createCalls(ActualCall calls){
        //verify if any parameter is empty
        if (calls.getCallerNumber().equals(""))
            throw new IllegalArgumentException("Caller number should not be empty");
        if (calls.getCalleeNumber().equals(""))
            throw new IllegalArgumentException("Callee number should not be empty");
        if (calls.getCallType().equals(""))
            throw new IllegalArgumentException("Call type should not be empty");
        if (calls.getCallerNumber().equals(calls.getCalleeNumber()))
            throw new IllegalArgumentException("Caller number should be different to callee number");

        for (ActualCall c:  currentCalls){
            if(c.getCallerNumber().equals(calls.getCallerNumber()) && c.getCallStatus().equals("IN_CALL"))
                throw new IllegalArgumentException("Caller number " + calls.getCallerNumber() + " is in call!");

            else if(c.getCalleeNumber().equals(calls.getCalleeNumber()) && c.getCallStatus().equals("IN_CALL"))
                throw new IllegalArgumentException("Callee number " + calls.getCalleeNumber() + " is in call!");
        }
        this.currentCalls.add(calls);

        //check if caller or callee number are in call
        //in case of true, they should not be added to the list
        /**Iterator<Calls> iterator = currentCalls.iterator();
        while(iterator.hasNext()){
            if(iterator.equals(calls.getCallerNumber())  && iterator.equals(calls))
                throw new IllegalArgumentException("Caller number " + calls.getCallerNumber() + " is in call!");
            else if(iterator.equals(calls.getCalleeNumber()))
                throw new IllegalArgumentException("Callee number " + calls.getCalleeNumber() + " is in call!");
            else if(iterator.equals(calls.getCallerNumber()))
            else{
                //add call to the current call list
                this.currentCalls.add(calls);
            }
            iterator.next();
        }*/

    }

    /**
     * Return a list of the active calls
     * @return list of calls
     */
    public List<ActualCall> activeCalls(){
        List<ActualCall> activeCalls = new ArrayList<>(currentCalls);
        return activeCalls;
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
