# Simple Weather API
by João Carlos (https://www.linkedin.com/in/joaocarlosvale/)

## User Story
As a user, I can check if temperature exceeds given limits in the next five days, so that I know how to dress

##Functionality
* Read a list of locations and limit temperatures to monitor from a JSON file
* Periodically request a weather forecast service
  1. using the https://openweathermap.org/api)
  2. The forecast checking frequency must be configurable in the application YAML file (milliseconds fixed delay)
* Check if any temperature limit (in Kelvin) is exceeded in the next five days and store the results in a H2-database

##Initial Setup:
1. create a JSON file storing the locations to monitor, example:
````json
{ "list": [
  {
    "latitude": 10,
    "longitude": 10,
    "minTemp": 10.5,
    "maxTemp": 11
  },
  {
    "latitude": 20,
    "longitude": 20,
    "minTemp": 20.5,
    "maxTemp": 21
  }
]
}
````   
2. application.yaml
```yaml
weather:
  id: <your open weather id>
  url: "api.openweathermap.org/data/2.5/forecast"
  absoluteFilePath: <complete path to JSON containing information about the locations to monitor>
  fixedDelayInMilli: <fixed delay used to query the external weather provider>
```
example:
```yaml
weather:
  id: "758493hjkdlsj"
  url: "api.openweathermap.org/data/2.5/forecast"
  absoluteFilePath: "C:\tmp\locationsToTest.json"
  fixedDelayInMilli: 30000
```

##Available endpoints
1. GET /getMonitoredLocations <br> 
   Responsible to list the current monitored locations and temperature limits.<br>
````json
{
"list": [
{
"latitude": 35.0,
"longitude": 139.0,
"minTemp": 280.0,
"maxTemp": 280.0
},
{
"latitude": 59.0,
"longitude": 24.0,
"minTemp": 270.0,
"maxTemp": 270.0
}
]
}
````
   
2. GET /forecast <br>
    List the next ALERTS sorted by date and time.
````json
[
  {
    "latitude": 59.0,
    "longitude": 24.0,
    "maxTemp": 275.23,
    "minTemp": 274.35,
    "timestamp": 1611694800,
    "dateTimeAsText": "2021-01-26 21:00:00"
  },
  {
    "latitude": 59.0,
    "longitude": 24.0,
    "maxTemp": 274.63,
    "minTemp": 274.39,
    "timestamp": 1611705600,
    "dateTimeAsText": "2021-01-27 00:00:00"
  },...
````
3. PUT /loadLocations <br>
    Reload the list of locations to be monitored from the JSON File configured in the application.yaml.

## Technologies used:
* Java 8
* Spring ecosystem
* Feign Rest Client  
* Hystrix (Circuit-breaker)
* Maven
* H2 database

## Commands:

To compile, test:

    mvn clean install

To run:
    
    mvn spring-boot:run
