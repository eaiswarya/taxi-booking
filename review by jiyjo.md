# Code Review

###  Review Comments

* JwtServiceTest is not completed

* In UserController there is addBalance and updateBalance. Both have same purpose. Only updateBalance is needed.

* Likewise, in UserService only updateBalance is needed addBalance is not needed.

* addBalance can be removed from UserServiceTest and UserControllerTest.

* There is a method availableTaxi in TaxiController and searchTaxi in BookingController, both serve the same purpose so searchTaxi from BookingController is only needed.

* Likewise, TaxiService, TaxiController, TaxiServiceTest,TaxiControllerTest can be changed.