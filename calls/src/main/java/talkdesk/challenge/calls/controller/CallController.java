package talkdesk.challenge.calls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.service.CallService;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class CallController {

    @Autowired
    private CallService callService;

    public static final int DEFAULT_PAGE_SIZE = 2;
    private static final int[] PAGE_SIZES = {1, 3, 4, 5};
    private static final String DEFAULT_TYPE = "ALL";

    //display list of ongoing calls
    @GetMapping("/")
    public String viewHomePage(Model model,
                               @RequestParam(value = "type",  defaultValue = DEFAULT_TYPE) String type,
                               @RequestParam(value = "pageSize")  Optional<Integer> pageSize){
        //model.addAttribute("listCalls", callService.getAllOngoingCalls());
        return findPaginated(1, pageSize, type, model);
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

    @GetMapping("/page/")
    public String findPaginated(@RequestParam(value = "pageNumber") int pageNumber, Optional<Integer> pageSize, String type, Model model){
        //
        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(DEFAULT_PAGE_SIZE);
        Page<Call> page = callService.findPaginatedOngoingCall(pageNumber, evalPageSize,type);

        List<Call> listCalls = page.getContent();
        System.out.println(listCalls);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCalls", listCalls);
        model.addAttribute("page", page);
        model.addAttribute("type", type);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        return "index";
    }



}
