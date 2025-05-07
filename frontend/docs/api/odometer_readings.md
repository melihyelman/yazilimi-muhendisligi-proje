Assigments
BASE_URL = /api/readings

GET - BASE_URL = return List<OdometerReadingDTO>
GET - BASE_URL/{readingId} = return OdometerReadingDTO
POST - BASE_URL = create a new reading you should give json date(LocalDate),km(Integer),vehicle_id then return OdometerReadingDTO.
PUT - BASE_URL/{readingId} = update a reading you can give changed properties(properties i give you with create one)
DELETE - BASE_URL/{readingId} = delete reading return nothing
GET - BASE_URL/vehicle/{vehicleId} = return List<OdometerReadingDTO> 


OdometerReadingDTO:
    private Long id;
    private LocalDate date;
    private int km;
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