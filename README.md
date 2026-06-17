# Threat Intel Dashboard

A full-stack web application that checks IP address reputation using the [AbuseIPDB](https://www.abuseipdb.com) API. Built with Java, Spring Boot, PostgreSQL, and Docker.

![Dashboard Preview](docs/preview.png)

## Features

- Look up any IP address and get an abuse confidence score (0–100)
- Risk classification: LOW / MEDIUM / HIGH with color-coded UI
- Lookup history persisted to PostgreSQL
- Per-IP history view
- Fully containerized with Docker Compose (one command to run)

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3, Spring Data JPA |
| Frontend | Thymeleaf, Bootstrap 5 |
| Database | PostgreSQL 15 |
| DevOps | Docker, Docker Compose |
| External API | AbuseIPDB |

## Getting Started

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- Free AbuseIPDB API key from [abuseipdb.com/register](https://www.abuseipdb.com/register)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/threat-intel-dashboard.git
   cd threat-intel-dashboard
   ```

2. Create your `.env` file:
   ```bash
   cp .env.example .env
   ```
   Then open `.env` and replace `your_api_key_here` with your AbuseIPDB key.

3. Start everything with Docker Compose:
   ```bash
   docker-compose up --build
   ```

4. Open your browser:
   - **Dashboard:** http://localhost:8080
   - **pgAdmin (DB viewer):** http://localhost:5050
     - Email: `admin@admin.com` / Password: `admin`

### Stopping the app
```bash
docker-compose down
```

To also delete the database volume:
```bash
docker-compose down -v
```

## Project Structure

```
threat-intel-dashboard/
├── src/main/java/com/threatintel/
│   ├── ThreatIntelApplication.java   # Entry point
│   ├── controller/
│   │   └── DashboardController.java  # Handles HTTP requests
│   ├── service/
│   │   ├── IpLookupService.java      # Business logic
│   │   └── AbuseIpDbService.java     # External API integration
│   ├── model/
│   │   ├── IpLookup.java             # Database entity
│   │   └── AbuseIpDbResponse.java    # API response DTO
│   └── repository/
│       └── IpLookupRepository.java   # Database layer
├── src/main/resources/
│   ├── templates/
│   │   ├── dashboard.html            # Main UI
│   │   └── history.html             # Per-IP history
│   └── application.properties       # App config
├── Dockerfile                        # Containerizes the Spring Boot app
├── docker-compose.yml               # Orchestrates app + db + pgadmin
└── .env                             # API key (never commit this)
```
