/**
 * This package contains classes and interfaces related to the service layer
 * of the hygimeter application. It includes entities representing plan patterns
 * for managing microclimates, data transfer objects (DTOs), repositories for
 * database access, and service implementations for business logic.
 *
 * @since 1.0
 */
package com.example.hygimeter.service;

/**
 * Represents a plan pattern for managing microclimates.
 * It includes details such as associated microclimate, device information, and plan parameters.
 */
@Entity
@Table(name = "PlanPattern")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanPattern {

    /**
     * Unique identifier for the PlanPattern.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The optimal microclimate associated with this plan pattern.
     */
    @OneToOne
    @JoinColumn(name = "optimalmicroclimate_id", referencedColumnName = "id")
    private Microclimate microclimate;

    /**
     * A list of microclimate plans linked to this plan pattern.
     */
    @OneToMany(mappedBy = "planPattern")
    private List<MicroclimatePlan> microclimatePlans;

    /**
     * Information about the device associated with this plan pattern.
     */
    @Column
    private String device;

    /**
     * Parameters for the plan associated with this plan pattern.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "planParameters_id", referencedColumnName = "id")
    private PlanParameters planParameters;
}

/**
 * Data Transfer Object for PlanPattern.
 * It's used to transfer data between processes, ensuring that only the necessary data is communicated.
 */
@Data
@Schema(description = "Plan Pattern Data Transfer Object")
public class PlanPatternDTO {

    /**
     * Identifier for the PlanPattern.
     */
    @Schema(description = "PlanPattern id")
    private Integer id;

    /**
     * Device information for the PlanPattern. It must be null when creating a new PlanPattern.
     */
    @Null(groups = OnCreate.class, message = "Devices must be null at fulling the form")
    @Schema(description = "Plan devices")
    private String device;

    /**
     * Microclimate details for the PlanPattern. It should be null when creating a new PlanPattern for the first time.
     */
    @Null(groups = OnCreate.class, message = "Microclimate should be null while 1st time creating")
    @Valid
    @Schema(description = "Microclimate")
    private MicroclimateDTO microclimateDTO;

    /**
     * Plan parameters for the PlanPattern. It cannot be null when creating a new PlanPattern.
     */
    @NotNull(groups = OnCreate.class, message = "Plan parameters cannot be null")
    @Valid
    @Schema(description = "Plan Parameters")
    private PlanParametersDTO planParametersDTO;
}

/**
 * Repository interface for PlanPattern entities.
 * It provides methods to perform operations such as finding a plan pattern by its ID.
 */
@Repository
public interface PlanPatternRepository extends JpaRepository<PlanPattern, Integer> {
    /**
     * Find a PlanPattern by its identifier.
     *
     * @param id The identifier of the PlanPattern.
     * @return An Optional containing the PlanPattern if found.
     */
    Optional<PlanPattern> findPlanPatternById(Integer id);
}

/**
 * Service interface defining operations for managing PlanPatterns.
 */
public interface PlanPatternService {
    PlanPatternDTO createPlanPattern(PlanPatternDTO planPatternDTO);
    PlanPatternDTO updatePlanPattern(Integer id, PlanPatternDTO planPatternDTO);
    void deletePlanPattern(Integer id);
    PlanPatternDTO getPlanPatternById(Integer id);
    List<PlanPatternDTO> getAllPlanPatterns();
}

/**
 * Implementation of PlanPatternService.
 * Provides functionality for managing PlanPatterns, including creation, update, deletion, and retrieval.
 */
@Service
@RequiredArgsConstructor
public class PlanPatternServiceImpl implements PlanPatternService{

    private final PlanPatternRepository planPatternRepository;
    private final PlanPatternMapper planPatternMapper;
    
    /**
     * Creates a new PlanPattern entity from a DTO and saves it to the repository.
     *
     * @param planPatternDTO The PlanPatternDTO containing the data to be saved.
     * @return The saved PlanPatternDTO with generated ID and persisted data.
     */
    @Override
    public PlanPatternDTO createPlanPattern(PlanPatternDTO planPatternDTO) {
        PlanPattern planPattern = planPatternMapper.toPlanPattern(planPatternDTO);
        return planPatternMapper.toPlanPatternDTO(planPatternRepository.save(planPattern));
    }

    /**
     * Updates an existing PlanPattern entity based on a provided DTO.
     *
     * @param id The ID of the PlanPattern to be updated.
     * @param planPatternDTO The PlanPatternDTO containing updated data.
     * @return The updated PlanPatternDTO.
     * @throws EntityNotFoundException If no PlanPattern is found with the provided ID.
     */
    @Override
    public PlanPatternDTO updatePlanPattern(Integer id, PlanPatternDTO planPatternDTO) {
        PlanPattern planPattern = planPatternRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StatusCodes.ENTITY_NOT_FOUND.name(), "Plan pattern not found"));

        planPatternValidation(planPatternDTO, OnUpdate.class);
        PlanPattern newPlanPattern = planPatternMapper.toPlanPattern(planPatternDTO);
        planPattern.setMicroclimatePlans(newPlanPattern.getMicroclimatePlans());
        planPattern.setDevice(newPlanPattern.getDevice());
        planPattern.setPlanParameters(newPlanPattern.getPlanParameters());

        return planPatternMapper.toPlanPatternDTO(planPatternRepository.save(planPattern));
    }

    /**
     * Deletes a PlanPattern entity from the repository based on the given ID.
     *
     * @param id The ID of the PlanPattern to be deleted.
     * @throws EntityNotFoundException If no PlanPattern is found with the provided ID.
     */
    @Override
    public void deletePlanPattern(Integer id) {
        PlanPattern planPattern = planPatternRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StatusCodes.ENTITY_NOT_FOUND.name(), "Plan Pattern not found"));

        planPatternRepository.deleteById(planPattern.getId());
    }

    /**
     * Retrieves a PlanPatternDTO by its ID.
     *
     * @param id The ID of the PlanPattern to be retrieved.
     * @return The retrieved PlanPatternDTO.
     * @throws EntityNotFoundException If no PlanPattern is found with the provided ID.
     */
    @Override
    public PlanPatternDTO getPlanPatternById(Integer id) {
        PlanPattern planPattern = planPatternRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StatusCodes.ENTITY_NOT_FOUND.name(), "Plan Pattern not found"));

        return planPatternMapper.toPlanPatternDTO(planPattern);
    }

    /**
     * Retrieves all PlanPattern entities from the repository.
     *
     * @return A list of PlanPatternDTOs.
     */
    @Override
    public List<PlanPatternDTO> getAllPlanPatterns() {
        List<PlanPattern> planPatterns = planPatternRepository.findAll();
        return planPatternMapper.toPlanPatternDTOS(planPatterns);
    }

    /**
     * Validates the given PlanPatternDTO object according to the specified validation group.
     * This method checks if the required fields in PlanPatternDTO and its nested objects (MicroclimateDTO, HumidityDTO, and PlanParametersDTO)
     * are valid based on whether it's a create or update operation.
     *
     * @param planPatternDTO The PlanPatternDTO object that contains the plan pattern data.
     * @param validationGroup The class of the validation group, typically OnCreate.class or OnUpdate.class.
     * @throws InvalidDataException If the validation fails, this exception is thrown with appropriate status codes and messages.
     * 
     * For a create operation (OnCreate.class):
     * - Throws an exception if 'device' is not null.
     * - Throws an exception if 'microclimate' is not null.
     * - Throws an exception if 'planParameters' is null.
     * - Throws an exception if 'relativeHumidity' or 'absoluteHumidity' in humidity is not null.
     * 
     * For an update operation (OnUpdate.class):
     * - Throws an exception if the length of 'temperature' in microclimate exceeds 20 characters.
     * - Throws an exception if the length of 'ventilation' in microclimate exceeds 100 characters.
     * - Throws an exception if 'lightLevel' in microclimate is less than or equal to 0.
     * - Throws an exception if 'relativeHumidity' or 'absoluteHumidity' in humidity is null or less than or equal to 0.
     * - Throws an exception if 'temperatureSked' in planParameters is null, empty, or its length exceeds 100 characters.
     * - Throws an exception if 'lightsOffTime' in planParameters is null or the hour is not within 0 to 23.
     */
    private void planPatternValidation(PlanPatternDTO planPatternDTO, Class<?> validationGroup) {
        MicroclimateDTO microclimate = planPatternDTO.getMicroclimateDTO();
        HumidityDTO humidity = microclimate.getHumidity();
        PlanParametersDTO planParameters = planPatternDTO.getPlanParametersDTO();

        // Assuming that an ID of null means this is a create operation
        boolean isCreateOperation = (planPatternDTO.getId() == null);

        if (isCreateOperation && validationGroup.equals(OnCreate.class)) {
            // Perform validations specific to OnCreate group
            if (planPatternDTO.getDevice() != null) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(),
                                               "Device must be null at fulling the form");
            }
            if (microclimate != null) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Microclimate should be null while 1st time creating");
            }
            if (planParameters == null) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Plan parameters cannot be null");
            }
            if (humidity.getRelativeHumidity() != null) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Relative humidity must be null on creating plan parameters");
            }
            if (humidity.getAbsoluteHumidity() != null) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Absolute humidity must be null on creating plan parameters");
            }
        } else if (!isCreateOperation && validationGroup.equals(OnUpdate.class)) {
            // Perform validations specific to OnUpdate group
            if (microclimate.getTemperature().length() > 20) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Max size of temperature is 20 characters");
            }
            if (microclimate.getVentilation().length() > 100) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Max size of ventilation is 100 characters");
            }
            if (microclimate.getLightLevel() <= 0) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Light level must be greater than 0");
            }
            if (humidity.getRelativeHumidity() == null || humidity.getRelativeHumidity() <= 0) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Relative humidity must be not null and greater than 0 on updating parameters");
            }
            if (humidity.getAbsoluteHumidity() == null || humidity.getAbsoluteHumidity() <= 0) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Absolute humidity must be not null and greater than 0 on updating parameters");
            }
            if (planParameters.getTemperatureSked() == null || planParameters.getTemperatureSked().isEmpty()) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Temperature schedule must be not null and not empty on updating parameters");
            }
            if (planParameters.getTemperatureSked().length() > 100) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Max size of temperature schedule is 100 characters");
            }
            if (planParameters.getLightsOffTime() == null || planParameters.getLightsOffTime().getHour() > 23 || planParameters.getLightsOffTime().getHour() < 0 ) {
                throw new InvalidDataException(StatusCodes.INVALID_DATA.name(), 
                                               "Time when lights go off must be not null on updating parameters");
            }
        }
    }
}
