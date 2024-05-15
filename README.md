# Fitness-Tracker
This application, developed in Java, offers users a comprehensive platform to monitor and manage their fitness and wellness activities including Step Tracking, Water Intake, Diet Monitoring, Exercise Routines, and Mental Health Assessments. Utilizing core principles of Object-Oriented Programming (OOP) such as Encapsulation, Inheritance, Polymorphism, and Abstraction, this project ensures a robust and scalable architecture. Additionally, it integrates advanced Database Connectivity features for seamless data management and employs Multithreading to enhance performance and responsiveness. Exception handling is meticulously designed to include both built-in and custom exceptions, ensuring reliable error management. The user interface, crafted with JSwing, provides an intuitive and visually appealing environment for users to interact with the application. Structured on a Layered software architecture, this application not only facilitates efficient separation of concerns but also promotes maintainability and flexibility in development and future enhancements. Moreover, the integration of the YouTube API allows the application to offer real-time fitness video recommendations and tutorials directly within the user interface, enhancing the user experience by providing valuable visual guidance and keeping users engaged with dynamic content.

Features:
- Login and Registration: Users can securely log in to their accounts or register for new accounts.
- Activity Tracking: Users can log various activities such as diet, exercise, mental health, steps, and water intake.
- Summary View: Users can view summaries of their logged activities categorized by type.
- Data Reset: Users can reset their data if needed, clearing all logged activities.

Class and Method Descriptions
- HealthTrackerGUI:
  
Description: This class represents the main graphical user interface (GUI) for the Health Tracker application. It sets up the main window and manages different panels for functionalities such as login, registration, activity tracking, summary view, and data reset.

Methods:

loginPanel(): Creates and returns a panel for user login.

registerPanel(): Creates and returns a panel for user registration.

activityPanel(): Creates and returns a panel for logging various health activities.

resetPanel(): Creates and returns a panel for resetting user data.

summaryPanel(): Creates and returns a panel for viewing summaries of logged activities.

fetchUserSpecificData(String category, User user): Fetches data specific to a user and category from the database.

generateTableName(String category, User user): Generates the name of the database table based on the category and user information.

initializeComponents(): Initializes all components and panels within the JFrame.

- User:

Description: This class represents a user of the Health Tracker application. It encapsulates user data and provides methods for user authentication and registration.
register(String username, String password, String name, int age, char gender, double weight, double height, DatabaseHelper dbHelper): Registers a new user with the provided details.

authenticate(String username, String password, DatabaseHelper dbHelper): Authenticates a user with the provided username and password.

- DatabaseHelper:

Description: This class provides helper methods for interacting with the database. It handles database connections, queries, and data manipulation operations.

Methods:

validateUser(String username, String password): Validates a user's credentials by querying the database.
resetUserData(User user): Resets the data associated with a user by deleting their records from the database.

Methods:

validateRegistrationInput(String username, String password, String name, String ageString, String weightString, String heightString):

Description: Validates the input provided during user registration to ensure that it meets certain criteria such as non-empty fields, valid numeric values for age, weight, and height, and alphabetic characters for the name.

fetchUserSpecificData(String category, User user):

Description: Fetches data specific to a user and category from the database. It dynamically generates the table name based on the category and user information and executes a SQL query to retrieve the data.

generateTableName(String category, User user):

Description: Generates the name of the database table based on the category and user information. It follows a specific naming convention to ensure that each user's data is stored in a separate table.

- YouTubeApiService

Description: Provides functionality to search for exercise videos on YouTube using the YouTube Data API.

Methods:

searchExerciseVideos(queryTerm): Searches for exercise videos on YouTube based on the provided query term and prints the URLs of the top 5 search results.

- DatabaseException

Description: Represents an exception thrown when there is a problem accessing the database.

- InvalidNumericValueException

Description: Represents an exception thrown when an invalid numeric value is encountered.

- InvalidTextValueException

Description: Represents an exception thrown when an invalid text value is encountered.

-UserAlreadyExistsException

Description: Represents an exception thrown when attempting to add a user that already exists in the database.

- DietActivity

Description: Represents a diet-related activity for tracking food consumption.

- ExerciseActivity

Description: Represents an exercise-related activity for tracking physical exercises.

- FitnessActivity

Description: Represents a generic fitness activity.

- MentalHealthActivity

Description: Represents a mental health-related activity for tracking emotions and sleep duration.

- StepActivity

Description: Represents a step-related activity for tracking daily steps.
- WaterIntakeActivity

Description: Represents a water intake-related activity for tracking daily water consumption.
