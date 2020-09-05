package talkdesk.challenge.calls.service;

import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.model.ActualCall;

import java.util.List;

public interface ICallService {

    public void setCallStatus(Call call);

    void createCalls(Call calls);

    List<ActualCall> activeCalls();

    void endCall(String callerNumber);
}
