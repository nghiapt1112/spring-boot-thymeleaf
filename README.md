#Project components:
1. web-app: web application, bussiness, html 
2. common-utils: common utilities for whole project 

#Run project with gradle:
linux: ```./gradlew :web-app:bootRun ```

windows: ```gradlew :web-app:bootRun```


# Run project with java:
###1. Linux
 - Step1 build war file: ```./gradlew clean web-app:build -x test```
 - Run with java: java -jar ./build/web-app.war
 - Access with http://localhost:20124
 ###2. Windows
  - Step1 build war file: ```gradlew clean web-app:build -x test```
  - Run with java: java -jar build/web-app.war
  - Access with http://localhost:20124

# Hot build when implement a feature:
- step1 run with gradle like precede step.
- step2 open antoher shell and cd into root folder then type:<br>
 >> for Linux: ```./gradle web-app:classes```<br>
 >> for windows: ```./gradle web-app:classes```

=> For each time you update your *.java, *.html, **, and want to rebuild project, you don't need to restart whole application, you just need 