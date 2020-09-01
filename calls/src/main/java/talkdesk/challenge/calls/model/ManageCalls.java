package talkdesk.challenge.calls.model;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.ListIterator;

public class ManageCalls {

    private LinkedList<Calls> call = new LinkedList<>();

    public void addCall(Calls calls){

        for (Calls c:  call){
            if(calls.getCallerNumber().equals(c.getCallerNumber()) && c.getCallStatus().equals("IN_CALL"))
                //mudar para exceções
                System.out.print("Caller " + calls.getCallerNumber() + "is in call!");
            else if(calls.getCalleeNumber().equals(c.getCalleeNumber()) && c.getCallStatus().equals("IN_CALL"))
                System.out.print("Callee " + c.getCalleeNumber() + "is in call!");
            else
                this.call.add(calls);
        }

    }

}
