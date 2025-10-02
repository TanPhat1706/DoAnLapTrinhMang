-- Seed base roles
IF NOT EXISTS (SELECT 1 FROM [Role] WHERE RoleName = 'Admin') INSERT INTO [Role](RoleName) VALUES ('Admin');
IF NOT EXISTS (SELECT 1 FROM [Role] WHERE RoleName = 'Student') INSERT INTO [Role](RoleName) VALUES ('Student');
IF NOT EXISTS (SELECT 1 FROM [Role] WHERE RoleName = 'Lecturer') INSERT INTO [Role](RoleName) VALUES ('Lecturer');

-- Seed one faculty
IF NOT EXISTS (SELECT 1 FROM Faculty WHERE FacultyName = 'Computer Science')
INSERT INTO Faculty(FacultyName) VALUES ('Computer Science');

-- Seed one class
IF NOT EXISTS (SELECT 1 FROM [Class] WHERE ClassName = 'CTK45A')
INSERT INTO [Class](ClassName, YearStart, FacultyID)
SELECT 'CTK45A', 2025, MIN(FacultyID) FROM Faculty;

-- Seed one user (Admin)
IF NOT EXISTS (SELECT 1 FROM [User] WHERE Email = 'admin@example.com')
INSERT INTO [User](StudentCode, FullName, Email, Gender, BirthYear, Phone, ClassID, RoleID)
SELECT NULL, 'System Admin', 'admin@example.com', 'M', 1990, '0123456789',
       (SELECT MIN(ClassID) FROM [Class]),
       (SELECT RoleID FROM [Role] WHERE RoleName = 'Admin');

-- Seed one user (Student)
IF NOT EXISTS (SELECT 1 FROM [User] WHERE Email = 'student1@example.com')
INSERT INTO [User](StudentCode, FullName, Email, Gender, BirthYear, Phone, ClassID, RoleID)
SELECT 'SV0001', 'Student One', 'student1@example.com', 'F', 2004, '0987654321',
       (SELECT MIN(ClassID) FROM [Class]),
       (SELECT RoleID FROM [Role] WHERE RoleName = 'Student');

-- Seed a vote
IF NOT EXISTS (SELECT 1 FROM Vote WHERE Title = 'Sample Vote')
INSERT INTO Vote(Title, CreatedDate, StartTime, EndTime, CreatedBy)
SELECT 'Sample Vote', CONVERT(date, GETDATE()), DATEADD(hour, -1, GETDATE()), DATEADD(day, 1, GETDATE()),
       (SELECT MIN(UserID) FROM [User]);

-- Seed two options for the sample vote
DECLARE @voteId INT;
SELECT @voteId = MIN(VoteID) FROM Vote WHERE Title = 'Sample Vote';
IF NOT EXISTS (SELECT 1 FROM VoteOption WHERE VoteID = @voteId)
BEGIN
    INSERT INTO VoteOption(VoteID, OptionText) VALUES (@voteId, 'Option A');
    INSERT INTO VoteOption(VoteID, OptionText) VALUES (@voteId, 'Option B');
END
