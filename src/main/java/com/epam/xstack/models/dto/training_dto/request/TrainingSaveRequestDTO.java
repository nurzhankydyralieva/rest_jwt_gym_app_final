package com.epam.xstack.models.dto.training_dto.request;

import com.epam.xstack.models.entity.Trainee;
import com.epam.xstack.models.entity.Trainer;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainingSaveRequestDTO {
    @NotBlank(message = "Trainee user name should not be empty")
    Trainee traineeUserName;
    @NotBlank(message = "Trainer user name should not be empty")
    Trainer trainerUserName;
    @NotNull(message = "Training name should not be empty")
    String trainingName;
    Date trainingDate;
    Number trainingDuration;
}
