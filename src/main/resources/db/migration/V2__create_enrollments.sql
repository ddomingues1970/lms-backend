-- Tabela de matrículas
create table if not exists enrollments (
  id            bigint generated always as identity primary key,
  student_id    bigint       not null,
  course_id     bigint       not null,
  enrollment_date date       not null default current_date,
  constraint fk_enroll_student foreign key (student_id) references students(id) on delete cascade,
  constraint fk_enroll_course  foreign key (course_id)  references courses(id)  on delete cascade,
  constraint uk_enroll_student_course unique (student_id, course_id)
);

-- Índices úteis
create index if not exists ix_enroll_student on enrollments(student_id);
create index if not exists ix_enroll_course  on enrollments(course_id);
