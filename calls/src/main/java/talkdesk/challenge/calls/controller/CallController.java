package talkdesk.challenge.calls.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.model.Calls;
import talkdesk.challenge.calls.service.CallService;
import talkdesk.challenge.calls.service.ICallService;

@RestController
public class CallController {

    @Autowired
    private ICallService callService;

    @PostMapping(value="/createCall", consumes = "application/json")
    public void createCall(@RequestBody Calls calls){
        callService.createCalls(calls);
    }
}
