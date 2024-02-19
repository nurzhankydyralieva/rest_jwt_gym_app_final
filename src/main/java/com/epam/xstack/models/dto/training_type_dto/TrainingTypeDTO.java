package com.epam.xstack.models.dto.training_type_dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainingTypeDTO {
    Long id;
    @NotBlank
    String trainingType;
}
