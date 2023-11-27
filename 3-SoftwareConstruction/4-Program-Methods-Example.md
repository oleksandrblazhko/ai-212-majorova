package com.example.hygimeter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PlanPattern")
public class PlanPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "planPattern")
    private Microclimate microclimate; //мікроклімат

    @OneToMany(mappedBy = "planPattern")
    private List<MicroclimatePlan> microclimatePlans; //відповідний план мікроклімату

    @Column
    private String device; //присрої для забезпечення гарного мікроклімату

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "planParameters_id", referencedColumnName = "id")
    private PlanParameters planParameters; //параметри шаблону
}


package com.example.hygimeter.dto;

import com.example.hygimeter.dto.group.OnCreate;
import com.example.hygimeter.dto.group.OnUpdate;
import com.example.hygimeter.model.Microclimate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
@Schema(description = "Plan Pattern Data Transfer Object")
public class PlanPatternDTO {

    @Schema(description = "PlanPattern id")
    private Integer id;

    @NotNull(groups = OnUpdate.class, message = "Devices must be specified at plan")
    @Null(groups = OnCreate.class, message = "Devices must be null at fulling the form")
    @Schema(description = "Plan devices")
    private String device; 

    @NotNull(groups = OnCreate.class, message = "Microclimate cannot be null")
    @Valid
    @Schema(description = "Microclimate")
    private Microclimate microclimate;

    @NotNull(groups = OnCreate.class, message = "Plan parameters cannot be null")
    @Valid
    @Schema(description = "Plan Parameters")
    private PlanParametersDTO planParameters;
}

package com.example.hygimeter.mapper;

import com.example.hygimeter.dto.PlanPatternDTO;
import com.example.hygimeter.model.PlanPattern;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = PlanParametersMapper.class)
public interface PlanPatternMapper {
    PlanPatternMapper INSTANCE = Mappers.getMapper(PlanPatternMapper.class); //одиничний інтанс

    PlanPattern toPlanPattern(PlanPatternDTO planPatternDTO); // перевод від ДТО до звичайного класу
    PlanPatternDTO toPlanPatternDTO(PlanPattern planPattern); // перевод від звичайного класу до ДТО

    List<PlanPatternDTO> toPlanPatternDTOS(List<PlanPattern> planPatterns); // перевод від листа з об'єктами звичайного класу до листа з об'єктами ДТО
    List<PlanPattern> toPlanPatterns(List<PlanPatternDTO> planPatternDTOS); // перевод від листа з об'єктами ДТО до листа з об'єктами звичайного класу
}


package com.example.hygimeter.repository;

import com.example.hygimeter.model.PlanPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanPatternRepository extends JpaRepository<PlanPattern, Integer> { //наслідуємося від JpaRepository, щоб отримати базові створені sql-запити
    Optional<PlanPattern> findPlanPatternById(Integer id); //знаходження шаблону плана за id
}


package com.example.hygimeter.service;

import com.example.hygimeter.dto.PlanPatternDTO;

import java.util.List;

public interface PlanPatternService {
    PlanPatternDTO createPlanPattern(PlanPatternDTO planPatternDTO); //створення шаблону
    PlanPatternDTO updatePlanPattern(PlanPatternDTO planPatternDTO); //оновлення даних шаблону
    void deletePlanPattern(Integer id); //видалити шаблон
    PlanPatternDTO getPlanPatternById(Integer id); //знайти шаблон плана за айді
    List<PlanPatternDTO> getAllPlanPatterns(); //знайти усі можливі шаблони
}


package com.example.hygimeter.service;

import com.example.hygimeter.dto.PlanPatternDTO;
import com.example.hygimeter.mapper.PlanPatternMapper;
import com.example.hygimeter.model.PlanPattern;
import com.example.hygimeter.repository.PlanPatternRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

package com.example.hygimeter.service;

import com.example.hygimeter.dto.PlanPatternDTO;
import com.example.hygimeter.mapper.PlanPatternMapper;
import com.example.hygimeter.model.PlanPattern;
import com.example.hygimeter.repository.PlanPatternRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanPatternServiceImpl implements PlanPatternService{

    private final PlanPatternRepository planPatternRepository;
    private final PlanPatternMapper planPatternMapper;


    /**
     * Створює новий PlanPattern.
     * 
     * @param planPatternDTO DTO PlanPattern, що містить дані для збереження.
     * @return PlanPatternDTO DTO представлення новоствореного PlanPattern.
     */
    @Override
    public PlanPatternDTO createPlanPattern(PlanPatternDTO planPatternDTO) {
        PlanPattern planPattern = planPatternMapper.toPlanPattern(planPatternDTO);
        return planPatternMapper.toPlanPatternDTO(planPatternRepository.save(planPattern));
    }

    /**
     * Оновлює існуючий PlanPattern.
     * 
     * @param planPatternDTO DTO PlanPattern, що містить оновлені дані.
     * @return PlanPatternDTO DTO представлення оновленого PlanPattern.
     * @throws EntityNotFoundException якщо PlanPattern, який потрібно оновити, не знайдено.
     */
    @Override
    public PlanPatternDTO updatePlanPattern(PlanPatternDTO planPatternDTO) {
        PlanPattern planPattern = planPatternRepository.findById(planPatternDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Humidity not found"));

        PlanPattern newPlanPattern = planPatternMapper.toPlanPattern(planPatternDTO);
        planPattern.setMicroclimatePlans(newPlanPattern.getMicroclimatePlans());
        planPattern.setDevice(newPlanPattern.getDevice());
        planPattern.setPlanParameters(newPlanPattern.getPlanParameters());

        return planPatternMapper.toPlanPatternDTO(planPatternRepository.save(planPattern));
    }


    /**
     * Видаляє PlanPattern за його ID.
     * 
     * @param id ID PlanPattern, який потрібно видалити.
     * @throws EntityNotFoundException якщо PlanPattern, який потрібно видалити, не знайдено.
     */
    @Override
    public void deletePlanPattern(Integer id) {
        PlanPattern planPattern = planPatternRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan Pattern not found"));

        planPatternRepository.deleteById(planPattern.getId());
    }

    
    /**
     * Отримує PlanPattern за його ID.
     * 
     * @param id ID PlanPattern, який потрібно отримати.
     * @return PlanPatternDTO DTO представлення отриманого PlanPattern.
     * @throws EntityNotFoundException якщо PlanPattern не знайдено.
     */
    @Override
    public PlanPatternDTO getPlanPatternById(Integer id) {
        PlanPattern planPattern = planPatternRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan Pattern not found"));

        return planPatternMapper.toPlanPatternDTO(planPatternRepository.save(planPattern));
    }

    /**
     * Отримує всі PlanPatterns.
     * 
     * @return List<PlanPatternDTO> Список DTO представлень усіх PlanPatterns.
     */
    @Override
    public List<PlanPatternDTO> getAllPlanPatterns() {
        List<PlanPattern> planPatterns = planPatternRepository.findAll();
        return planPatternMapper.toPlanPatternDTOS(planPatterns);
    }
}

