package com.epam.xstack.controller;

import com.epam.xstack.aspects.training_type.annotations.SaveTrainingTypeEndPointAspectAnnotation;
import com.epam.xstack.aspects.training_type.annotations.SelectAllTrainingTypeEndPointAspectAnnotation;
import com.epam.xstack.models.dto.training_type_dto.TrainingTypeDTO;
import com.epam.xstack.service.training_type_service.TrainingTypeService;
import com.epam.xstack.exceptions.validation.NotNullValidation;
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
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/training_types")
@Api(tags = "Training type controller")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;
    private final NotNullValidation validation;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Training type saved successfully"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @ApiOperation(value = "Save Training Type to database")
    @SaveTrainingTypeEndPointAspectAnnotation
    @PostMapping("/save")
    public ResponseEntity<TrainingTypeDTO> save(@Valid @RequestBody TrainingTypeDTO trainingTypeDTO, BindingResult result) {
        validation.nullValidation(result);
        return new ResponseEntity<>(trainingTypeService.save(trainingTypeDTO), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Training type selected successfully"),
            @ApiResponse(code = 401, message = "Bad credentials")
    })
    @ApiOperation(value = "Get all Trainings")
    @SelectAllTrainingTypeEndPointAspectAnnotation
    @GetMapping("/all")
    public ResponseEntity<List<TrainingTypeDTO>> findAll() {
        return new ResponseEntity<>(trainingTypeService.findAll(), HttpStatus.FOUND);
    }
}
