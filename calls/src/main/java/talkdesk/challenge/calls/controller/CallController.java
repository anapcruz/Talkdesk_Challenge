package talkdesk.challenge.calls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import talkdesk.challenge.calls.model.ActualCall;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.repository.CallRepository;
import talkdesk.challenge.calls.service.ICallService;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CallController {

    private final CallRepository callRepository;
    private final ICallService callService;

    public CallController(CallRepository callRepository, ICallService callService) {
        this.callRepository = callRepository;
        this.callService = callService;
    }

    /**

    @PostMapping(value = "/create")
    ResponseEntity<Call> createGroup(@RequestBody Call call) throws URISyntaxException {
        callService.setCallStatus(call);
        Call test = new Call(call.getCallerNumber(),call.getCalleeNumber(), call.getType());
        Call result = callRepository.save(test);
        return ResponseEntity.created(new URI("/create/" + result.getId()))
                .body(result);
    }


    @GetMapping(value = "/calls")
    Collection<Call> groups() {
        return callRepository.findAll();
    }
    */

    @GetMapping(value = "/")
    public String retrieveActiveCalls(Model model){
        model.addAttribute("data", callRepository.findAll());
        return "index";
    }

    @PostMapping(value = "/createCall", consumes = "application/json")
    public void createCall(@RequestBody Call calls){
        callService.createCalls(calls);
    }

    @PutMapping(value = "/endCall/{id}")
    public void finishCall(@PathVariable(value = "id") String id){
        callService.endCall(id);
    }

    @DeleteMapping(value = "/deleteCallByCaller/{callerID}")
    public void deleteCallByCaller(@PathVariable(value = "callerID") String caller){
        callRepository.deleteCallByCallerNumber(caller);
    }

    @DeleteMapping(value = "/deleteCallByCallee/{calleeID}")
    public void deleteCallByCallee(@PathVariable(value = "calleeID") String callee){
        callRepository.deleteCallByCalleeNumber(callee);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(value = "id") long id){
        callRepository.deleteById(id);
    }

    @GetMapping(value = "/getAllCalls")
    public ResponseEntity<Map<String, Object>> retrieveAllCalls(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
            ){
        try {
            List<Call> calls;
            Pageable paging = PageRequest.of(page, size);

            Page<Call> pageCalls;

            if(type == null)
                pageCalls = callRepository.findAll(paging);
            else
                pageCalls = callRepository.findByType(type, paging);

            calls = pageCalls.getContent();

            if(calls.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            Map<String, Object> response = new HashMap<>();
            response.put("calls",  calls);
            response.put("currentPage", pageCalls.getNumber());
            response.put("totalItems", pageCalls.getTotalElements());
            response.put("totalPages", pageCalls.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
