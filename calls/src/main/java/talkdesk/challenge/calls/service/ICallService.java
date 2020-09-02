package talkdesk.challenge.calls.service;

import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.model.ActualCall;

import java.util.List;

public interface ICallService {

    void createCalls(ActualCall calls);

    List<ActualCall> activeCalls();

    void endCall(String callerNumber);
}
