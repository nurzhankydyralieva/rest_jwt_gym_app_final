package com.epam.xstack.models.dto.training_dto.request;

import com.epam.xstack.models.dto.trainer_dto.response.TrainerDTO;
import com.epam.xstack.models.dto.training_type_dto.TrainingTypeDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraineeTrainingDTO {
    String trainingName;
    Date trainingDate;
    TrainingTypeDTO trainingType;

    Number trainingDuration;
    TrainerDTO trainer;
}
