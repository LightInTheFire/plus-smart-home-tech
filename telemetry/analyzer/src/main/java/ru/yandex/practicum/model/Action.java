package ru.yandex.practicum.model;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "actions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ActionType type;

    @Column(name = "value")
    private Integer value;
}
