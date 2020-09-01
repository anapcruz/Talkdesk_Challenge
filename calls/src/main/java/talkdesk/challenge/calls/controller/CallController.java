package talkdesk.challenge.calls.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.service.CallService;

@RestController
@RequestMapping(value = "api")
public class CallController {

    private CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @PostMapping("/createCall")
    public void createCall(@RequestBody Call call){



        //store information into database
        //callService.createCall(call);
    }
}
