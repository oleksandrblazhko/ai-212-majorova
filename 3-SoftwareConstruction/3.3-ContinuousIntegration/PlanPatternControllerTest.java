package com.example.test.controller;

import com.example.hygimeter.config.JwtService;
import com.example.hygimeter.controller.PlanPatternController;
import com.example.hygimeter.dto.*;
import com.example.hygimeter.exception.EntityNotFoundException;
import com.example.hygimeter.exception.InvalidDataException;
import com.example.hygimeter.exception.StatusCodes;
import com.example.hygimeter.service.PlanPatternService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalTime;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PlanPatternController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureDataJpa
@AutoConfigureMockMvc
public class PlanPatternControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanPatternService planPatternService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    public void updatePlanPattern_PlanPatternExists_ReturnOK() throws Exception {
        String id = "1";

        PlanPatternDTO planPatternDTO = new PlanPatternDTO();
        planPatternDTO.setId(Integer.valueOf(id));
        planPatternDTO.setDevice("DeviceTypeA");

        MicroclimateDTO microclimate = new MicroclimateDTO();
        microclimate.setTemperature("22°C");
        microclimate.setVentilation("TypeB");
        microclimate.setLightLevel(0.8f);

        PlanParametersDTO planParameters = new PlanParametersDTO();
        planParameters.setTemperatureSked("Every 6 hours");
        planParameters.setLightsOffTime(LocalTime.of(22, 0));

        planPatternDTO.setMicroclimateDTO(microclimate);
        planPatternDTO.setPlanParametersDTO(planParameters);

        PlanPatternDTO updatedPlanPatternDTO = new PlanPatternDTO();
        updatedPlanPatternDTO.setId(planPatternDTO.getId());
        updatedPlanPatternDTO.setDevice(planPatternDTO.getDevice());
        updatedPlanPatternDTO.setMicroclimateDTO(planPatternDTO.getMicroclimateDTO());
        updatedPlanPatternDTO.setPlanParametersDTO(planPatternDTO.getPlanParametersDTO());

        RemoteResponse remoteResponse = RemoteResponse.create(true, StatusCodes.OK.name(),
                "Plan pattern has been updated successfully", List.of(planPatternDTO));

        given(planPatternService.updatePlanPattern(Integer.valueOf(id), planPatternDTO)).willReturn(updatedPlanPatternDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String planPatternDTOJson = objectMapper.writeValueAsString(planPatternDTO);

        String expectedCode = "OK";
        String expectedMessage = "Plan pattern has been updated successfully";


        mockMvc.perform(MockMvcRequestBuilders.put("/plan-pattern/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(planPatternDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].device").value("DeviceTypeA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(expectedCode));
    }

    @Test
    public void updatePlanPattern_PlanPatternNotExist() throws Exception {
        String id = "2131";

        PlanPatternDTO planPatternDTO = new PlanPatternDTO();
        planPatternDTO.setId(Integer.valueOf(id));
        planPatternDTO.setDevice("DeviceTypeX");

        MicroclimateDTO microclimate = new MicroclimateDTO();
        microclimate.setTemperature("25°C");
        microclimate.setVentilation("TypeX");
        microclimate.setLightLevel(0.5f);

        PlanParametersDTO planParameters = new PlanParametersDTO();
        planParameters.setTemperatureSked("Every 4 hours");
        planParameters.setLightsOffTime(LocalTime.of(20, 0));

        planPatternDTO.setMicroclimateDTO(microclimate);
        planPatternDTO.setPlanParametersDTO(planParameters);

        given(planPatternService.updatePlanPattern(Integer.valueOf(id), planPatternDTO))
                .willThrow(new EntityNotFoundException(StatusCodes.ENTITY_NOT_FOUND.name(), "Plan pattern not found"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String planPatternDTOJson = objectMapper.writeValueAsString(planPatternDTO);

        String expectedCode = "ENTITY_NOT_FOUND";
        String expectedMessage = "Plan pattern not found";

        mockMvc.perform(MockMvcRequestBuilders.put("/plan-pattern/{id}", id)
                        .contentType("application/json")
                        .content(planPatternDTOJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(expectedCode));
    }

    @Test
    public void updatePlanPattern_Microclimate_LightLevelExists() throws Exception {
        String id = "1";

        // Initialize original plan pattern DTO
        PlanPatternDTO originalPlanPatternDTO = new PlanPatternDTO();
        originalPlanPatternDTO.setId(Integer.valueOf(id));
        originalPlanPatternDTO.setDevice("DeviceTypeOriginal");

        MicroclimateDTO originalMicroclimate = new MicroclimateDTO();
        originalMicroclimate.setTemperature("20°C");
        originalMicroclimate.setVentilation("TypeOriginal");
        originalMicroclimate.setLightLevel(0.6f);

        PlanParametersDTO originalPlanParameters = new PlanParametersDTO();
        originalPlanParameters.setTemperatureSked("Every 3 hours");
        originalPlanParameters.setLightsOffTime(LocalTime.of(21, 0));

        originalPlanPatternDTO.setMicroclimateDTO(originalMicroclimate);
        originalPlanPatternDTO.setPlanParametersDTO(originalPlanParameters);

        // Create updated DTO with new light level
        PlanPatternDTO updatedPlanPatternDTO = new PlanPatternDTO();
        updatedPlanPatternDTO.setId(Integer.valueOf(id));
        updatedPlanPatternDTO.setDevice("DeviceTypeUpdated");

        MicroclimateDTO updatedMicroclimate = new MicroclimateDTO();
        updatedMicroclimate.setTemperature("22°C");
        updatedMicroclimate.setVentilation("TypeUpdated");
        updatedMicroclimate.setLightLevel(0.75f); // Updated light level

        PlanParametersDTO updatedPlanParameters = new PlanParametersDTO();
        updatedPlanParameters.setTemperatureSked("Every 5 hours");
        updatedPlanParameters.setLightsOffTime(LocalTime.of(23, 0));

        updatedPlanPatternDTO.setMicroclimateDTO(updatedMicroclimate);
        updatedPlanPatternDTO.setPlanParametersDTO(updatedPlanParameters);

        given(planPatternService.updatePlanPattern(Integer.valueOf(id), updatedPlanPatternDTO))
                .willReturn(updatedPlanPatternDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String updatedPlanPatternDTOJson = objectMapper.writeValueAsString(updatedPlanPatternDTO);

        String expectedCode = "OK";
        String expectedMessage = "Plan pattern has been updated successfully";

        mockMvc.perform(MockMvcRequestBuilders.put("/plan-pattern/{id}", id)
                        .contentType("application/json")
                        .content(updatedPlanPatternDTOJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].microclimateDTO.lightLevel")
                        .value(0.75f))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(expectedCode));
    }

    @Test
    public void updatePlanPattern_Microclimate_LightLevelNotExist() throws Exception {
        String id = "1";

        // Initialize original plan pattern DTO
        PlanPatternDTO originalPlanPatternDTO = new PlanPatternDTO();
        originalPlanPatternDTO.setId(Integer.valueOf(id));
        originalPlanPatternDTO.setDevice("DeviceTypeOriginal");

        MicroclimateDTO originalMicroclimate = new MicroclimateDTO();
        originalMicroclimate.setTemperature("20°C");
        originalMicroclimate.setVentilation("TypeOriginal");

        PlanParametersDTO originalPlanParameters = new PlanParametersDTO();
        originalPlanParameters.setTemperatureSked("Every 3 hours");
        originalPlanParameters.setLightsOffTime(LocalTime.of(21, 0));

        originalPlanPatternDTO.setMicroclimateDTO(originalMicroclimate);
        originalPlanPatternDTO.setPlanParametersDTO(originalPlanParameters);

        // Create updated DTO with new light level
        PlanPatternDTO updatedPlanPatternDTO = new PlanPatternDTO();
        updatedPlanPatternDTO.setId(Integer.valueOf(id));
        updatedPlanPatternDTO.setDevice("DeviceTypeUpdated");

        MicroclimateDTO updatedMicroclimate = new MicroclimateDTO();
        updatedMicroclimate.setTemperature("22°C");
        updatedMicroclimate.setVentilation("TypeUpdated");

        PlanParametersDTO updatedPlanParameters = new PlanParametersDTO();
        updatedPlanParameters.setTemperatureSked("Every 5 hours");
        updatedPlanParameters.setLightsOffTime(LocalTime.of(23, 0));
        updatedMicroclimate.setLightLevel(0f);

        updatedPlanPatternDTO.setMicroclimateDTO(updatedMicroclimate);
        updatedPlanPatternDTO.setPlanParametersDTO(updatedPlanParameters);

        given(planPatternService.updatePlanPattern(Integer.valueOf(id), updatedPlanPatternDTO))
                .willReturn(updatedPlanPatternDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String updatedPlanPatternDTOJson = objectMapper.writeValueAsString(updatedPlanPatternDTO);

        String expectedCode = "OK";
        String expectedMessage = "Plan pattern has been updated successfully";

        mockMvc.perform(MockMvcRequestBuilders.put("/plan-pattern/{id}", id)
                        .contentType("application/json")
                        .content(updatedPlanPatternDTOJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].microclimateDTO.lightLevel")
                        .value(0f))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(expectedCode));
    }

    @Test
    public void updatePlanPattern_TemperatureValidationError() throws Exception {
        String id = "1";

        // Initialize plan pattern DTO with an invalid temperature value
        PlanPatternDTO planPatternDTO = new PlanPatternDTO();
        planPatternDTO.setId(Integer.valueOf(id));
        planPatternDTO.setDevice("DeviceTypeA");

        MicroclimateDTO microclimate = new MicroclimateDTO();
        // Set an invalid temperature value (e.g., a temperature outside an acceptable range)
        microclimate.setTemperature("100912345678901234567890"); // Assuming 100°C is invalid for the context
        microclimate.setVentilation("TypeB");
        microclimate.setLightLevel(0.7f);

        PlanParametersDTO planParameters = new PlanParametersDTO();
        planParameters.setTemperatureSked("Every 2 hours");
        planParameters.setLightsOffTime(LocalTime.of(23, 0));

        planPatternDTO.setMicroclimateDTO(microclimate);
        planPatternDTO.setPlanParametersDTO(planParameters);

        // Assuming the service throws a custom ValidationException for invalid temperature values
        given(planPatternService.updatePlanPattern(Integer.valueOf(id), planPatternDTO))
                .willThrow(new InvalidDataException(StatusCodes.INVALID_DATA.name(), "Max size of temperature is 20 characters"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String planPatternDTOJson = objectMapper.writeValueAsString(planPatternDTO);

        String expectedCode = "INVALID_DATA";
        String expectedMessage = "Max size of temperature is 20 characters";

        mockMvc.perform(MockMvcRequestBuilders.put("/plan-pattern/{id}", id)
                        .contentType("application/json")
                        .content(planPatternDTOJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(expectedCode));
    }

    @Test
    public void updatePlanPattern_PlanParameters_TemperatureSkedValidationError() throws Exception {
        String id = "1";

        // Initialize plan pattern DTO with valid values
        PlanPatternDTO planPatternDTO = new PlanPatternDTO();
        planPatternDTO.setId(Integer.valueOf(id));
        planPatternDTO.setDevice("DeviceTypeA");

        MicroclimateDTO microclimate = new MicroclimateDTO();
        microclimate.setTemperature("25"); // Assuming 25°C is a valid temperature
        microclimate.setVentilation("TypeB");
        microclimate.setLightLevel(0.7f);

        PlanParametersDTO planParameters = new PlanParametersDTO();
        // Set an invalid temperature schedule value (e.g., a nonsensical or non-existent schedule)
        planParameters.setTemperatureSked("Invalid schedule Invalid schedule Invalid schedule Invalid schedule Invalid schedule Invalid scheduleInvalid scheduleInvalid scheduleInvalid scheduleInvalid scheduleInvalid schedule");
        planParameters.setLightsOffTime(LocalTime.of(23, 0));

        planPatternDTO.setMicroclimateDTO(microclimate);
        planPatternDTO.setPlanParametersDTO(planParameters);

        // Assuming the service throws a custom ValidationException for invalid temperature schedule values
        given(planPatternService.updatePlanPattern(Integer.valueOf(id), planPatternDTO))
                .willThrow(new InvalidDataException(StatusCodes.INVALID_DATA.name(), "Max size of temperature schedule is 100 characters"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String planPatternDTOJson = objectMapper.writeValueAsString(planPatternDTO);

        String expectedCode = "INVALID_DATA";
        String expectedMessage = "Max size of temperature schedule is 100 characters";

        mockMvc.perform(MockMvcRequestBuilders.put("/plan-pattern/{id}", id)
                        .contentType("application/json")
                        .content(planPatternDTOJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(expectedCode));
    }

    @Test
    public void updatePlanPattern_Microclimate_VentilationValidationError() throws Exception {
        String id = "1";

        // Initialize plan pattern DTO with valid temperature and plan parameters
        PlanPatternDTO planPatternDTO = new PlanPatternDTO();
        planPatternDTO.setId(Integer.valueOf(id));
        planPatternDTO.setDevice("DeviceTypeA");

        MicroclimateDTO microclimate = new MicroclimateDTO();
        microclimate.setTemperature("25"); // Assuming 25°C is a valid temperature
        // Set an invalid ventilation value (e.g., a value outside an acceptable range or format)
        microclimate.setVentilation("InvalidVentilationType,InvalidVentilationType,InvalidVentilationType,InvalidVentilationType,InvalidVentilationType");
        microclimate.setLightLevel(0.7f);

        PlanParametersDTO planParameters = new PlanParametersDTO();
        planParameters.setTemperatureSked("Every 2 hours");
        planParameters.setLightsOffTime(LocalTime.of(23, 0));

        planPatternDTO.setMicroclimateDTO(microclimate);
        planPatternDTO.setPlanParametersDTO(planParameters);

        // Assuming the service throws a custom ValidationException for invalid ventilation values
        given(planPatternService.updatePlanPattern(Integer.valueOf(id), planPatternDTO))
                .willThrow(new InvalidDataException(StatusCodes.INVALID_DATA.name(), "Max size of ventilation is 100 characters"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String planPatternDTOJson = objectMapper.writeValueAsString(planPatternDTO);

        String expectedCode = "INVALID_DATA";
        String expectedMessage = "Max size of ventilation is 100 characters";

        mockMvc.perform(MockMvcRequestBuilders.put("/plan-pattern/{id}", id)
                        .contentType("application/json")
                        .content(planPatternDTOJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(expectedCode));
    }

    @Test
    public void updatePlanPattern_Microclimate_HumidityValidationError() throws Exception {
        String id = "1";

        // Initialize plan pattern DTO with valid temperature and plan parameters
        PlanPatternDTO planPatternDTO = new PlanPatternDTO();
        planPatternDTO.setId(Integer.valueOf(id));
        planPatternDTO.setDevice("DeviceTypeA");

        MicroclimateDTO microclimate = new MicroclimateDTO();
        microclimate.setTemperature("25"); // Assuming 25°C is a valid temperature
        microclimate.setVentilation("TypeB");
        microclimate.setLightLevel(0.7f);

        HumidityDTO humidityDTO = new HumidityDTO();
        humidityDTO.setRelativeHumidity(0f);
        humidityDTO.setAbsoluteHumidity(-22.1f);

        microclimate.setHumidity(humidityDTO);

        // Set invalid humidity values (e.g., values outside an acceptable range)
        microclimate.getHumidity().setAbsoluteHumidity(-22.1f); // Invalid absolute humidity
        microclimate.getHumidity().setRelativeHumidity(0f); // Invalid relative humidity

        PlanParametersDTO planParameters = new PlanParametersDTO();
        planParameters.setTemperatureSked("Every 2 hours");
        planParameters.setLightsOffTime(LocalTime.of(23, 0));

        planPatternDTO.setMicroclimateDTO(microclimate);
        planPatternDTO.setPlanParametersDTO(planParameters);

        given(planPatternService.updatePlanPattern(Integer.valueOf(id), planPatternDTO))
                .willThrow(new InvalidDataException(StatusCodes.INVALID_DATA.name(), "Relative humidity must be not null and greater than 0 on updating parameters"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String planPatternDTOJson = objectMapper.writeValueAsString(planPatternDTO);

        String expectedCode = "INVALID_DATA";
        String expectedMessage = "must be greater than or equal to 1";


        mockMvc.perform(MockMvcRequestBuilders.put("/plan-pattern/{id}", id)
                        .contentType("application/json")
                        .content(planPatternDTOJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(expectedCode));
    }

    @Test
    public void updatePlanPattern_PlanParameters_LightsTurnOffValidationError() throws Exception {
        String id = "1";

        // Initialize plan pattern DTO with valid microclimate settings
        PlanPatternDTO planPatternDTO = new PlanPatternDTO();
        planPatternDTO.setId(Integer.valueOf(id));
        planPatternDTO.setDevice("DeviceTypeA");

        MicroclimateDTO microclimate = new MicroclimateDTO();
        microclimate.setTemperature("25"); // Assuming 25°C is a valid temperature
        microclimate.setVentilation("TypeB");
        microclimate.setLightLevel(0.7f);
        HumidityDTO humidityDTO = new HumidityDTO();
        humidityDTO.setRelativeHumidity(50f);
        humidityDTO.setAbsoluteHumidity(10f);
        microclimate.setHumidity(humidityDTO);

        PlanParametersDTO planParameters = new PlanParametersDTO();
        planParameters.setTemperatureSked("Every 2 hours");
        // Set an invalid lights off time (e.g., a time outside acceptable range)
        planParameters.setLightsOffTime(null);

        planPatternDTO.setMicroclimateDTO(microclimate);
        planPatternDTO.setPlanParametersDTO(planParameters);

        given(planPatternService.updatePlanPattern(Integer.valueOf(id), planPatternDTO))
                .willThrow(new InvalidDataException(StatusCodes.INVALID_DATA.name(), "Time when lights go off must be not null on updating parameters"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String planPatternDTOJson = objectMapper.writeValueAsString(planPatternDTO);

        String expectedCode = "INVALID_DATA";
        String expectedMessage = "Time when lights go off must be not null on updating parameters";

        mockMvc.perform(MockMvcRequestBuilders.put("/plan-pattern/{id}", id)
                        .contentType("application/json")
                        .content(planPatternDTOJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMessage").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(expectedCode));
    }


}
