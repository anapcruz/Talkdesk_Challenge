package talkdesk.challenge.calls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.service.CallInterface;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CallController {

    @Autowired
    private CallInterface callService;

    /**
     * The default value of rows of the table (FrontEnd).
     */
    public static final int DEFAULT_PAGE_SIZE = 5;

    /**
     * Array with all possible row sizes supported in this project (FrontEnd).
     */
    private static final int[] PAGE_SIZES = {5, 10, 15, 20};

    /**
     * Default type call (FrontEnd).
     */
    private static final String DEFAULT_TYPE = "ALL";

    /**
     * Shows the index page.
     * The index page shows all ongoing calls.
     * Requests the parameters type and page size to implement on the table.
     * @param model represents a Java object carrying data
     * @param type call type
     * @param pageSize max number of the rows per "view" of the table
     * @return index page
     */
    @GetMapping("/")
    public String viewHomePage(Model model,
                               @RequestParam(value = "type",  defaultValue = DEFAULT_TYPE) String type,
                               @RequestParam(value = "pageSize")  Optional<Integer> pageSize){
        return findPaginated(1, pageSize, type, model);
    }

    /**
     * Shows a new window with a form to create a new call.
     * @param model represents a Java object carrying data
     * @return new_call page
     */
    @GetMapping("/showNewCallForm")
    public String showNewCallForm(Model model){
        Call call = new Call();
        model.addAttribute("call", call);

        return "new_call";
    }

    /**
     * Post new calls.
     * When the submit button is selected, if exists any errors continue on the same page.
     * If not returns to the main page (index page).
     * @param call all data of the call
     * @param bindingResult check if there is any errors
     * @return index page
     */
    @PostMapping("/saveCall")
    public String saveCall( @Valid @ModelAttribute("call") Call call, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "new_call";
        }
        // save call to database
        callService.saveCall(call);
        return "redirect:/";
    }

    /**
     * Terminate the active call.
     * @param id call id
     * @return index page
     */
    @GetMapping("/endCallById/{id}")
    public String endCallById(@PathVariable(value = "id") long id){
        //get call from the service
        this.callService.endCallById(id);
        return "redirect:/";
    }

    /**
     * Show history page.
     * @param model represents a Java object carrying data
     * @param type call type
     * @param pageSize max number of the rows per "view" of the table
     * @return history page
     */
    @GetMapping("/history")
    public String viewHistoryPage(Model model,
                                  @RequestParam(value = "type",  defaultValue = DEFAULT_TYPE) String type,
                                  @RequestParam(value = "pageSize")  Optional<Integer> pageSize){
        return paginatedHistory(1, pageSize, type, model);
    }

    /**
     * Delete call.
     * @param id call id
     * @return history page
     */
    @GetMapping("/deleteCallById/{id}")
    public String deleteCallByID(@PathVariable(value = "id") long id){
        //get call from the service
        this.callService.deleteCallByID(id);

        return "redirect:/history";
    }

    /**
     * Insert all the data into the index page, such as, information related to active calls and pagination
     * @param pageNumber number of pages represented in the table
     * @param pageSize max number of the rows per "view" of the table
     * @param type call type
     * @param model represents a Java object carrying data
     * @return index page
     */
    @GetMapping("/page/")
    public String findPaginated(@RequestParam(value = "pageNumber") int pageNumber, Optional<Integer> pageSize, String type, Model model){
        // Evaluate page size. If requested parameter is null, return initial
        int evalPageSize = pageSize.orElse(DEFAULT_PAGE_SIZE);
        //pagination related all calls with the status "ONGOING_CALL"
        Page<Call> page = callService.findCallPagination(pageNumber, evalPageSize,type, "ONGOING_CALL");
        //list of calls
        List<Call> listCalls = page.getContent();

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

    /**
     * Insert all the data into the history page, such as, information related to active calls and pagination
     * @param pageNumber number of pages represented in the table
     * @param pageSize max number of the rows per "view" of the table
     * @param type call type
     * @param model represents a Java object carrying data
     * @return history page
     */
    @GetMapping("/pagehist/")
    public String paginatedHistory(@RequestParam(value = "pageNumber") int pageNumber, Optional<Integer> pageSize, String type, Model model){
        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(DEFAULT_PAGE_SIZE);
        //pagination related all calls with the status "ENDED_CALL"
        Page<Call> page = callService.findCallPagination(pageNumber, evalPageSize,type, "ENDED_CALL");
        //list of calls
        List<Call> listCalls = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCalls", listCalls);
        model.addAttribute("page", page);
        model.addAttribute("type", type);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        return "history";
    }

    /**
     * Shows statistics page with information related to:
     * - total call duration;
     * - total number of calls;
     * - number of calls by caller and callee number;
     * - total cost by type.
     * @param type call type
     * @param model represents a Java object carrying data
     * @return statistics page
     */
    @GetMapping("/statistics")
    public String statisticsPage(String type, Model model){

        Map<String, String> mapCallDurationInbound = callService.getDurationCallByType("INBOUND");
        Map<String, String> mapCallDurationOutbound = callService.getDurationCallByType("OUTBOUND");

        long totalCalls = callService.getTotalNumberOfCalls();
        Map<String, Map<String, Long>> totalCallsByCallerNumber = callService.getTotalCallsByCallerOrCalleeNumber("Caller");
        Map<String, Map<String, Long>> totalCallsByCalleeNumber = callService.getTotalCallsByCallerOrCalleeNumber("Callee");
        Map<String, Double> totalCostByOutbound = callService.getTotalCostByType();

        model.addAttribute("callDurationInbound", mapCallDurationInbound);
        model.addAttribute("callDurationOutbound", mapCallDurationOutbound);
        model.addAttribute("totalNumber", totalCalls);
        model.addAttribute("callsByCallerDate", totalCallsByCallerNumber);
        model.addAttribute("callsByCalleeDate", totalCallsByCalleeNumber);
        model.addAttribute("totalCost", totalCostByOutbound);

        return "statistics";
    }

}
