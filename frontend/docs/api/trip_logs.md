Assigments
BASE_URL = /api/trip-logs

GET - BASE_URL = return List<TripLogDTO>
GET - BASE_URL/{tripLogId} = return TripLogDTO
GET - BASE_URL/assignment/{employeeAssigmentId} = return for assigmentId

TripLogDTO:
    private Long id;
    private Long km;
    private EmployeeAssignmentRequestDTO assignment;

EmployeeAssignmentRequestDTO
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean approved;
    private Long employeeId;
    private Long vehicleId;