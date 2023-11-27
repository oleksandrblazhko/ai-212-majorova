import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlanPatternServiceImplTest {

    @Mock
    private PlanPatternRepository planPatternRepository;

    @Mock
    private PlanPatternMapper planPatternMapper;

    @InjectMocks
    private PlanPatternServiceImpl planPatternService;

    private PlanPattern validPlanPattern;
    private PlanPatternDTO validPlanPatternDTO;
    private Microclimate microclimate;
    private PlanParameters planParameters;
    private Humidity humidity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup valid data
        microclimate = new Microclimate();
        microclimate.setCurrentTemperature("18°C"); 
        microclimate.setVentilationType("Natural"); 
        microclimate.setLightLevel(10); 

        humidity = new Humidity();
        humidity.setAbsoluteHumidity(5);
        humidity.setRelativeHumidity(50); 
        microclimate.setHumidity(humidity);

        planParameters = new PlanParameters();
        planParameters.setTemperatureRegime("Normal");
        planParameters.setLightOffTime("22:00"); 

        validPlanPattern = new PlanPattern();
        validPlanPattern.setMicroclimate(microclimate);
        validPlanPattern.setPlanParameters(planParameters);

        validPlanPatternDTO = new PlanPatternDTO();
        // Convert validPlanPattern to DTO (Assuming a method exists)
        validPlanPatternDTO = convertToDTO(validPlanPattern);
    }

    @Test
    public void testSuccessfulUpdate() {
        when(planPatternMapper.toPlanPattern(validPlanPatternDTO)).thenReturn(validPlanPattern);
        when(planPatternRepository.save(validPlanPattern)).thenReturn(validPlanPattern);
        when(planPatternMapper.toPlanPatternDTO(validPlanPattern)).thenReturn(validPlanPatternDTO);

        ResponseEntity<?> response = planPatternService.updatePlanPattern(validPlanPatternDTO);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testInvalidCurrentTemperature() {
        PlanPatternDTO invalidDTO = new PlanPatternDTO();
        invalidDTO.setCurrentTemperature("invalid temperature"); // Set invalid temperature

        assertThrows(BadRequestException.class, () -> {
            planPatternService.updatePlanPattern(invalidDTO);
        });
    }

    @Test
    public void testInvalidTemperatureRegime() {
        PlanPatternDTO invalidDTO = new PlanPatternDTO();
        invalidDTO.setTemperatureRegime("Long string over 100 characters ..."); // Set invalid temperature regime

        assertThrows(BadRequestException.class, () -> {
            planPatternService.updatePlanPattern(invalidDTO);
        });
    }

    @Test
    public void testInvalidVentilationType() {
        PlanPatternDTO invalidDTO = new PlanPatternDTO();
        invalidDTO.setVentilationType("Long string over 150 characters ..."); // Set invalid ventilation type

        assertThrows(BadRequestException.class, () -> {
            planPatternService.updatePlanPattern(invalidDTO);
        });
    }

    @Test
    public void testInvalidLightLevel() {
        PlanPatternDTO invalidDTO = new PlanPatternDTO();
        invalidDTO.setLightLevel(-1); // Set invalid light level

        assertThrows(BadRequestException.class, () -> {
            planPatternService.updatePlanPattern(invalidDTO);
        });
    }

    @Test
    public void testInvalidHumidity() {
        PlanPatternDTO invalidDTO = new PlanPatternDTO();
        invalidDTO.setHumidity(new Humidity(-1, -1)); // Set invalid humidity

        assertThrows(BadRequestException.class, () -> {
            planPatternService.updatePlanPattern(invalidDTO);
        });
    }

    @Test
    public void testInvalidLightOffTime() {
        PlanPatternDTO invalidDTO = new PlanPatternDTO();
        invalidDTO.setLightOffTime(""); // Set invalid light off time

        assertThrows(BadRequestException.class, () -> {
            planPatternService.updatePlanPattern(invalidDTO);
        });
    }
}
