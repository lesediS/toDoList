<h1>JSF To Do List</h1>

<h1>Getting Started</h1>
<h2>Prerequisites</h2>
Before you begin, ensure you have the following installed:

<ul>
<li>SQL</li>
<li>MySQL Workbench</li>
<li>Java Development Kit (JDK)</li>
<li>Eclipse IDE</li>
</ul>

<h1>Setup</h1>
<ol>
<li>Ensure MySQL is running:</li>
<p>Make sure MySQL is running in your services. You can check this by opening MySQL Workbench and ensuring that the server status is running. You can also open Services from the Start menu
and navigate to MySQL in the list and starting it if it has not already started.</p>

<li>Run the MySQL Script:</li>
<p>Locate the MySQL script in the resources folder of the project directory.
Open MySQL Workbench and connect to your MySQL server.
Open a new SQL tab and copy the contents of the MySQL script from the resources folder.
Execute the script to set up the necessary database and tables.</p>
</ol>

<h1>Running the Project</h1>
<ul><li>Open the project in your Eclipse IDE.</li>
<li>Run the project by clicking the 'Run' button or using the appropriate command for your IDE.</li>
</ul>

<h2>Using the Application</h2>
<h3>Sign Up</h3>
<ul>
<li>If it is your first time running the project or if you want to create another user, click on the "Sign Up" button.</li>
<li>Fill in all the required fields on the Sign Up page.</li>
<li>Click the "Sign Up" button to create a new account. You will be redirected to the Login page.</li></ul>

<h3>Login</h3>
<ul>
<li>On the Login page, enter your credentials.</li>
<li>Click the "Login" button to access your account.</li>
</ul>

<h3>Adding a Task</h3>
<ul>
<li>To add a new task, click the plus button (+) or the menu option.</li>
<li>On the New Task page, fill in the task details.</li>
<li>Click "Add Task" to create the new task.</li>
<li>Alternatively, you can click "Clear" to clear all the fields.</li>
<li>If you want to go back to the Home Page without adding a task, click "Cancel" or use the Home menu option.</li>
</ul>

<h3>Database Interaction</h3>
<ul>
<li>The console will log interactions for debugging and tracking purposes.</li>
<li>You can view and manage entries directly in MySQL Workbench.</li>
<li>Each task is saved according to the logged-in user by linking the user's ID to the task using a Foreign Key relationship in the database.</li>
</ul>


<h1>Notes</h1>
&#10003; Ensure that MySQL is running whenever you want to use the application.<br>
&#10003; Regularly check the console for any logged interactions to troubleshoot issues.<br>
&#10003; Always run the MySQL script initially to set up the database correctly.
