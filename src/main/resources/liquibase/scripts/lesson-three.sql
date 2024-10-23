-- liquibase formatted sql

--changeset nazar:1
CREATE INDEX IF NOT EXISTS student_name_idx ON student (name);

--changeset nazar:2
CREATE INDEX IF NOT EXISTS faculty_nameAndColor_idx ON faculty(name, color);