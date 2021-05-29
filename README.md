# Optimal-schedule-creator

This was the first assignment for the course "Artificial Intelligence" of AUEB's Informatics Department. The task we had was to implement a state space search algorithm to find a (nearly) optimal schedule for an imaginary school. The algorithm we chose to use was Beam Search.

**Input:** The program reads two files which specify how many hours each course should be taught in each grade and what teachers are available, for how many hours they are available and what lessons each teacher can teach. The program also reads from the command line the number of classes in each grade.

**Output:** The output of our program is an HTML file with a nearly optimal schedule for the school based on the data given. The program firstly does some quick checks about whether a valid program can be found or not. If the results of these tests are negative we inform the user and the program terminates. If the result is positive, it doesn't necessarily mean that a valid schedule can be found, but that it is just more probable that such a schedule can be found.

During this project we learned about state space search algorithms and had to experiment with some before choosing the best one for the task we had to complete. We also gained experience in representing (near)-real-life situations as states of an AI model. We also had to resolve conflicts between rules that need to hold about any valid schedule and more complicated rules that need to hold for a "good" schedule.

My partners in this project were Vasilis Ballas ([@BLM3826](https://github.com/BLM3826)) and Stella Douka ([@stelladk](https://github.com/stelladk)).
