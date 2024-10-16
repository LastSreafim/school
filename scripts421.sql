//ограничени по возрасту
ALTER TABLE student
    ADD CONSTRAINT age_constraint CHECK ( age >= 16 )

//уникальность имени и проверка на "Null"
ALTER TABLE student
    ADD CONSTRAINT unique_student_name UNIQUE (name) ,
ALTER COLUMN name SET NOT NULL;

//уникальная пара факультета имя/цвет
ALTER TABLE faculty
    ADD CONSTRAINT unique_name_color UNIQUE (name, color);

//Для установки дефолтного значения возраста, т.е когда возраст не указывается явно будет использовано значение 20
ALTER TABLE students
    ALTER COLUMN age SET DEFAULT 20;