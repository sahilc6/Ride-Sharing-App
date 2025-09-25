Ride Share

A full-stack ride-sharing platform that enables efficient driver and rider management, and uses the Hungarian Algorithm for optimal driver-rider assignment.[1]

Features

- Driver and Rider registration and management with React frontend.[1]
- Real-time availability tracking for drivers and ride requests for riders.[1]
- Optimized matching of drivers and riders to minimize total travel cost using the Hungarian Algorithm.[1]
- System statistics panel that displays driver/rider counts and match results.[1]
- Spring Boot backend with RESTful APIs for driver, rider, and matching logic.[1]

Tech Stack

- Frontend: React (with React Router), local/session storage for demo persistence.[1]
- Backend: Spring Boot (Java), JPA, REST Controllers, Hungarian Algorithm implementation.[1]
- Database: JPA Entities for Driver, Rider, Location, and Cost.[1]

Installation

1. Clone the repository
   
   git clone <repo-url>
   cd ride-share
   

2. Frontend Setup
   
   cd frontend
   npm install
   npm start
   

3. Backend Setup
   
   cd backend
   ./mvnw spring-boot:run
   
   - Make sure a supported database is configured (JPA).[1]

4. Set Environment
   - Configure API base URLs and environment variables as required in the frontend `.env` file (see example in code).[1]

Usage

- Access the platform in a browser at [http://localhost:3000](http://localhost:3000).
- Use the dashboard for:
  - Driver management: add, remove, and review drivers.
  - Rider management: add, remove, and review riders.[1]
  - Running the driver-rider matching algorithm for optimal assignment.
  - Viewing live system status and statistics.[1]

API Endpoints (Spring Boot Backend)

- `POST /apiv1/drivers` — Add new driver.[1]
- `POST /apiv1/riders` — Add new rider.[1]
- `GET /apiv1/matching/solve` — Run and retrieve matching result.[1]
- `GET /apiv1/matching/status` — Health and status check.[1]
- `GET /apiv1/matching/info` — API info (version, description).[1]

Algorithms

- The backend implements the Hungarian Algorithm for solving the assignment problem to minimize total ride cost between available drivers and requesting riders.[1]

Contributing

- Fork this repository and create a pull request for any improvements or bug fixes.
- Issues and bug reports are welcome![1]
