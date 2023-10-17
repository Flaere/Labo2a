# Labo2a

Preliminary remarks: It is important to take the following points into account:
1. Two REST controllers: you need to implement the below functionalities for both the Level 2 REST
  controller (in MealsRestRpcStyleController.java) and the Level 3 REST controller (in MealsRestCon-
  troller.java).

3. HTTP response status codes: your REST API should return an appropriate response status code for
  each API call.

5. HTTP verbs: your REST API should make use of an appropriate HTTP verb for each API call on
  resources.
6. Hypermedia-driven API : your REST API should reflect the appropriate set of URIs for each resource.
  Searching meals that match a constraint. You should add two functions, for getting the cheapest and
  the largest meal, to both the MealsRestRpcStyleController and MealsRestController controllers.
  Adding/updating/deleting a meal. You need to add 3 remote operations to your REST controllers to
  support (1) meal addition, (2) meal update, and (3) meal deletion. For the addition and update, the meal
  object should be transmitted in the HTTP body. Make sure you use the right HTTP verbs and status
  codes. In the MealsRestRpcStyleController it is sufficient to return HTTP Status code 200 in case of success
  and HTTP Status code 404 in case the meal is not found. In MealsRestController you can also return
  HTTP Status code 201 when a new meal is created by using ResponseEntity.created, which takes a
  URI pointing to the newly created meal as a parameter, after which you can add the body to the response.
  Measuring the delay of remote operations. We will now run the server and test client on two
  different machines in the PC labs.

7. Run the application server on your local machine that you are working on and leave it running.
  Open a new terminal window and ssh to a different machine in the PC labs (see the list at https:
  //mysql.student.cs.kuleuven.be/) and go to your project directory.

  Execute the client requests again on the remote client machine, but use your own machine’s name
  instead of localhost for the server. For example, assuming you are working on Aalst and are using
  Gent as remote client machine:
  gent$ curl -v -X GET http://aalst.student.cs.kuleuven.be:8080/rest/meals
  
  You can get additional information about the timing of your request using the -w option of curl.
  What is the response time of the remote REST operations? What is the response time when testing
  locally?
  gent$ curl -v -X GET http://aalst.student.cs.kuleuven.be:8080/rest/meals
  -w "\n time: %{time_total}\n"
