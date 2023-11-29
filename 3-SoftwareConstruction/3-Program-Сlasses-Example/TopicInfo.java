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
@Table(name = "TopicsInfo")
public class TopicInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String description;

    @Column
    private String type;

    @Lob
    @Column(name = "info", columnDefinition = "BYTEA")
    private byte[] info;

    @ManyToMany
    @JoinTable(
            name = "ThemeTopicsInfo",
            joinColumns = @JoinColumn(name = "topics_info_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private List<Theme> themes;

    @OneToMany(mappedBy = "topic")
    private List<MicroclimatePlan> microclimatePlans;
}
