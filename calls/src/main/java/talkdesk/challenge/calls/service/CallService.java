package talkdesk.challenge.calls.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.model.Calls;
import talkdesk.challenge.calls.model.ManageCalls;
import talkdesk.challenge.calls.repository.CallRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CallService implements ICallService{

    @Autowired
    private final CallRepository callRepository;

    Map<String, ManageCalls> mapCalls =  new HashMap<>();

    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }


    public void createCalls(Calls calls){
        if (calls.getCallerNumber().equals("")) throw new IllegalArgumentException("Caller number should not be empty");
        if (calls.getCalleeNumber().equals("")) throw new IllegalArgumentException("Callee number should not be empty");
        if (calls.getCallType().equals("")) throw new IllegalArgumentException("Call type should not be empty");

        if(!mapCalls.containsKey(calls.getCallerNumber()))
            mapCalls.put(calls.getCallerNumber(), new ManageCalls());

        ManageCalls manageCalls = mapCalls.get(calls.getCallerNumber());

        manageCalls.addCall(calls);
        System.out.println("mapCalls " + mapCalls);

    }


    public void endCall(Call call){

        this.callRepository.save(call);
    }

    @Override
    public List<Call> listCalls() {
        return null;
    }
}
