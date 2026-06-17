# Threat Intel Dashboard

A full-stack web application that checks IP address reputation using the [AbuseIPDB](https://www.abuseipdb.com) API. Enter any IP address and instantly see its abuse confidence score, country of origin, ISP, and full report history persisted to a PostgreSQL database.

Built with Java, Spring Boot, PostgreSQL, and Docker.

## Features

- Look up any IP address and get an abuse confidence score (0–100)
- Risk classification: LOW / MEDIUM / HIGH with color-coded UI
- Lookup history persisted to PostgreSQL
- Per-IP history view showing all previous checks
- Fully containerized with Docker Compose — one command to run the entire stack

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3, Spring Data JPA |
| Frontend | Thymeleaf, Bootstrap 5 |
| Database | PostgreSQL 15 |
| DevOps | Docker, Docker Compose |
| External API | AbuseIPDB |

---

## Getting Started

### Prerequisites, you need...

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- A free AbuseIPDB API key — register at [abuseipdb.com/register](https://www.abuseipdb.com/register)

---

### Installation, how to set it up!

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/threat-intel-dashboard.git
cd threat-intel-dashboard
```

**2. Create your `.env` file**
```bash
cp .env.example .env
```
Open `.env` and replace `your_api_key_here` with your AbuseIPDB API key:
```
ABUSEIPDB_API_KEY=your_real_key_here
```

**3. Start the application**
```bash
docker-compose up --build
```

**4. Open in your browser**

| Service | URL | Description |
|---|---|---|
| Dashboard | http://localhost:8080 | Main application |
| pgAdmin | http://localhost:5050 | Visual database browser |

---

## Using the Dashboard

### Checking an IP Address

1. Open **http://localhost:8080**
2. Type any IP address into the search bar (e.g. `118.25.6.39`)
3. Click **Check IP**
4. The result card will appear showing:
   - **Abuse Score** — 0% (clean) to 100% (malicious)
   - **Risk Level** — LOW (0–24%), MEDIUM (25–74%), HIGH (75–100%)
   - **Country** — where the IP is registered
   - **ISP** — the internet service provider
   - **Domain** — associated domain name
   - **Total Reports** — number of times reported on AbuseIPDB

Every lookup is automatically saved to the database and appears in the **Recent Lookups** table at the bottom of the page. 

NOTE: As of now, this project only takes in public IPv4 addresses... meaning, private and internal ips, Localhosts, Ipv6 and domain names/urls will not return a valid result...

### Viewing IP History

Click the **History** button next to any IP in the Recent Lookups table to see every time that IP has been checked, along with its score at each point in time. This is useful for tracking whether an IP's reputation changes over time.

### Browsing the Database (pgAdmin)

1. Open **http://localhost:5050**
2. Log in: Email `admin@admin.com` / Password `admin`
3. In the left panel: right-click **Servers** → **Register** → **Server**
4. Fill in the Connection tab:
   - **Host:** `db`
   - **Port:** `5432`
   - **Database:** `threatintel`
   - **Username:** `postgres`
   - **Password:** `postgres`
5. Click **Save**
6. Navigate to: **Servers → threatintel → Databases → threatintel → Schemas → public → Tables → ip_lookups**
7. Right-click `ip_lookups` → **View/Edit Data** → **All Rows**

---

## Stopping the App

Stop and remove containers:
```bash
docker-compose down
```

Stop and also delete all saved lookup data:
```bash
docker-compose down -v
```

---

## Quick Commands Reference!!

| Action | Command |
|---|---|
| Start the app | `docker-compose up --build` |
| Start without rebuilding | `docker-compose up` |
| Stop the app | `docker-compose down` |
| Wipe the database | `docker-compose down -v` |
| View live logs | `docker-compose logs -f app` |

---

## Project Structure...

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
├── docker-compose.yml               # Orchestrates app + db + pgAdmin
├── .env.example                     # Template for API key setup
└── .gitignore                       # Excludes secrets and build output
```
