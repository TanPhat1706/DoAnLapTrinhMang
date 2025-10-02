-- DEV seed tailored for H2 with Hibernate's generated schema

-- Roles
INSERT INTO "role"(role_name)
SELECT 'Admin' WHERE NOT EXISTS (SELECT 1 FROM "role" WHERE role_name = 'Admin');
INSERT INTO "role"(role_name)
SELECT 'Student' WHERE NOT EXISTS (SELECT 1 FROM "role" WHERE role_name = 'Student');
INSERT INTO "role"(role_name)
SELECT 'Lecturer' WHERE NOT EXISTS (SELECT 1 FROM "role" WHERE role_name = 'Lecturer');

-- Faculty
INSERT INTO faculty(faculty_name)
SELECT 'Computer Science' WHERE NOT EXISTS (SELECT 1 FROM faculty WHERE faculty_name = 'Computer Science');

-- Class
INSERT INTO "class"(class_name, year_start, facultyid)
SELECT 'CTK45A', 2025, (SELECT MIN(facultyid) FROM faculty)
WHERE NOT EXISTS (SELECT 1 FROM "class" WHERE class_name = 'CTK45A');

-- Users
INSERT INTO "user"(student_code, full_name, email, gender, birth_year, phone, classid, roleid)
SELECT NULL, 'System Admin', 'admin@example.com', 'M', 1990, '0123456789',
             (SELECT MIN(classid) FROM "class"),
             (SELECT roleid FROM "role" WHERE role_name = 'Admin')
WHERE NOT EXISTS (SELECT 1 FROM "user" WHERE email = 'admin@example.com');

INSERT INTO "user"(student_code, full_name, email, gender, birth_year, phone, classid, roleid)
SELECT 'SV0001', 'Student One', 'student1@example.com', 'F', 2004, '0987654321',
             (SELECT MIN(classid) FROM "class"),
             (SELECT roleid FROM "role" WHERE role_name = 'Student')
WHERE NOT EXISTS (SELECT 1 FROM "user" WHERE email = 'student1@example.com');

-- Vote
INSERT INTO vote(title, created_date, start_time, end_time, created_by)
SELECT 'Sample Vote', CURRENT_DATE, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), DATEADD('DAY', 1, CURRENT_TIMESTAMP),
             (SELECT MIN(userid) FROM "user")
WHERE NOT EXISTS (SELECT 1 FROM vote WHERE title = 'Sample Vote');

-- Options for the sample vote (only add once if none exist)
INSERT INTO vote_option(voteid, option_text)
SELECT (SELECT MIN(voteid) FROM vote WHERE title = 'Sample Vote'), 'Option A'
WHERE NOT EXISTS (
    SELECT 1 FROM vote_option WHERE voteid = (SELECT MIN(voteid) FROM vote WHERE title = 'Sample Vote')
);

INSERT INTO vote_option(voteid, option_text)
SELECT (SELECT MIN(voteid) FROM vote WHERE title = 'Sample Vote'), 'Option B'
WHERE NOT EXISTS (
    SELECT 1 FROM vote_option WHERE voteid = (SELECT MIN(voteid) FROM vote WHERE title = 'Sample Vote')
);
