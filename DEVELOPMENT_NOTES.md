Loan Service – Development Notes

1. Overall Approach

The application was implemented using a layered architecture following standard Spring Boot practices:

- Controller layer for REST endpoints
- Service layer for business logic and eligibility evaluation
- Domain layer for entities and enums
- Repository layer for persistnce
- Utility class for EMI calculation
- Global exception handler for validation errors

- The system processes a loan application request, applies validation and business rules, calculates interest and EMI .

H2 in-memory database was used for simplicity and demonstration purposes.

---

2. Key Design Decisions

Layered Architecture
Separated concerns clearly between controller, service, repository, and domain to improve maintainability and testability.

BigDecimal for Financial Calculations
Used BigDecimal with scale = 2 and RoundingMode.HALF_UP to ensure precision and avoid floating-point inaccuracies.

Dedicated EMI Calculator Utility
Extracted EMI logic into a separate utility class to:
- Improve readability
- Enable isolated unit testing

Risk Band Classification
Encapsulated risk classification logic inside the service to keep it close to eligibility logic.

Validation Using Bean Validation
Used Jakarta validation annotations for request validation and a global exception handler to return meaningful HTTP 400 responses.

---
 3.Trade-offs Considered


In-Memory Database
H2 was used for faster setup and demonstration. In production, a relational database like PostgreSQL or MySQL would be preferable.

Business Logic Location
All eligibility logic is centralized in the service class for clarity.
In a larger system, this could be broken into smaller strategy-based components.



## 4. Assumptions Made

- Tenure in years for age + tenure validation is calculated using integer division.
- EMI rejection at 60% and offer validity at 50% are treated as strict greater-than comparisons.
- No partial approvals or alternate tenure offers are generated.
- No authentication or authorization is required.
- Currency handling assumes a single currency context.


5. Improvements With More Time

- Add comprehensive unit tests for:
    - EMI calculation
    - Risk band classification
    - Eligibility logic
- Add integration tests using @SpringBootTest.
- Implement structured logging.
- Introduce Swagger/OpenAPI documentation.
- Improve error response format with standardized error codes.
- Use Strategy pattern for risk and premium calculation to improve extensibility.
- Add Docker support for containerized deployment.