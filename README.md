# Mayhem for API GitHub Integration demo

Mayhem for API called part of the build using GitHub Actions. We have
forked the [swagger-petstore](https://github.com/swagger-api/swagger-petstore) repository
which is used to showcase a REST API that implements the OpenAPI 3 Spec.

As part of the build, a swagger-petstore stand-alone server is started so that the `mapi`
CLI fuzzer can fuzz the target running on localhost using the OpenAPI specification located in 
`/src/main/resources/openapi.yaml`.

With the help of the Mayhem for API GitHub App, builds will automatically be updated
with the status of an API Fuzzing job as it is run.

For more details please visit us at https://mayhem4api.forallsecure.com.

# Swagger Petstore Sample

## Overview
This is the pet store sample hosted at https://petstore3.swagger.io. For other versions, check the branches.
We welcome suggestion both the code and the API design.
To make changes to the design itself, take a look at https://github.com/swagger-api/swagger-petstore/blob/master/src/main/resources/openapi.yaml.

This is a java project to build a stand-alone server which implements the OpenAPI 3 Spec.  You can find out
more about both the spec and the framework at http://swagger.io.

This sample is based on [swagger-inflector](https://github.com/swagger-api/swagger-inflector), and provides an example of swagger / OpenAPI 3 petstore.

### To run (with Maven)
To run the server, run this task:

```
mvn package jetty:run
```

This will start Jetty embedded on port 8080.

### To run (via Docker)

Expose port 8080 from the image and access petstore via the exposed port. You can then add and delete pets as you see fit.


*Example*:

```
docker build -t swaggerapi/petstore3:unstable .
```

```
docker pull swaggerapi/petstore3:unstable
docker run  --name swaggerapi-petstore3 -d -p 8080:8080 swaggerapi/petstore3:unstable
```


### Testing the server
Once started, you can navigate to http://localhost:8080/api/v3/openapi.json to view the Swagger Resource Listing.
This tells you that the server is up and ready to demonstrate Swagger.

### Using the UI
There is an HTML5-based API tool bundled in this sample--you can view it it at [http://localhost:8080](http://localhost:8080). This lets you inspect the API using an interactive UI.  You can access the source of this code from [here](https://github.com/swagger-api/swagger-ui)
