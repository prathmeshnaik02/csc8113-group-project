# Bookstore Application (Newcastle University - CSC8113)

## About this Project

An online bookstore that automatically scales during traffic spikes, recovers from failures, and deploys updates with zero downtime. The system will include:

- Microservices:
  - Frontend: User interface (React/Node.js)
  - Catalog Service: Manages book listings (Python/Flask).
  - Cart Service: Handles shopping carts (Java/Spring Boot).
  - Database: PostgreSQL for storing orders and inventory
- Key DevOps Goals:
  - Scale services during peak traffic (e.g., Black Friday sales).
  - Recover from database crashes or node failures.
  - Deploy updates to the catalog service without downtime
