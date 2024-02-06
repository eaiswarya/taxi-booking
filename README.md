# Taxi Booking and Billing System

It is a Taxi Booking and Billing System using Spring Boot, which allows users to book taxis, calculate fares, and handle billing.

Entities :
- Booking
- Taxi
- User

## Endpoints

### User SignUp
- **Endpoint:** `/user/signup`
- **Method:** POST

### User Login
- **Endpoint:** `/user/login
- **Method:** POST

### Add Balance
- **Endpoint:** /user/addBalance
-  **Method:** PUT
### Update Balance
- **Endpoint:** /user/updateBalance/{id}
- **Method:** PUT
### Adding Taxi
- **Endpoint:** /taxi/addingTaxi
- **Method:** POST

### Available Taxis 
- **Endpoint:** /taxi/available
- **Method:** GET
### Add Booking
- **Endpoint:** /booking/add
- **Method:** POST
### Get All Booking
- **Endpoint:** /booking/details
- **Method:** GET
### Get Booking by its ID
- **Endpoint:** /booking/details/{id}
- **Method:** GET
### Cancel Booking
- **Endpoint:** /booking/cancel/{bookingId}
- **Method:** POST
### Get Nearest Taxi
- **Endpoint:** /booking/nearestTaxi
- **Method:** GET
### Calculate Fare
- **Endpoint:** /taxi-booking/fare
- **Method:** POST
### Balance check
- **Endpoint:** /taxi-booking/{id}
- **Method:** GET

## Prerequisites:
- Java Development Kit (JDK) 17
- Maven
- PostgresQl
- Other dependencies


## Testing:

To test the APIs, you can use any API testing tool  Postman. Make sure your server is running, and then send HTTP requests to the API endpoints.


installation:
-  Clone the repository to your local machine:
````
https://github.com/eaiswarya/taxi-booking.git
````
Api Documentation:
````
http://localhost:8082/swagger-ui/index.html
````
- Open the project.
- Ensure you have Java and Maven installed.
