package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.Student;
import com.danieldomingues.lms.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository repo;

    @InjectMocks
    private StudentService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve registrar estudante com dados válidos")
    void register_ok() {
        Student s = new Student();
        s.setFirstName("Ana");
        s.setLastName("Silva");
        s.setBirthDate(LocalDate.now().minusYears(20));
        s.setEmail("ana@example.com");          // <-- Faltava isso
        s.setPhone("11999999999");

        when(repo.existsByEmail("ana@example.com")).thenReturn(false);
        when(repo.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student saved = service.register(s);

        assertNotNull(saved);
        assertEquals("Ana", saved.getFirstName());
        verify(repo).existsByEmail("ana@example.com");
        verify(repo).save(any(Student.class));
    }

    @Test
    @DisplayName("Deve rejeitar registro para menor de 16 anos")
    void register_under16_shouldFail() {

        Student s = new Student();
        s.setFirstName("Joao");
        s.setLastName("Souza");
        s.setBirthDate(LocalDate.now().minusYears(15));
        s.setPhone("joao@example.com");
        s.setPhone("11888888888");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.register(s));
        assertTrue(ex.getMessage().toLowerCase().contains("16"));
        verify(repo, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Deve rejeitar e-mail já registrado")
    void register_duplicateEmail_shouldFail() {
        Student s = new Student();
        s.setFirstName("Maria");
        s.setLastName("Oliveira");
        s.setBirthDate(LocalDate.now().minusYears(25));
        s.setEmail("maria@example.com");      // <-- faltava isso
        s.setPhone("11777777777");

        when(repo.existsByEmail("maria@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.register(s));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
        verify(repo, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Deve delegar listagem para o repositório")
    void findAll_callsRepository() {
        service.findAll();
        verify(repo).findAll();
    }
}
