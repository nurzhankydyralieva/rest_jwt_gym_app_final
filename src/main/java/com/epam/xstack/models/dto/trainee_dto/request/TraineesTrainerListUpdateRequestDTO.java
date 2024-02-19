package com.epam.xstack.models.dto.trainee_dto.request;

import com.epam.xstack.models.dto.trainer_dto.response.TrainerDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraineesTrainerListUpdateRequestDTO {
    @NotEmpty(message = "User name should not be empty")
    private String userName;
    Collection<TrainerDTO> trainers;

}
