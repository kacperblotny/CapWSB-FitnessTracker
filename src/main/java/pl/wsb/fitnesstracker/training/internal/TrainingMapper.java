package pl.wsb.fitnesstracker.training.internal;

import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.user.api.UserSimpleDto;

@Component
class TrainingMapper {

    TrainingDto toDto(Training training) {
        if (training == null) {
            return null;
        }

        UserSimpleDto userDto = null;
        if (training.getUser() != null) {
            userDto = new UserSimpleDto(
                    training.getUser().getId(),
                    training.getUser().getFirstName(),
                    training.getUser().getLastName()
            );
        }

        return new TrainingDto(
                training.getId(),
                userDto,
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        );
    }
}