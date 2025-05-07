Assigments
BASE_URL = /api/employees

GET - BASE_URL = return List<EmployeeDTO>
GET - BASE_URL/{employeeId} = return EmployeeDTO
POST - BASE_URL = create a new employee you should give json name,department,role=EMPLOYEE(you should give constant), passwordHash(you dont need hash)
PUT - BASE_URL/{employeeId} = update a employee you can give changed properties(properties i give you with create one)
DELETE - BASE_URL/{employeeId} = delete Employee return nothing


EmployeeDTO:
    private Long id;
    private String username;
    private String name;
    private String department;
    private List<AssignmentRequestDTO> assignments;

AssignmentRequestDTO:
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long vehicleId;
    private List<Long> tripLogIds;
    private String type;