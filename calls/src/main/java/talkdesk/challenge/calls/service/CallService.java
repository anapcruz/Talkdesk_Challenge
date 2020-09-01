package talkdesk.challenge.calls.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.model.Calls;
import talkdesk.challenge.calls.model.ManageCalls;
import talkdesk.challenge.calls.repository.CallRepository;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Service
public class CallService implements ICallService{

    @Autowired
    private final CallRepository callRepository;

    Map<String, ManageCalls> mapCalls =  new HashMap<>();

    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public void createCalls(Calls calls){
        if(!mapCalls.containsKey(calls.getCallerNumber()))
            mapCalls.put(calls.getCallerNumber(), new ManageCalls());
    }


    public void endCall(Call call){
        this.callRepository.save(call);
    }

    @Override
    public List<Call> listCalls() {
        return null;
    }
}
