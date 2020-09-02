package talkdesk.challenge.calls.controller;

import org.springframework.web.bind.annotation.*;
import talkdesk.challenge.calls.model.ActualCall;
import talkdesk.challenge.calls.service.ICallService;

import java.util.List;

@RestController
public class CallController {

    private final ICallService callService;

    public CallController(ICallService callService) {
        this.callService = callService;
    }

    @PostMapping(value="/createCall", consumes = "application/json")
    public void createCall(@RequestBody ActualCall calls){
        callService.createCalls(calls);
    }

    @GetMapping(value="/endCall")
    public List<ActualCall> retrieveActiveCalls(){
        return callService.activeCalls();
    }

    @PutMapping(value="/endCall/{id}")
    public void finishCall(@PathVariable String id){
        callService.endCall(id);
    }
}
