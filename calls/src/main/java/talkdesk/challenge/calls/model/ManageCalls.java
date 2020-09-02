package talkdesk.challenge.calls.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.ListIterator;

import static java.lang.System.exit;

public class ManageCalls {

    /**
     * List the current calls
     */
    private LinkedList<Calls> call = new LinkedList<>();

    /**
     * End timestamp of the call
     */
    private Instant endTime;

    public void addCall(Calls calls){
        if (calls.getCallerNumber().equals("")) throw new IllegalArgumentException("Caller number should not be empty");
        if (calls.getCalleeNumber().equals("")) throw new IllegalArgumentException("Callee number should not be empty");
        if (calls.getCallType().equals("")) throw new IllegalArgumentException("Call type should not be empty");



        for (Calls c:  call){
            if(calls.getCallerNumber().equals(c.getCallerNumber()) && c.getCallStatus().equals("IN_CALL"))
                //mudar para exceções
                System.out.print("Caller " + calls.getCallerNumber() + " is in call!");

            else if(calls.getCalleeNumber().equals(c.getCalleeNumber()) && c.getCallStatus().equals("IN_CALL"))
                System.out.print("Callee " + c.getCalleeNumber() + " is in call!");



                System.out.println("list " + call);
        }
        //if not in call, add to the list
        this.call.add(calls);

    }

    public Instant endCall(Calls calls){
        for (Calls c:  call){
            if(calls.getCallerNumber().equals(c.getCallerNumber()) &&  calls.getCalleeNumber().equals(c.getCalleeNumber())
                    && c.getCallStatus().equals("IN_CALL"))
                //mudar para exceções
                calls.setCallStatus("END_CALL");
                endTime = Instant.now();
        }
        return endTime;
    }

}
