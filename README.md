# the-weather

Consults weather information.

This program will load the dateset `resources/challenge_dataset.csv`, and will print to the console
the same information plus two additional fields `destination-weather` and  `origin-weather`.

The program will try to fetch the current weather from `open-weather.com` for every airport.
It will fetch a batch of weather reports every minute in order to not overload the API.
The amount of parallel downloads can be configured, but it's set to 6 by default.
After attempting to fetch the weather for all airports, if  there was any error with the API calls,
the program will sleep for two minutes before retrying to fetch the missing weathers.

It will generate an output only after fetching all weather reports.


## Installation
Download and install [leiningen](https://leiningen.org)


## Usage

To run the tests
```
lein test
```

To run the program
```
lein run
```


