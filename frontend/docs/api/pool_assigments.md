Employee Assigments
BASE_URL = /api/pool-assignments

GET BASE_URL = return List<PoolAssignmentDTO>
GET BASE_URL/{employeeAssigmentId} = return PoolAssignmentDTO
POST BASE_URL = create a new EmployeeAssigment you should give json with startDate,endDate,vehilce_id then return PoolAssignmentDTO
DELETE BASE_URL/{employeeAssigmentId} = delete a EmployeeAssigment return nothing

PoolAssignmentDTO:
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean approved;
    private VehicleRequestDTO vehicle;

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
