#Project components:
1. web-app: web application, bussiness, html 
2. common-utils: common utilities for whole project 

#Run project with gradle:
linux: ```./gradlew :web-app:bootRun ```

windows: ```gradlew :web-app:bootRun```


# Run project with java:
###1. Linux
 - Step1 build jar file: ```./gradlew clean web-app:build -x test```
 - Run with java: java -jar ./build/web-app.jar
 - Access with http://localhost:20124
 ###2. Windows
  - Step1 build jar file: ```gradlew clean web-app:build -x test```
  - Run with java: java -jar build/web-app.jar
  - Access with http://localhost:20124

# Hot build when implement a feature:
- step1 run with gradle like precede step.
- step2 open antoher shell and cd into root folder then type:<br>
 >> for Linux: ```./gradle web-app:classes```<br>
 >> for windows: ```./gradle web-app:classes```

=> For each time you update your *.java, *.html, **, and want to rebuild project, you don't need to restart whole application, you just need

 # Config MySql to using full_group function.
- Modify /etc/mysql/conf.d/mysql.cnf with:
```
 [mysqld]
 lower_case_table_names=1
 sql_mode= STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION
 max_allowed_packet=16M
 ```