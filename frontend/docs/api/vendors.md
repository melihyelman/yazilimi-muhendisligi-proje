Assigments
BASE_URL = /api/vendors

GET - BASE_URL = return List<VendorDTO>
GET - BASE_URL/{vendorId} = return VendorDTO
POST - BASE_URL = create a new vendor you should give json name,department,role=VENDOR(you should give constant), passwordHash(you dont need hash)
PUT - BASE_URL/{vendorId} = update a vendor you can give changed properties(properties i give you with create one)
DELETE - BASE_URL/{vendorId} = delete vendor return nothing


VendorDTO:
    private Long id;
    private String username;
    private UserRole role;
    private String name;
    private String companyName;
