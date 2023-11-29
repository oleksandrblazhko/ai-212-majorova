package com.example.hygimeter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MicroclimatePlan")
public class MicroclimatePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "planPattern_id")
    private PlanPattern planPattern;

    @OneToOne
    @JoinColumn(name = "initiallyMicroclimate_id")
    private Microclimate initialMicroclimate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private TopicInfo topic;
}
