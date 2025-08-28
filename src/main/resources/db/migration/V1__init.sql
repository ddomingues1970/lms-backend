create table courses (
  id bigint generated always as identity primary key,
  name varchar(150) not null unique,
  description varchar(1000),
  start_date date not null,
  end_date date not null
);

create table students (
  id bigint generated always as identity primary key,
  first_name varchar(100) not null,
  last_name varchar(100) not null,
  birth_date date not null,
  email varchar(200) not null unique,
  phone varchar(30)
);
