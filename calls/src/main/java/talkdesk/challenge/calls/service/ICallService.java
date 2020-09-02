package talkdesk.challenge.calls.service;

import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.model.Calls;

import java.util.List;

public interface ICallService {

    void createCalls(Calls calls);

    public List<Call> listCalls();
}
