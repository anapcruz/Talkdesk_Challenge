package talkdesk.challenge.calls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.service.CallService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CallController {

    @Autowired
    private CallService callService;

    //display list of ongoing calls
    @GetMapping("/")
    public String viewHomePage(Model model){
        //model.addAttribute("listCalls", callService.getAllOngoingCalls());
        return findPaginated(1, model);
    }

    @GetMapping("/showNewCallForm")
    public String showNewCallForm(Model model){
        Call call = new Call();
        model.addAttribute("call", call);

        return "new_call";
    }

    @PostMapping("/saveCall")
    public String saveCall( @Valid @ModelAttribute("call") Call call, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "new_call";
        }
        // save call to database
        callService.saveCall(call);
        return "redirect:/";
    }

    @GetMapping("/endCallById/{id}")
    public String endCallById(@PathVariable(value = "id") long id){
        //get call from the service
        this.callService.endCallById(id);
        return "redirect:/";
    }

    @GetMapping("/history")
    public String viewHistoryPage(Model model){
        model.addAttribute("listCalls", callService.getAllCalls());
        return "history";
    }

    @GetMapping("/deleteCallById/{id}")
    public String deleteCallByID(@PathVariable(value = "id") long id){

        //get call from the service
        this.callService.deleteCallByID(id);
        return "redirect:/history";
    }

    @GetMapping("/page/{pageNumber}")
    public String findPaginated(@PathVariable(value = "pageNumber") int pageNumber, Model model){
        int pageSize = 1;

        Page<Call> page = callService.findPaginated(pageNumber, pageSize);
        List<Call> listCalls = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCalls", listCalls);
        model.addAttribute("page", page);
        return "index";
    }

}
