package talkdesk.challenge.calls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.repository.CallRepository;

import java.sql.Timestamp;

@SpringBootApplication
public class CallsApplication implements CommandLineRunner {

    public static void main(String[] args){
        SpringApplication.run(CallsApplication.class, args);
    }

    @Autowired
    private CallRepository callRepository;

    public void run(String... args) throws Exception{
        this.callRepository.save(createCall("231441231", "231441232", "2020-09-07 16:43:19.77", "2020-09-07 17:43:19.77", "INBOUND", "ENDED_CALL"));
        this.callRepository.save(createCall("231441231", "231441232", "2020-09-07 09:00:19.77", "2020-09-07 10:30:30.77", "OUTBOUND", "ENDED_CALL"));
        this.callRepository.save(createCall("231441234", "239441236", "2020-09-07 12:30:19.77", "2020-09-07 14:00:19.77", "INBOUND", "ENDED_CALL"));
        this.callRepository.save(createCall("231441232", "236441236", "2020-09-07 20:42:19.77", "2020-09-07 21:00:02.77", "OUTBOUND", "ENDED_CALL"));
        this.callRepository.save(createCall("231441231", "236441236", "2020-09-08 10:42:19.77", "2020-09-08 11:20:02.77", "INBOUND", "ENDED_CALL"));
        this.callRepository.save(createCall("912345677", "923456789", "2020-09-08 14:20:19.77", "2020-09-08 14:22:02.77", "OUTBOUND", "ENDED_CALL"));
        this.callRepository.save(createCall("923456789", "923456789", "2020-09-08 17:42:19.77", "2020-09-08 18:00:02.77", "INBOUND", "ENDED_CALL"));
    }
    /**
     * Auxiliary function to insert data into database
     * @param callerNumber caller number
     * @param calleeNumber callee number
     * @param start start timestamp
     * @param end end timestamp
     * @param type call type
     * @param status call status
     * @return call
     */
    private Call createCall(String callerNumber, String calleeNumber, String start, String end, String type, String status){
        Call call = new Call();
        call.setCallerNumber(callerNumber);
        call.setCalleeNumber(calleeNumber);
        call.setStartTime(Timestamp.valueOf(start));
        call.setEndTime(Timestamp.valueOf(end));
        call.setType(type);
        call.setStatus(status);
        return call;
    }
}
