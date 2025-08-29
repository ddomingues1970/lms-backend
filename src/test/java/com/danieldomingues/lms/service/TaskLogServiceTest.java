package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.domain.Student;
import com.danieldomingues.lms.domain.TaskCategory;
import com.danieldomingues.lms.domain.TaskLog;
import com.danieldomingues.lms.repository.CourseRepository;
import com.danieldomingues.lms.repository.StudentRepository;
import com.danieldomingues.lms.repository.TaskCategoryRepository;
import com.danieldomingues.lms.repository.TaskLogRepository;
import com.danieldomingues.lms.web.dto.TaskLogCreateDto;
import com.danieldomingues.lms.web.dto.TaskLogUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskLogServiceTest {

    @Mock
    private TaskLogRepository repo;
    @Mock
    private StudentRepository studentRepo;
    @Mock
    private CourseRepository courseRepo;
    @Mock
    private TaskCategoryRepository categoryRepo;

    @InjectMocks
    private TaskLogService service;

    private Student student;
    private Course course;
    private TaskCategory category;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Entidades concretas (no-args + setters) seguindo o padrão do projeto
        student = new Student();
        student.setFirstName("Ana");
        student.setLastName("Silva");
        student.setBirthDate(LocalDate.now().minusYears(20));
        student.setEmail("ana@example.com");
        student.setPhone("11999999999");

        course = new Course();
        course.setName("Java Básico");
        course.setDescription("Intro");
        course.setStartDate(LocalDate.now());
        course.setEndDate(LocalDate.now().plusMonths(3));

        category = new TaskCategory("PESQUISA");
    }

    // ---------- CREATE ----------

    @Test
    @DisplayName("create: deve criar TaskLog com minutos múltiplos de 30 e entidades existentes")
    void create_ok() {
        Long sid = 1L, cid = 2L, catId = 3L;
        TaskLogCreateDto dto = new TaskLogCreateDto(
                sid, cid, catId,
                LocalDateTime.now(),
                "Assistir aula",
                60
        );

        when(studentRepo.findById(sid)).thenReturn(Optional.of(student));
        when(courseRepo.findById(cid)).thenReturn(Optional.of(course));
        when(categoryRepo.findById(catId)).thenReturn(Optional.of(category));
        when(repo.save(any(TaskLog.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskLog saved = service.create(dto);

        assertNotNull(saved);
        assertEquals(60, saved.getMinutesSpent());
        assertEquals("Assistir aula", saved.getDescription());
        assertEquals(student, saved.getStudent());
        assertEquals(course, saved.getCourse());
        assertEquals(category, saved.getCategory());
        verify(repo).save(any(TaskLog.class));
    }

    @Test
    @DisplayName("create: deve falhar se minutos não forem múltiplos de 30")
    void create_invalid_minutes_shouldFail() {
        TaskLogCreateDto dto = new TaskLogCreateDto(
                1L, 2L, 3L,
                LocalDateTime.now(),
                "desc",
                45 // inválido
        );

        // Stub necessários para passar das buscas e cair na validação dos 30 min
        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepo.findById(2L)).thenReturn(Optional.of(course));
        when(categoryRepo.findById(3L)).thenReturn(Optional.of(category));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("30"));

        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("create: deve falhar se Student não existir")
    void create_student_not_found() {
        TaskLogCreateDto dto = new TaskLogCreateDto(
                1L, 2L, 3L,
                LocalDateTime.now(),
                "desc",
                30
        );

        when(studentRepo.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("student"));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("create: deve falhar se Course não existir")
    void create_course_not_found() {
        TaskLogCreateDto dto = new TaskLogCreateDto(
                1L, 2L, 3L,
                LocalDateTime.now(),
                "desc",
                30
        );

        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepo.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("course"));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("create: deve falhar se Category não existir")
    void create_category_not_found() {
        TaskLogCreateDto dto = new TaskLogCreateDto(
                1L, 2L, 3L,
                LocalDateTime.now(),
                "desc",
                30
        );

        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepo.findById(2L)).thenReturn(Optional.of(course));
        when(categoryRepo.findById(3L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("category"));
        verify(repo, never()).save(any());
    }

    // ---------- UPDATE ----------

    @Test
    @DisplayName("update: deve atualizar TaskLog com minutos válidos e categoria existente")
    void update_ok() {
        Long id = 10L, catId = 5L;

        TaskLog existing = new TaskLog(student, course, category, LocalDateTime.now().minusHours(1), "antigo", 30);

        TaskCategory newCategory = new TaskCategory("PRATICA");
        TaskLogUpdateDto dto = new TaskLogUpdateDto(
                catId,
                LocalDateTime.now(),
                "novo desc",
                60
        );

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepo.findById(catId)).thenReturn(Optional.of(newCategory));

        TaskLog updated = service.update(id, dto);

        assertNotNull(updated);
        assertEquals("novo desc", updated.getDescription());
        assertEquals(60, updated.getMinutesSpent());
        assertEquals(newCategory, updated.getCategory());
        // como update() não chama repo.save (retorna a entidade gerenciada), não é necessário verify(repo).save(...)
    }

    @Test
    @DisplayName("update: deve falhar se TaskLog não existir")
    void update_not_found_shouldFail() {
        Long id = 10L;
        TaskLogUpdateDto dto = new TaskLogUpdateDto(1L, LocalDateTime.now(), "x", 30);

        when(repo.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.update(id, dto));
        assertTrue(ex.getMessage().toLowerCase().contains("tasklog"));
    }

    @Test
    @DisplayName("update: deve falhar se Category não existir")
    void update_category_not_found_shouldFail() {
        Long id = 10L;
        TaskLog existing = new TaskLog(student, course, category, LocalDateTime.now(), "desc", 30);
        TaskLogUpdateDto dto = new TaskLogUpdateDto(99L, LocalDateTime.now(), "x", 30);

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepo.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.update(id, dto));
        assertTrue(ex.getMessage().toLowerCase().contains("category"));
    }

    @Test
    @DisplayName("update: deve falhar se minutos não forem múltiplos de 30")
    void update_invalid_minutes_shouldFail() {
        Long id = 10L;
        TaskLog existing = new TaskLog(student, course, category, LocalDateTime.now(), "desc", 30);
        TaskLogUpdateDto dto = new TaskLogUpdateDto(1L, LocalDateTime.now(), "x", 45);

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.update(id, dto));
        assertTrue(ex.getMessage().toLowerCase().contains("30"));
    }

    // ---------- DELETE ----------

    @Test
    @DisplayName("delete: executa quando TaskLog existe (existsById=true)")
    void delete_ok_whenExists() {
        Long id = 7L;

        when(repo.existsById(id)).thenReturn(true);
        doNothing().when(repo).deleteById(id);

        assertDoesNotThrow(() -> service.delete(id));

        verify(repo).existsById(id);
        verify(repo).deleteById(id);
    }

    @Test
    @DisplayName("delete: deve falhar quando TaskLog não existe")
    void delete_shouldFail_whenNotExists() {
        Long id = 8L;

        when(repo.existsById(id)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.delete(id));
        assertTrue(ex.getMessage().toLowerCase().contains("tasklog"));
        verify(repo, never()).deleteById(anyLong());
    }

    // ---------- LIST ----------

    @Test
    @DisplayName("listByStudent: delega para o repositório e retorna lista")
    void listByStudent_ok() {
        Long studentId = 1L;
        when(repo.findByStudentId(studentId)).thenReturn(List.of(
                new TaskLog(student, course, category, LocalDateTime.now(), "d1", 30),
                new TaskLog(student, course, category, LocalDateTime.now().plusHours(1), "d2", 60)
        ));

        var result = service.listByStudent(studentId);

        assertEquals(2, result.size());
        verify(repo).findByStudentId(studentId);
    }
}
