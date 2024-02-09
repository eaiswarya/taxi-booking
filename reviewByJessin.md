#- CODE REVIEW

##- COMMENTS

*-Missing of Global exception Handler-A global exception handler provides a centralized location to handle all uncaught exceptions that occur throughout the application.

*-In UserService line72/-its good to practice separate names for the build one and saved one.

*- Postman collections are not properly saved

*-For post Mapping of addBooking-
	/we can use a custom validation for pickupLocation and dropoutLocation not being same. ValidBooking can be used
	/Eventhough,we added @NotBlank, we are getting a booking with null pickupLocation.So that validation is not working.

*- For post Mapping for cancelBooking-
	/In postman you have given with Request param.Path variable is only needed.
	/In DB,there is no booking with a taxiId or userId.ie,Booking Response is not properly builded

*- In most functionalities,validations are not working

*- /For Account Balance details,addBalance and update Balance are the same with your code.
	/Custom Validation Valid AccountBalance can be given.

*- /Proper validations for requests should be given .
	/Builded response should contain all fields in your response.
