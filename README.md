# WAES - Scalable Web Api

This is the result of the assignment given.

## Usage

### Requirments

 Application was developed using java 1.8, maven as a build tool and versioned using github.io.
 - Maven (https://maven.apache.org/)
 - Java 1.8 (https://adoptopenjdk.net/)
 - Git client (https://git-scm.com/download/)

Make sure you have the above software installed and configured in your machine. Execute the following to get the code and build the software.

```sh
$ git clone https://github.com/krinosx/marketdata_parser.git
$ cd base64-diff-api
$ mvn package
```
The above instructions will build an executable jar inside the <project_home>/target folder. In order to start the application run
```sh
$ mvn spring-boot:run
```
It will start an embedded Apache Tomcat server listening on 8080 port and use default profile. The default profile will use a local in memory storage.

To deploy in a production environment or to use this app as scalable web app use the mongodb profile.
```sh
$ mvn spring-boot:run -Dspring.profiles.active=mongodb
```

## Endpoints Description

| Endpoint | Method | curl command | Description |
| ------ | ---- |------ |-----|
| http://{host}:{port}/v1/diff/{ID}/left | PUT | curl -i -H Accept:application/json -X PUT http://localhost:8080/v1/diff/{ID}/left -H Content-Type: application/json -d '{Base64 encoded content}' | Set the right side document to be compared for the given ID. If there is no comparision request with given ID it will create one. |
| http://{host}:{port}/v1/diff/{ID}//right | PUT | curl -i -H Accept:application/json -X PUT http://localhost:8080/v1/diff/{ID}/right -H Content-Type: application/json -d '{Base64 encoded content}' | Set the right side document to be compared for the given ID. If there is no comparision request with given ID it will create one. |
| http://{host}:{port}/v1/diff/{ID}/ | GET | curl -i -H Accept:application/json -X GET http://localhost:8080/v1/diff/{ID} | Get the comparision result for the Given ID. If there is no comparision with given ID an error will be reported |

# Documentation
In order to read the full documentation check the /docs folder on project structure. In this folder you can find:
* Architectural documents with diagrams and decisions to make the application scalable.
* Improvement suggestions


### Extra - Running as a Docker Image
If you dont have Maven or Java installed, but are used to deal with docker images, the following instructions can be used to get running ASAP.
```sh
$ docker pull giulianobortolassi/waes:base64-diff-api
$ docker run -d -e APP_PROFILE=mongodb -p {LOCAL_PORT}:8080 giulianobortolassi/waes:base64-diff-api
```
It will download and start a container with the embedded server listening on {LOCAL_PORT}. Is possible to test the API using a local REST client pointing to http://localhost:{LOCAL_PORT}/. The APP_PROFILE values must be 'mongdb' or 'default'

### Extra - AWS hosted

I hosted one instance of this application on AWS. It can be tested using the following link:

    http://ec2-54-202-207-52.us-west-2.compute.amazonaws.com:8080/v1/diff/{ID}
