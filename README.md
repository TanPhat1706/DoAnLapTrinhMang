# DoAnLapTrinhMang

Nơi tình yêu bắt đầu

## Voting API (Spring Boot)

Base URL: http://localhost:8080

### Entities

- User [User]
- Role [Role]
- Class [Class]
- Faculty [Faculty]
- Vote, VoteOption, VoteResult

### Endpoints

- POST /api/votes

  - Params: title (string), createdBy (int), start (ISO-8601, optional), end (ISO-8601, optional)
  - Create a new vote (anyone can create)

- POST /api/votes/{voteId}/options

  - Params: text (string)
  - Add an option to the vote

- POST /api/votes/{voteId}/cast

  - Params: optionId (int), userId (int)
  - Cast a vote (1 vote per user per vote)

- GET /api/votes/{voteId}/results

  - Returns: { optionId: count }

- GET /api/votes/{voteId}/qrcode

  - Query: baseUrl (default http://localhost:8080)
  - Returns: PNG QR of link to the vote

- GET /api/votes/{voteId}/export
  - Returns: Excel (xlsx) with vote result counts

### Quick start

Option A — Dev mode (khuyên dùng, không cần cài DB):

1. Chạy với profile dev (H2 in-memory):

- Windows: `mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dspring.sql.init.data-locations=classpath:data-dev.sql`
- Dùng H2 MODE=MSSQLServer, tự seed dữ liệu mẫu -> mở Postman là gọi được ngay.

Option B — SQL Server:

1. Ensure SQL Server is running and database `VotingApp` exists.
2. Update `src/main/resources/application.properties` if needed.
3. Build and run:

- Windows (CMD/PowerShell): `mvnw.cmd spring-boot:run`
- Or with system Maven: `mvn spring-boot:run`

1. Ensure SQL Server is running and database `VotingApp` exists.
2. Update `src/main/resources/application.properties` if needed.
3. Build and run:
   - Windows (CMD/PowerShell): `mvnw.cmd spring-boot:run`
   - Or with system Maven: `mvn spring-boot:run`

### Notes

- Table names that are keywords are quoted (e.g., [User], [Class], [Role]) for SQL Server.
- Use the provided repositories to seed initial data (e.g., Role, Faculty, Class, User) or add a `data.sql`.
