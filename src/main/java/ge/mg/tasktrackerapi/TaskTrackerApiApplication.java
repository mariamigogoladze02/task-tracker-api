package ge.mg.tasktrackerapi;

import ge.mg.tasktrackerapi.config.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class TaskTrackerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApiApplication.class, args);
    }

}
