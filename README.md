###########################################################################
###########################################################################
  Proto Sloth README file
###########################################################################
###########################################################################

Proto Sloth is an implementation of Conway's Game of Life.

!!! DEVELOPER NOTE: For the remainder of this document, the project root is
defined as the directory that this README is located within.

###########################################################################
- Development Environment Requirements
###########################################################################
To set up for execution of this development environment, the following
installations will be required.
   * JDK 1.6+ (http://www.oracle.com/technetwork/java/javase/downloads/index.html)
   * Maven 3.0+ (http://maven.apache.org/)

After installation of the development tools, initialize the development
environment by typing "mvn compile" from root. A "BUILD SUCCESS" message.

###########################################################################
- Basic Development Environment Features
###########################################################################

The development environment is composed of 3 modules.
   * root/sloth-api - Contains the Game of Life API.
   * root/sloth-core - This is an implementation of the sloth-api.
   * root/sloth-rest - This is Web Service implementation of the Game of Life
                       by wrapping sloth-core in a Springframework container.
-- Build Commands
"mvn clean" : Will clean all generate source and artifacts
"mvn compile" : Will generate and compile source
"mvn test" : Will invoke integrated test suites. Test result reports can be
             viewed at root/<module>/target/surefire-reports/index.html.
"mvn package" : Will generate target artifact for each module, with artifact
                being generated in root/<module>/target directory.
"mvn site" : Will generate a complete project overview to include dependency,
             javadoc, code style check, and test reports. Results can be viewed
             at root/<module>/target/site/index.html and root/target/site/index.html.
"mvn -help" : List maven commands and usage

###########################################################################
- Running the Embedded Jetty REST API
###########################################################################

The development environment support an embedded Jetty server that is used
to run sloth-rest web service application.

-- Running the web service
1: cd sloth-rest
2: mvn jetty:run
3(to stop): CTRL-C

-- REST POINTS
To get the current world
     GET http://localhost:8092/sloth/world
To get the current iteration of the world
     GET http://localhost:8092/sloth/world/location?x=<x-coordinat>&y=<y-coordinate>
To get the current iteration of the world
     GET http://localhost:8092/sloth/world/iteration
To spin the world through so many iteration counts
     POST http://localhost:8092/sloth/world/spin?count=<default is 1 if parameter not included>
To create a new world
     POST http://localhost:8092/sloth/creator/world?upperBound=<the upper bound size of the world, defaults to 5>
