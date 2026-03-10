package ru.yandex.practicum.model;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "conditions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ConditionType type;

    @Column(name = "operation")
    @Enumerated(EnumType.STRING)
    private ConditionOperation operation;

    @Column(name = "value")
    private Integer value;
}
