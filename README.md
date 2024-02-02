# Taxi Booking and Billing System

It is a Taxi Booking and Billing System using Spring Boot, which allows users to book taxis, calculate fares, and handle billing.

Entities : 
- Booking
- Taxi
- User


## Endpoints

- @PostMapping("/add")- Adds a new Booking
- @GetMapping("/details")-Get list of Bookings
- @DeleteMapping("/cancel/{id}")-Cancel a Booking
-  @PostMapping("/fare/{userId}")
- @PutMapping("/updateBalance/{id}")

- @PostMapping("/user/signup")
- @PostMapping("/user/login")



### Prerequisites:

- JDK 17
- Maven
- Other dependencies
- GitHub
- Lombok

## Testing

To test the APIs, you can use any API testing tool  Postman. Make sure your server is running, and then send HTTP requests to the API endpoints.


installation:
-  Clone the repository to your local machine:
````
https://github.com/eaiswarya/taxi-booking.git
````
- Open the project.
- Ensure you have Java and Maven installed.
