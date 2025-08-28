# LMS - Learning Management System (Backend)

Este projeto implementa o backend de um sistema simples de gerenciamento de aprendizagem (**LMS**) como parte de avaliação técnica.

---

## 🚀 Tecnologias utilizadas
- **Java 21**
- **Spring Boot 3.4.4**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database** (memória para desenvolvimento)
- **Maven**

---

## ⚙️ Funcionalidades implementadas

### 1. Registro de Estudantes
- Cadastro de estudantes com:
    - Idade mínima: **16 anos**
    - E-mail único
    - Campos obrigatórios: primeiro nome, último nome, data de nascimento, e-mail, telefone

### 2. Gerenciamento de Cursos (Admin)
- CRUD de cursos
- Nome único
- Curso deve terminar em até **6 meses** após a data de início
- **Apenas administradores** podem acessar os endpoints

### 3. Matrículas
- Estudantes podem se matricular em até **3 cursos simultaneamente**
- Validação de duplicidade de matrícula

### 4. Registro de Tarefas (TaskLog)
- Estudantes registram tarefas em cursos informando:
    - Data/hora
    - Categoria (tabela fixa: `PESQUISA`, `PRATICA`, `ASSISTIR_VIDEOAULA`)
    - Descrição
    - Tempo gasto em blocos de **30 minutos**
- Permite múltiplos registros no mesmo dia
- Estudantes podem editar e remover logs

---

## 🔐 Segurança (Spring Security)

Dois usuários em memória foram configurados para testes:

- **Admin**
    - Usuário: `admin`
    - Senha: `admin123`
    - Permissão: `ROLE_ADMIN`
    - Pode gerenciar cursos

- **Student**
    - Usuário: `student`
    - Senha: `student123`
    - Permissão: `ROLE_STUDENT`
    - Pode se matricular e registrar tarefas

---

## ▶️ Como executar

### Pré-requisitos
- Java 21
- Maven 3.9+

### Comandos
```bash
# Clonar o repositório
git clone https://github.com/ddomingues1970/lms-backend.git
cd lms-backend

# Executar o projeto
./mvnw spring-boot:run

O backend subirá em:
👉 http://localhost:8080

Banco H2 Console

👉 http://localhost:8080/h2-console

Configuração:

JDBC URL: jdbc:h2:mem:lms

User: sa

Password: (em branco)

📡 Endpoints principais
Estudantes

POST /api/students → cadastrar estudante

GET /api/students → listar estudantes

Cursos (somente Admin)

POST /api/courses → criar curso

PUT /api/courses/{id} → atualizar curso

DELETE /api/courses/{id} → excluir curso

GET /api/courses → listar cursos

Matrículas (somente Student)

POST /api/enrollments → matricular estudante em curso

GET /api/enrollments/student/{id} → listar matrículas de um estudante

Tarefas (somente Student)

POST /api/task-logs → registrar tarefa

PUT /api/task-logs/{id} → editar tarefa

DELETE /api/task-logs/{id} → remover tarefa

GET /api/task-logs/student/{id} → listar tarefas por estudante

📦 Postman Collection

Uma coleção pronta está incluída no repositório:
👉 lms-postman-collection-tasklog.json

Importe no Postman para testar rapidamente os endpoints.

📝 Observações

O projeto está configurado para rodar com H2 em memória (dados não persistem após restart).

Scripts SQL (schema.sql / data.sql) criam e populam a tabela de categorias fixas.

Pode ser facilmente adaptado para PostgreSQL ou outro banco relacional.

👨‍💻 Autor

Daniel Domingues
Senior Systems Engineer | Software Architect
LinkedIn