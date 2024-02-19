package com.epam.xstack.models.dto.trainer_dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainerActivateDeActivateDTO {
    @NotEmpty(message = "User name should not be empty")
    String userName;
    @NotNull
    Boolean isActive;
}
