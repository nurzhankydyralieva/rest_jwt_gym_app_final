package com.epam.xstack.controller;

import com.epam.xstack.aspects.trainer_aspects.end_points_aspects.annotations.*;
import com.epam.xstack.models.dto.trainer_dto.request.*;
import com.epam.xstack.models.dto.trainer_dto.response.*;
import com.epam.xstack.service.trainer_service.TrainerService;
import com.epam.xstack.validation.NotNullValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
@Api(tags = "Trainer controller")
public class TrainerController {
    private final TrainerService trainerService;
    private final NotNullValidation validation;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User saved successfully"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null")
    })
    @ApiOperation(value = "Save Trainer to database")
    @SaveTrainerEndPointAspectAnnotation
    @PostMapping("/save")
    public ResponseEntity<TrainerRegistrationResponseDTO> saveTrainee(@Valid @RequestBody TrainerRegistrationRequestDTO requestDTO, BindingResult result) {
        validation.nullValidation(result);
        return new ResponseEntity<>(trainerService.saveTrainer(requestDTO), HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User selected successfully"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Get Trainer by user name")
    @SelectTrainerProfileAspectAnnotation
    @GetMapping("/{id}")
    public ResponseEntity<TrainerProfileSelectResponseDTO> selectTrainerProfile(@PathVariable("id") UUID id, @Valid @RequestBody TrainerProfileSelectRequestDTO requestDTO, BindingResult result) {
        validation.userNotNullValidation(result);
        return new ResponseEntity<>(trainerService.selectTrainerProfileByUserName(id, requestDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated successfully"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Update Trainer in database")
    @UpdateTrainerEndPointAspectAnnotation
    @PutMapping("/update/{id}")
    public ResponseEntity<TrainerProfileUpdateResponseDTO> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody TrainerProfileUpdateRequestDTO requestDTO, BindingResult result) {
        validation.nullValidation(result);
        return new ResponseEntity<>(trainerService.updateTrainerProfile(id, requestDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User activated successfully"),
            @ApiResponse(code = 403, message = "Access denied, check user name or id"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Update active or de active Trainer")
    @ActiveDeActiveTrainerEndPointAspectAnnotation
    @PatchMapping("/{id}")
    public ResponseEntity<TrainerOkResponseDTO> updateActivateDe_ActivateTrainer(@PathVariable("id") UUID id, @Valid @RequestBody TrainerActivateDeActivateDTO dto, BindingResult result) {
        validation.nullValidation(result);
        return new ResponseEntity<>(trainerService.activateDe_ActivateTrainer(id, dto), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User selected successfully"),
            @ApiResponse(code = 403, message = "Access denied, check user name or id"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Get Trainer Trainings List")
    @SelectTrainerTLEndPointAspectAnnotation
    @GetMapping("/select/{id}")
    public ResponseEntity<TrainerTrainingsListResponseDTO> selectTrainerTrainingsList(@PathVariable("id") UUID id, @Valid @RequestBody TrainerTrainingsListRequestDTO requestDTO, BindingResult result) {
        validation.userNotNullValidation(result);
        return new ResponseEntity<>(trainerService.selectTrainerTrainingsList(id, requestDTO), HttpStatus.OK);
    }
}
