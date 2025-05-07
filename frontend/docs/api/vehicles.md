Assigments
BASE_URL = /api/vehicles

GET - BASE_URL = return List<VehicleDTO>
GET - BASE_URL/{vehicleId} = return VehicleDTO
POST - BASE_URL = return VehilceDTO using for creating a vehilce, you should give json properties like this:  ownership is 2 type OWNED and LEASED if ownership is leased you should give a 2 dates, otherwise you dont need to give dates.
{
  "plateNumber": "test 3",
  "brand": "honda",
  "model": "civi",
"ownership": "OWNED",
"leaseStartDate": "2025-04-04",
"leaseEndDate": "2025-05-05"
}
PUT - BASE_URL/{vehicleId} = update a vehicle you can give changed properties(properties i give you with create one)
DELETE - BASE_URL/{vehicleId} = delete vehicle return nothing
GET - BASE_URL/{vehcileId}/current-odo = return int means vehicle current odometear readings count
GET - BASE_URL/{vehcileId}/odometer = return {mileage: km} means vehicle total odometear readings count
POST - BASE_URL/{vehcileId}/assign/pool = with json startDate,endDate and mission dates are LocalDate. return PoolAssignmentDTO
POST - BASE_URL/{vehcileId}/assign/employee = with json startDate,endDate and employeeId dates are LocalDate. return EmployeeAssignmentDTO
GET - BASE_URL/{vehcileId}/forecast = return {forecast: avg} using for expenseforecast

VehicleDTO:
    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    private String ownership;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;
    private List<AssignmentRequestDTO> assignments;
    private List<OdometerReadingRequestDTO> readings;
    private List<ExpenseRequestDTO> expenses;

AssignmentRequestDTO:
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long vehicleId;
    private List<Long> tripLogIds;
    private String type;


OdometerReadingRequestDTO:
    private Long id;
    private LocalDate date;
    private int km;
    private Long vehicleId;

ExpenseRequestDTO:
    private Long id;
    private LocalDate date;
    private String type;
    private Double amount;
    private String description;
    private Long vehicleId;
    private Long vendorId;
    private Long validatedById;

PoolAssignmentDTO:
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean approved;
    private VehicleRequestDTO vehicle;

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
