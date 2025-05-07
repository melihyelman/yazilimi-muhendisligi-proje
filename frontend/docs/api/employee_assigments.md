Employee Assigments
BASE_URL = /api/employee-assignments

GET BASE_URL = return List<EmployeeAssigmentDTO>
GET BASE_URL/{employeeAssigmentId} = return EmployeeAssigmentDTO
POST BASE_URL/{employeeAssigmentId}/approve?km=kmAmount = using for approve assigment you should share kmAmount with request parameter.return EmployeeAssigmentDTO
POST BASE_URL/{employeeAssigmentId}/reject = using for reject assigment. return EmployeeAssigmentDTO
POST BASE_URL = create a new EmployeeAssigment you should give json with startDate,endDate,vehilce_id and employee_id then return EmployeeAssigmentDTO
DELETE BASE_URL/{employeeAssigmentId} = delete a EmployeeAssigment return nothing

EmployeeAssigmentDTO:
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean approved;
    private EmployeeRequestDTO employee;
    private VehicleRequestDTO vehicle;

EmployeeRequestDTO:
    private Long id;
    private String username;
    private String name;
    private String department;
    private List<Long> assignmentsIds;

VehicleRequestDTO:
    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    private String ownership;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;
    private List<Long> assignmentIds;
    private List<Long> readingIds;
    private List<Long> expenseIds;
