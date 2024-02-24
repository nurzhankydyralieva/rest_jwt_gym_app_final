package com.epam.xstack.controller;

import com.epam.xstack.aspects.training_aspects.annotations.SaveTrainingEndPointAspectAnnotation;
import com.epam.xstack.exceptions.validation.NotNullValidation;
import com.epam.xstack.models.dto.training_dto.request.TrainingSaveRequestDTO;
import com.epam.xstack.models.dto.training_dto.response.TrainingSaveResponseDTO;
import com.epam.xstack.service.training_service.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
@Api(tags = "Training controller")
public class TrainingController {
    private final TrainingService trainingService;
    private final NotNullValidation validation;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Training saved successfully"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @ApiOperation(value = "Save Training to database")
    @SaveTrainingEndPointAspectAnnotation
    @PostMapping("/save")
    public ResponseEntity<TrainingSaveResponseDTO> saveTraining(@RequestBody TrainingSaveRequestDTO requestDTO, BindingResult result) {
        validation.nullValidation(result);
        return new ResponseEntity<>(trainingService.saveTraining(requestDTO), HttpStatus.CREATED);
    }
}
