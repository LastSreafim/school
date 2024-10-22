-- liquibase formatted sql
-- changeset nazar: 2
CREATE INDEX student_name_idx ON student (name);
CREATE INDEX faculty_nameAndColor_idx ON faculty(name, color);