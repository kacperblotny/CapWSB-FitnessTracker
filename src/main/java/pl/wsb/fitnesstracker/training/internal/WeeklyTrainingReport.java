package pl.wsb.fitnesstracker.training.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingRepository;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class WeeklyTrainingReport {

    private static final Logger log = LoggerFactory.getLogger(WeeklyTrainingReport.class);

    private final UserProvider userProvider;
    private final TrainingRepository trainingRepository;

    public WeeklyTrainingReport(UserProvider userProvider, TrainingRepository trainingRepository) {
        this.userProvider = userProvider;
        this.trainingRepository = trainingRepository;
    }

    // @Scheduled(cron = "0 0 8 * * MON") // every monday at 8:001
    @Scheduled(fixedRate = 10000)

    public void generateReport() {
        log.info("generating weekly report");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date oneWeekAgo = calendar.getTime();

        List<User> users = userProvider.findAllUsers();

        for (User user : users) {
            List<Training> recentTrainings = trainingRepository.findAllByUserIdAndEndTimeAfter(user.getId(), oneWeekAgo);

            log.info("user: {} {} (email: {})", user.getFirstName(), user.getLastName(), user.getEmail());

            if (recentTrainings.isEmpty()) {
                log.info("  -> no sessions in past week");
            } else {
                log.info("  -> found {} sessions:", recentTrainings.size());
                for (Training training : recentTrainings) {
                    log.info("     * training ID: {}, date: {}, distance: {} km",
                            training.getId(),
                            training.getEndTime(),
                            training.getDistance());
                }
            }
        }

        log.info("report generated");
    }
}