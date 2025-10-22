# SmartOrder Microservices

Ein einfaches Microservice-System zur Verwaltung von Bestellungen**, Zahlungen**, E-Mail Benachrichtigungen und Lagerbeständen**.  
Das Projekt basiert auf Spring Boot, Kafka, Docker und einem Python Payment-Service.

---

## Architekturüberblick
- **SmartOrder Service** → verwaltet Bestellungen & veröffentlicht Kafka-Events  
- **Payment Service (Python)** → verarbeitet Zahlungen & bestätigt Bestellungen  
- **Inventory Service** → überwacht Lagerbestände  
- **Notification Service** → sendet automatische E-Mail-Benachrichtigungen  
- **Kafka** → Eventbasierte Kommunikation zwischen den Services
---

##  Installation & Start
### Voraussetzungen
- [Docker & Docker Compose](https://docs.docker.com/get-docker/)  
- Java 17  
- Python 3.10+

### Starten des gesamten Systems
```bash
docker-compose up --build
