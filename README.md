# Spring Batch POC project

A project that reads imaginary product and order information from a postgres db and stores it to a dynamo db.

Features:
* `docker-compose` for local postgres and local dynamo db
* supports testing against the real aws dynamo db
* supports having spring batch metadata stored as in a different database (for example to avoid adding these tables to a real db - see `application.properties` and classes under `com.example.batchdatabase.configuration`. 

## Prerequisites
* Java 11
* Docker
* Docker compose

## Data

The data model of this demo is dummy.
In postgres we store:
* a list of orders, identified by a numeric id
* Each order contains a list of products which are identified by a numeric id too

After running the batch job, information about orders is stored in dynamo as: `orderid` -> `list of product ids`.

`DemoData` creates test data in postgres. See `application.properties` to control how many orders and products get created.


## Local Run (no AWS)
Create a `.env` file in the project root containing: `SPRING_PROFILE=local`
To build and run, run `run.sh`.

## Run against AWS

This may result in a AWS bill and assumes that a IAM user is already setup.

* Run `dynamo-scripts\create-table.sh`
* Edit `.env` to remove the `SPRING_PROFILE` entry but set the aws authentication AWS variables 
(`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`).
* Once done run `dynamo-scripts\delete-table.sh` to delete the table




