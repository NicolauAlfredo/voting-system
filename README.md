# Java Voting System API with Frontend

A complete **Java-based electronic voting system**, built entirely **without frameworks**.  
This project demonstrates full backend skills using **pure Java**, **Maven**, **RESTful APIs**, and **JSON file-based persistence**, along with a modern and lightweight **HTML/CSS/JS frontend**.

---

## Features

### Backend (Java 11+)

- RESTful API built with `com.sun.net.httpserver`
- JSON persistence using local files (`candidates.json`, `votes.json`)
- Full CORS support
- Candidate management (`POST /candidates`)
- Voting system (`POST /vote`)
- Live result tracking (`GET /results`)

### Frontend (HTML + CSS + JS)

- Add candidates via form
- Vote in real-time using Voter ID
- View live vote results
- Responsive interface with clean styling

---

## Technologies

- Java 11+
- Maven
- Jackson (JSON)
- HTML5, CSS3, JavaScript (Vanilla)
- Python 3 (for serving frontend locally)
- Git + GitHub

---

## How to Run the Application

### 1 Clone the repository

```bash
git clone https://github.com/yourusername/voting-system.git
cd voting-system
```

### 2. Run the Java Backend
- Make sure you have Java 11+ and Maven installed.

```bash
mvn clean compile exec:java
```

- This will start the server at:

```bash
http://localhost:8000
```

- You should see in your terminal:

```bash
Server running at http://localhost:8000
```

### 3. Run the Frontend Locally

```bash
cd frontend
python -m http.server 5500
```
- Then open your browser at:

```arduino
http://localhost:5500
```

## API Usage (Examples)

- Add Candidate

```http
POST /candidates
Content-Type: application/json

{
  "id": 1,
  "name": "Nicolau"
}
```

- Vote

```http
POST /vote
Content-Type: application/json

{
  "voterId": "voter123",
  "candidateId": 1
}
```

- Get Results

```http
GET /results
```

Returns:
```json
{
  "Nicolau": 2,
  "Alfredo": 1
}
```
## JSON Persistence
All data is stored in two files:

- candidates.json — list of all registered candidates

- votes.json — all votes cast

These are automatically created if not present, and updated in real-time.

## License
This project is open source and available under the MIT License.
