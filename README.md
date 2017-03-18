<img src = "https://github.com/bharatnc/micro_balance/blob/master/micro_balance_logo.png?raw=true" height = "113" width= "427" />

'Micro-Balance' is a Client-Side Load Balancer for handling HTTP requests in Microservices Architecture, from the application level. Although it is written in Java, it is not dependent on the type of tech-stack and can be relatively be implemented in any programming language.

##The Implementation

Micro-Balance is written on top of the Java Asynchronous HTTP Client library and currently supports the GET and POST methods. Tests have been written for the GET method and development is still in Progress. Refer to the `Examples.java` file for sample usages.

##Features and Implementation:

1. **DONE**GET Method Testing:heavy_check_mark:
2. **DONE**POST Method Testing:heavy_check_mark:
3. **NOT YET**HEAD Method
4. **NOT YET**DELETE Method
5. **NOT YET**UPDATE Method
6. **NOT YET**OPTIONS Method
7. **NOT YET**CONNECT Method

##Dependencies

Use the **pom.xml** for installing all the dependencies.  

1. Uses JAVA 8 - So having Java 8 installed is a must.
2. Asynchttpclient - For Asynchronous Request
3. JUnit - For Unit Tests
4. Mockito - For mocks and Stubbing within the Java unit test. 