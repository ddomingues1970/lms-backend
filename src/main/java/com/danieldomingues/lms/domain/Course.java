package com.danieldomingues.lms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "courses", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter                 // para o Jackson popular via @RequestBody
@NoArgsConstructor      // exigido pelo JPA
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    // Atualiza os campos principais; se quiser permitir renomear, inclua 'name' aqui também
    public void update(String description, @NotNull LocalDate startDate, @NotNull LocalDate endDate) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Se quiser permitir rename com validação de unicidade no service:
    public void rename(@NotBlank String name) {
        this.name = name;
    }
}
