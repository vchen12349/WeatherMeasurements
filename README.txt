Created using Spring Tools Suit IDE.
All dependencies should be in the pom.xml
Used chrome extension  postman for testing.
Main file: WeatherMeasurementsApplication.class

Sample messages:

Create measurement
{"dateMeasured":"2015-09-01T17:00:00.000Z","metricsList":[{"value":17,"name":"temperature"},{"value":17,"name":"dewPoint"},{"value":17,"name":"precipitation"}]}

Get measurement for day:
http://localhost:8080/measurements/2015-09-01

Get measurement for timestamp
http://localhost:8080/measurements/2015-09-01T17:00:22.011Z

Replacement measurement
{"metricsList":[{"value":0,"name":"temperature"},{"value":2,"name":"dewPoint"},{"value":0,"name":"precipitation"}]}

Patch measurement
{"metricsList":[{"value":6,"name":"temperature"},{"value":6,"name":"precipitation"}]}

Delete
http://localhost:8080/measurements/2015-09-01T18:00:22.000Z

Get stats
http://localhost:8080/measurements/stats?stat=avg&stat=min&stat=max&metric=precipitation&metric=temperature&fromDateTime=2015-09-01T15:00:00.000Z&toDateTime=2015-09-01T19:00:00.000Z