# Ankat

Backend project to get and post duck sightings.

[![Build Status](https://travis-ci.org/heidilind/Ankat.svg?branch=master)](https://travis-ci.org/heidilind/Ankat)

## Requirements
Requires scala version 2.11.6 and scala play version 2.6.9.

## Install
```
$ git clone https://github.com/heidilind/Ankat.git
$ cd duck-server
```
## Run
To start server run
```
$ sbt run
```
## Usage

###To get sightings already made and saved.
GET /sightings

###To get existing species. 
GET /species

###To post a new sighting. Only existing species accepted.

POST sighting/report 
```
{
    "id": 5,
    "species": "lesser scaup",
    "description": "brown and said Kwak",
    "dateTime": "2016-11-29T10:00:01Z",
    "count": 239
}
```



