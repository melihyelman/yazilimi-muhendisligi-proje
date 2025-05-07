Assigments
BASE_URL = /api/expenses

GET - BASE_URL = return List<ExpenseDTO>
GET - BASE_URL/{expenseId} = return ExpenseDTO
POST - BASE_URL?vehicleId=id&vendorId=id&validatedBy=id = return ExpenseDTO you should give vehicleId,vendorId,validatedById(user) with request parameter and you should give expense properties with body json. properties: date(LocalDate), type(only = MAINTENANCE,INSURANCE,FUEL,REPAIR,TIRE), amount(double), description(string).
PUT - BASE_URL/{expenseId} = update a expense you can give changed properties(properties i give you with create one)
DELETE - BASE_URL/{expenseId} = delete expense return nothing
GET - BASE_URL/total = return json {total: amount}

ExpenseDTO:
    private Long id;
    private LocalDate date;
    private String type;
    private Double amount;
    private String description;
    private VehicleRequestDTO vehicle;
    private VendorRequestDTO vendor;
    private UserResponse validatedBy;

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

VendorRequestDTO:
    private Long id;
    private String username;
    private UserRole role[ADMIN,VENDOR,EMPLOYEE];
    private String name;
    private String companyName;
    
UserResponse:
    private Long id;
    private String username;
    private UserRole role[ADMIN,VENDOR,EMPLOYEE];