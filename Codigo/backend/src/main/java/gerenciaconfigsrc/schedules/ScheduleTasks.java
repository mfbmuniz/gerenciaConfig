package gerenciaconfigsrc.schedules;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTasks {

    @Autowired

    @Scheduled(cron = "0 1 1 * * *")
    private void create(){

    }
}
