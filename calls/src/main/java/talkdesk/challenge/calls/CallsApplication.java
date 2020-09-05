package talkdesk.challenge.calls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import talkdesk.challenge.calls.model.Call;
import talkdesk.challenge.calls.repository.CallRepository;

@SpringBootApplication
public class CallsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CallsApplication.class, args);
    }
    @Autowired
    private CallRepository callRepository;

    public void run(String... args) throws Exception{
        this.callRepository.save(new Call("1234", "1222", "INBOUND"));
    }
}
