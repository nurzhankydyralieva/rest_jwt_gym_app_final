package com.epam.xstack.models.dto.training_dto.request;

import com.epam.xstack.models.dto.trainee_dto.response.TraineeDTO;
import com.epam.xstack.models.dto.training_type_dto.TrainingTypeDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainerTrainingDTO {
    @NotEmpty(message = "Training name should not be empty")
    String trainingName;
    @NotBlank
    Date trainingDate;
    @NotEmpty(message = "Training type should not be empty")
    TrainingTypeDTO trainingType;
    @NotBlank
    Number trainingDuration;
    TraineeDTO trainee;
}
