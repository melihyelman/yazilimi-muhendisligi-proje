Assigments
BASE_URL = /api/assigmennts

GET - BASE_URL = return List<AsssigmentDTO>
GET - BASE_URL/{assigmentId} = return AssigmentDTO
DELETE - BASE_URL/{assigmentId} = delete Assigment return nothing

AssigmentDTO:
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private VehicleDTO vehicle;
    private List<TripLogRequestDTO> tripLogs;
    private String type;

TripLogRequestDTO:
    private Long id;
    private Long km;
    private Long assignmentId;