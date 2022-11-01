CREATE TABLE users (
id SERIAL PRIMARY KEY NOT NULL,
username varchar(64) NOT NULL,
password varchar(64) NOT NULL,
name varchar(64) NOT NULL,
surname varchar(64) NOT NULL,
email varchar(64) NOT NULL
);

CREATE TABLE courses (
id SERIAL PRIMARY KEY NOT NULL,
theme varchar(64) NOT NULL,
subject varchar(64) NOT NULL,
status varchar(64) NOT NULL,
online varchar(64) NOT NULL,
address varchar(64),
teacher_id bigint REFERENCES users(id),
cost int,
start_date timestamp,
finish_date timestamp,
description varchar(1024) NOT NULL,
average_rating float
);

CREATE TABLE lessons (
id SERIAL PRIMARY KEY NOT NULL,
theme varchar(64) NOT NULL,
subject varchar(64) NOT NULL,
teacher_id bigint REFERENCES users(id),
course_id bigint REFERENCES courses(id),
status varchar(64) NOT NULL,
address varchar(64),
max_students_number int,
description varchar(1024) NOT NULL,
cost int,
duration int,
lesson_date timestamp NOT NULL,
fixed_salary int,
unfixed_salary int,
average_rating float
);

CREATE TABLE subscriptions (
id SERIAL PRIMARY KEY NOT NULL,
student_id bigint REFERENCES users(id),
lesson_id bigint REFERENCES lessons(id)
);

CREATE TABLE reviews (
id SERIAL PRIMARY KEY NOT NULL,
rating int NOT NULL,
text varchar(1024),
user_id bigint REFERENCES users(id),
lesson_id bigint REFERENCES lessons(id),
course_id bigint REFERENCES courses(id)
);
