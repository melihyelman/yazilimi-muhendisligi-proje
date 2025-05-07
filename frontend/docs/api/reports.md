Assigments
BASE_URL = /api/reports

GET - BASE_URL = get all reports return Report
POST - BASE_URL = generete report all vehicles, return List<ReportDTO>
POST - BASE_URL/{vehicleId} = generate report single vehicle, return ReportDTO


public record ReportDTO(
        VehicleRequestDTO vehicle,
        int currentOdometer,
        int mileageInPeriod,
        Map<String, Double> expensesByType,
        double totalExpense,
        int assignmentCount
) {}


VehicleRequestDTO {
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


Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate createdDate;
    @Lob
    private String content;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User generatedBy;

User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;


ExpenseType {
    MAINTENANCE,
    INSURANCE,
    FUEL,
    REPAIR,
    TIRE
}