# Hospital Appointment System Accenture

Task 1
Implemented createAppointment method in AppointmentController.java file.

- It handles HTTP POST requests to the "/appointment" endpoint.
- A new ArrayList called appointments is created to store the existing appointments.
  The findAll() method is used to retrieve all existing appointments from the appointmentRepository.
- Checking if the appointment has the same time,room as any existing appointment.
- If it does, HttpStatus.NOT_ACCEPTABLE response is returned with the existing appointments.
- If there is already an appointment with the same ID, HttpStatus.BAD_REQUEST response is returned with the existing appointments.
- If the appointment starts and finishes at the same time, a HttpStatus.BAD_REQUEST response is returned with the existing appointments.
- After checking all if statements, create and save a new appointment using 'save' method from 'appointmentRepository'.

Task 2
JUnit tests
Implemented tests for each Entity class in EntityUnitTest.java file.
- testDoctor, testPatient, testRoom these methods create each entity using entityManager, retrieves,
  and then check if the retrieved entity matches the expected values.
- In the testAppointment method, creating instances of Patient, Doctor, Room.
  Appointment entity is created using these instances and LocalDate.now().
- The persistAndFlush method ensures that the change is flushed to the database immediately.
- The TestEntityManager retrieve the persisted Appointment entity based on the ID.
- Each assertion checks the properties.

Implemented all the unite test in its corresponding class in EntityControllerUnitTest.java file.
- Each test method of the Controller is divided with two tests, NonEmpty or Empty or present or not Present.
  The test method checks the behavior of the 'GET /api/doctors' endpoint if there are no doctors.
  THe mockMvc perform a GET request to /api/doctors, and the response status is NO_CONTENT or expected JSON.
- The same with PatientControllerUnitTest and RoomControllerUnitTest.

Task 3
Creating Dockerfiles.
- Built local images based on Dockerfile.mysql and Dockerfile.maven.
- Run the Docker Containers.
- Pushed them to Docker hub.
- Pulled the images from dockerhub.


Task 4
Develop a UML diagram
- UML.gif