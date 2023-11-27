package com.example.hygimeter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Microclimate")
public class Microclimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String temperature;

    @Column
    private String ventilation;

    @Column(name = "lightLevel")
    private Float lightLevel;

    @OneToOne(mappedBy = "initialMicroclimate")
    private MicroclimatePlan plan;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "humidity_id", referencedColumnName = "id")
    private Humidity humidity;
}
