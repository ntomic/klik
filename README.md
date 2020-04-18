# KONZUM-KLIK

### What does it do
The service parses Konzum site (every 10 minutes) for information about delivery availability.  
If delivery is available (for location: Zagreb), the service sends a notification to Slack

## Prerequisites
* JDK 10 (or newer)

## Setup
* change yml file:  
1. change cron info if needed
2. add bearer authorization token for your slack-bot
eg. xoxp-215666943653-.......
3. add konzum cookie authorization 
(paste cookie from konzum request headers when logged in to Konzum site)  
eg. token=InGJbFz..........

## Running
* build with: gradlew build
* run with: gradlew bootRun
* build jar with: gradlew bootJar
* run jar with: java -jar klik.jar
* put application.yml in the same location as .jar file  
* if .yml path is different than .jar location, run .jar with jvm arg:  
 --spring.config.additional-location=file:{yml_file_path} 
* stop jar with: ctrl + c
