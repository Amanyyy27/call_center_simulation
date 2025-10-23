# call_center_simulation
EEX5362 - Performance Modelling Case Study

DESCRIPTION
This project models a call center system using discrete even simulation (DES) to analyze the perofrmance under different conditions. The simulation evaluates how factors such as arrival rate service time or staffing levels affect the efficiency and customer satisfacton.

STRUCTURE
Within the "src" branch you can find the following files;
  Customer.java - defines customer behaviour and their patience levels
  Agent.java - represents the call center agents and their service handling
  Event.java/EventType.java - manage simulation events like arrival and customers leaving the queue
  CallCenterSimulaiton.java - core simulation logic

And within the "results" branch you will find SimulationMain.java where we run multiple test scenarios and outputs results for analysis.

HOW TO RUN THE PROJECT
1. compile all the files
       javac src/*.java results/SimulationMain.java
2. run the simulation
       java results.SimulationMain

SIMULATED SCENARIOS
1. Low load conditions
2. High load conditions
3. Increased Staffing
4. Low patience
5. Faster Service


