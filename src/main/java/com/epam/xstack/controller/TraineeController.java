package com.epam.xstack.controller;

import com.epam.xstack.aspects.trainee_aspects.end_points_aspects.annotations.*;
import com.epam.xstack.models.dto.trainee_dto.request.*;
import com.epam.xstack.models.dto.trainee_dto.response.*;
import com.epam.xstack.service.trainee_service.TraineeService;
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
@RequestMapping("/trainees")
@RequiredArgsConstructor
@Api(tags = "Trainee controller")
public class TraineeController {
    private final TraineeService traineeService;
    private final NotNullValidation validation;
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User saved successfully"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null")
    })
    @ApiOperation(value = "Save Trainee to database")
    @SaveTraineeEndPointAspectAnnotation
    @PostMapping("/save")
    public ResponseEntity<TraineeRegistrationResponseDTO> saveTrainee(@Valid @RequestBody TraineeRegistrationRequestDTO requestDTO, BindingResult result) {
        validation.nullValidation(result);
        return new ResponseEntity<>(traineeService.saveTrainee(requestDTO), HttpStatus.CREATED);
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User selected successfully"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Get Trainee by user name")
    @SelectTraineeProfileAspectAnnotation
    @GetMapping("/{id}")
    public ResponseEntity<TraineeProfileSelectResponseDTO> selectTraineeProfile(@PathVariable("id") UUID id, @Valid @RequestBody TraineeProfileSelectRequestDTO requestDTO, BindingResult result) {
        validation.userNotNullValidation(result);
        return new ResponseEntity<>(traineeService.selectTraineeProfileByUserName(id, requestDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated successfully"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Update Trainee in database")
    @UpdateTraineeEndPointAspectAnnotation
    @PutMapping("/update/{id}")
    public ResponseEntity<TraineeProfileUpdateResponseDTO> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody TraineeProfileUpdateRequestDTO requestDTO, BindingResult result) {
        validation.nullValidation(result);
        return new ResponseEntity<>(traineeService.updateTraineeProfile(id, requestDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated successfully"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @ApiOperation(value = "Update Trainee's Trainer List in database")
    @UpdateTTListEndPointAspectAnnotation
    @PutMapping("/update-list/{id}")
    public ResponseEntity<TraineesTrainerListUpdateResponseDTO> updateTraineesTrainerList(@PathVariable("id") UUID id, @Valid @RequestBody TraineesTrainerListUpdateRequestDTO requestDTO, BindingResult result) {
        validation.nullValidation(result);
        return new ResponseEntity<>(traineeService.updateTraineesTrainerList(id, requestDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User activated successfully"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Update active or de active Trainee")
    @ActiveDeActiveTraineeEndPointAspectAnnotation
    @PatchMapping("/{id}")
    public ResponseEntity<TraineeOkResponseDTO> updateActivateDe_ActivateTrainee(@PathVariable("id") UUID id, @Valid @RequestBody TraineeActivateDeActivateDTO dto, BindingResult result) {
        validation.userNotNullValidation(result);
        return new ResponseEntity<>(traineeService.activateDe_ActivateTrainee(id, dto), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User deleted successfully"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Delete Trainee by user name")
    @DeleteEndPointAspectAnnotation
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TraineeOkResponseDTO> deleteTraineeByUserName(@PathVariable("id") UUID id, @Valid @RequestBody TraineeProfileSelectRequestDTO requestDTO, BindingResult result) {
        validation.userNotNullValidation(result);
        return new ResponseEntity<>(traineeService.deleteTraineeByUserName(id, requestDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User selected successfully"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Get Trainee Trainings List")
    @SelectTraineeTLEndPointAspectAnnotation
    @GetMapping("/select/{id}")
    public ResponseEntity<TraineeTrainingsListResponseDTO> selectTraineeTrainingsList(@PathVariable("id") UUID id, @Valid @RequestBody TraineeTrainingsListRequestDTO requestDTO, BindingResult result) {
        validation.userNotNullValidation(result);
        return new ResponseEntity<>(traineeService.selectTraineeTrainingsList(id, requestDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User selected successfully"),
            @ApiResponse(code = 403, message = "Access denied, check user name or id"),
            @ApiResponse(code = 401, message = "Bad credentials"),
            @ApiResponse(code = 422, message = "User or password is null"),
            @ApiResponse(code = 404, message = "User with user name or id not found")
    })
    @ApiOperation(value = "Get not assigned on trainee active trainers.")
    @NotAssignedTraineeEndPointAspectAnnotation
    @GetMapping("/active-not-assigned/{id}")
    public ResponseEntity<TraineesTrainerActiveAndNotAssignedResponseDTO> selectNotAssignedOnTraineeActiveTrainers(@PathVariable("id") UUID id, @Valid @RequestBody TraineesTrainerActiveAndNotAssignedRequestDTO userName, BindingResult result) {
        validation.userNotNullValidation(result);
        return new ResponseEntity<>(traineeService.selectNotAssignedOnTraineeActiveTrainers(id, userName), HttpStatus.OK);
    }
}
