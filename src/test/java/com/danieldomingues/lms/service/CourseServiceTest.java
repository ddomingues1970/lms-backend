package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.repository.CourseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository repo;

    @InjectMocks
    private CourseService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Cria curso válido (nome único e duração ≤ 6 meses)")
    void create_ok() {
        Course c = new Course();
        c.setName("Java Básico");
        c.setDescription("Intro");
        c.setStartDate(LocalDate.now());
        c.setEndDate(LocalDate.now().plusMonths(3)); // <= 6

        when(repo.existsByName("Java Básico")).thenReturn(false);
        when(repo.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

        Course saved = service.create(c);

        assertNotNull(saved);
        assertEquals("Java Básico", saved.getName());
        verify(repo).existsByName("Java Básico");
        verify(repo).save(any(Course.class));
    }

    @Test
    @DisplayName("Rejeita criação quando nome do curso já existe")
    void create_duplicateName_shouldFail() {
        Course c = new Course();
        c.setName("Java Básico");
        c.setDescription("Intro");
        c.setStartDate(LocalDate.now());
        c.setEndDate(LocalDate.now().plusMonths(3));

        when(repo.existsByName("Java Básico")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(c));
        assertTrue(ex.getMessage().toLowerCase().contains("unique") || ex.getMessage().toLowerCase().contains("nome"));
        verify(repo, never()).save(any(Course.class));
    }

    @Test
    @DisplayName("Rejeita criação quando duração do curso excede 6 meses")
    void create_over6Months_shouldFail() {
        Course c = new Course();
        c.setName("Projeto Longo");
        c.setDescription("> 6 meses");
        c.setStartDate(LocalDate.now());
        c.setEndDate(LocalDate.now().plusMonths(7)); // > 6

        when(repo.existsByName("Projeto Longo")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(c));
        assertTrue(ex.getMessage().toLowerCase().contains("6"));
        verify(repo, never()).save(any(Course.class));
    }

    @Test
    @DisplayName("Atualiza curso válido (mantendo regra dos 6 meses)")
    void update_ok() {
        Long id = 10L;

        // existente no banco
        Course existing = new Course();
        existing.setName("Java Básico");
        existing.setDescription("Old");
        existing.setStartDate(LocalDate.now());
        existing.setEndDate(LocalDate.now().plusMonths(2));

        // dados de entrada (controller monta um Course e passa para o service.update)
        Course data = new Course();
        data.setDescription("Atualizado");
        data.setStartDate(LocalDate.now().plusDays(1));
        data.setEndDate(LocalDate.now().plusMonths(5)); // <= 6

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

        Course updated = service.update(id, data);

        assertNotNull(updated);
        assertEquals("Atualizado", updated.getDescription());
        verify(repo).findById(id);
        verify(repo).save(any(Course.class));
    }

    @Test
    @DisplayName("Update deve falhar se exceder 6 meses")
    void update_over6Months_shouldFail() {
        Long id = 11L;

        Course existing = new Course();
        existing.setName("Kotlin");
        existing.setDescription("Old");
        existing.setStartDate(LocalDate.now());
        existing.setEndDate(LocalDate.now().plusMonths(2));

        Course data = new Course();
        data.setDescription("Novo");
        data.setStartDate(LocalDate.now());
        data.setEndDate(LocalDate.now().plusMonths(7)); // > 6

        when(repo.findById(id)).thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.update(id, data));
        assertTrue(ex.getMessage().toLowerCase().contains("6"));
        verify(repo, never()).save(any(Course.class));
    }

    @Test
    @DisplayName("Delete executa quando curso existe (usa existsById + deleteById)")
    void delete_callsRepo_whenExists() {
        Long id = 15L;

        when(repo.existsById(id)).thenReturn(true);
        doNothing().when(repo).deleteById(id);

        assertDoesNotThrow(() -> service.delete(id));

        verify(repo).existsById(id);
        verify(repo).deleteById(id);
    }


    @Test
    @DisplayName("Delete deve falhar quando curso não existe (existsById=false)")
    void delete_shouldFail_whenNotExists() {
        Long id = 99L;

        when(repo.existsById(id)).thenReturn(false);

        NoSuchElementException ex =
                assertThrows(NoSuchElementException.class, () -> service.delete(id));
        assertTrue(ex.getMessage().toLowerCase().contains("course"));
        verify(repo, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Listar delega para o repositório")
    void findAll_callsRepo() {
        service.findAll();
        verify(repo).findAll();
    }
}
