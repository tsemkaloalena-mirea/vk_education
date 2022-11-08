CREATE TABLE users (
id SERIAL NOT NULL,
username varchar(64) NOT NULL,
password varchar(64) NOT NULL,
name varchar(64) NOT NULL,
surname varchar(64) NOT NULL,
email varchar(64) NOT NULL,
CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE courses (
id SERIAL NOT NULL,
theme varchar(64) NOT NULL,
subject varchar(64) NOT NULL,
status varchar(64) NOT NULL,
online varchar(64) NOT NULL,
address varchar(64),
teacher_id bigint REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
cost int,
start_date timestamp,
finish_date timestamp,
description varchar(1024) NOT NULL,
average_rating float,
CONSTRAINT courses_pk PRIMARY KEY (id)
);

CREATE TABLE lessons (
id SERIAL NOT NULL,
theme varchar(64) NOT NULL,
subject varchar(64) NOT NULL,
teacher_id bigint REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
course_id bigint REFERENCES courses(id) ON UPDATE CASCADE ON DELETE CASCADE,
status varchar(64) NOT NULL,
address varchar(64),
max_students_number int,
description varchar(1024) NOT NULL,
cost int,
duration int,
lesson_date timestamp NOT NULL,
fixed_salary int,
unfixed_salary int,
average_rating float,
CONSTRAINT lessons_pk PRIMARY KEY (id)
);

CREATE TABLE subscriptions (
id SERIAL NOT NULL,
student_id bigint REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
lesson_id bigint REFERENCES lessons(id) ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT subscriptions_pk PRIMARY KEY (id)
);

CREATE TABLE reviews (
id SERIAL NOT NULL,
rating int NOT NULL,
text varchar(1024),
user_id bigint REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
lesson_id bigint REFERENCES lessons(id) ON UPDATE CASCADE ON DELETE CASCADE,
course_id bigint REFERENCES courses(id) ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT reviews_pk PRIMARY KEY (id)
);
