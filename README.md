###### auth-ms-spring

# Secure REST API with OAuth2 and Authorization Service

*This project is part of the 'IBM Cloud Native Reference Architecture' suite, available at
https://github.com/ibm-garage-ref-storefront/refarch-cloudnative-storefront*

## Table of Contents

* [Introduction](#introduction)
    + [APIs](#apis)
* [Pre-requisites](#pre-requisites)
* [Implementation Details](#implementation-details)
* [Running the application on Docker](#running-the-application-on-docker)
    + [Get the Auth application](#get-the-auth-application)
    + [Run the Auth application](#run-the-auth-application)
    + [Validating the application](#validating-the-application)
    + [Exiting the application](#exiting-the-application)
* [Conclusion](#conclusion)

## Introduction

This project demonstrates how to authenticate the API user as well as enable OAuth 2.0 authorization for all OAuth protected APIs in the [Storefront](https://github.com/ibm-garage-ref-storefront/refarch-cloudnative-storefront) reference application.

#### Interaction with Identity Provider (Auth Microservice)

![Application Architecture](static/auth.png?raw=true)

#### Interaction with Resource Server API

![Resource Server](static/auth_orders.png?raw=true)

- When a client wishes to acquire an OAuth token to call a protected API, it calls the OAuth Provider (Authorization microservice) token endpoint with the `username/password` of the user and requests a token with scope `blue`.
- Authorization microservice will call the [Customer](https://github.com/ibm-garage-ref-storefront/customer-ms-spring) microservice to get the credentials and perform the validation.
- If the `username/password` are valid, `HTTP 200` is returned, along with a JWT (signed using a HS256 shared secret) in the JSON response under `access_token` which contains the auth ID of the user passed in the `user_name` claim.
- The client uses the JWT in the `Authorization` header as a bearer token to call other Resource Servers that have OAuth protected API (such as the [Orders microservice](https://github.com/ibm-garage-ref-storefront/orders-ms-spring)).
- The service implementing the REST API verifies that the JWT is valid and signed using the shared secret, then extracts the `user_name` claim from the JWT to identify the caller.
- The JWT is encoded with scope `blue` and the the expiry time in `exp`; once the token is generated, there is no additional interaction between the Resource Server and the OAuth server.

Here is an overview of the project's features:

- Leverage [`Spring Boot`](https://projects.spring.io/spring-boot/) framework to build a Microservices application.
- Uses [`Spring Security OAuth`](https://spring.io/projects/spring-security-oauth).
- Return a signed [JWT](https://jwt.io/) Bearer token back to caller for identity propagation and authorization
- Uses [`Docker`](https://docs.docker.com/) to package application binary and its dependencies.

### APIs

Following the [OAuth 2.0 specification](https://tools.ietf.org/html/rfc6749), the Authorization server exposes both an authorization URI and a token URI.

- GET `/oauth/authorize`
- POST `/oauth/token`

The Storefront reference application supports the following clients and grant types:

- The [Storefront UI](https://github.com/ibm-garage-ref-storefront/storefront-ui) is using client ID `bluecomputeweb` and client secret `bluecomputewebs3cret` supporting OAuth 2.0 Password grant type.

The BlueCompute application has scope, `blue`.

## Pre-requisites:

* [Appsody](https://appsody.dev/)
    + [Installing on MacOS](https://appsody.dev/docs/installing/macos)
    + [Installing on Windows](https://appsody.dev/docs/installing/windows)
    + [Installing on RHEL](https://appsody.dev/docs/installing/rhel)
    + [Installing on Ubuntu](https://appsody.dev/docs/installing/ubuntu)
For more details on installation, check [this](https://appsody.dev/docs/installing/installing-appsody/) out.

* Docker Desktop
    + [Docker for Mac](https://docs.docker.com/docker-for-mac/)
    + [Docker for Windows](https://docs.docker.com/docker-for-windows/)

## Implementation Details

We created a new spring boot project using appsody as follows.

```
appsody repo add kabanero https://github.com/kabanero-io/kabanero-stack-hub/releases/download/0.6.5/kabanero-stack-hub-index.yaml

appsody init kabanero/java-spring-boot2
```

And then we defined the necessary code for the application on top on this template.

## Running the application on Docker

### Get the Auth application

- Clone auth repository:

```bash
git clone https://github.com/ibm-garage-ref-storefront/auth-ms-spring.git
cd auth-ms-spring
```

### Run the Auth application

- Before running the application, make sure you grab the `HS256` shared secret.

To make things easier for you, we pasted below the 2048-bit secret here.

```
E6526VJkKYhyTFRFMC0pTECpHcZ7TGcq8pKsVVgz9KtESVpheEO284qKzfzg8HpWNBPeHOxNGlyudUHi6i8tFQJXC8PiI48RUpMh23vPDLGD35pCM0417gf58z5xlmRNii56fwRCmIhhV7hDsm3KO2jRv4EBVz7HrYbzFeqI45CaStkMYNipzSm2duuer7zRdMjEKIdqsby0JfpQpykHmC5L6hxkX0BT7XWqztTr6xHCwqst26O0g8r7bXSYjp4a
```

- To run the auth application, use the below command.

```
appsody run --docker-options "-e HS256_KEY=<Paste HS256 key here>"
```

- If it is successfully running, you will see something like below.

```
[Container] 2020-05-05 07:18:23.995  INFO 178 --- [  restartedMain] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
[Container] 2020-05-05 07:18:24.055  INFO 178 --- [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
[Container] 2020-05-05 07:18:24.108  INFO 178 --- [  restartedMain] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page: class path resource [public/index.html]
[Container] 2020-05-05 07:18:24.634  INFO 178 --- [  restartedMain] d.s.w.p.DocumentationPluginsBootstrapper : Context refreshed
[Container] 2020-05-05 07:18:24.680  INFO 178 --- [  restartedMain] d.s.w.p.DocumentationPluginsBootstrapper : Found 1 custom documentation plugin(s)
[Container] 2020-05-05 07:18:24.751  INFO 178 --- [  restartedMain] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
[Container] 2020-05-05 07:18:24.926  INFO 178 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
[Container] 2020-05-05 07:18:24.934  INFO 178 --- [  restartedMain] application.Main                         : Started Main in 10.296 seconds (JVM running for 12.632)
```

- You can also verify it as follows.

```
$ docker ps
CONTAINER ID        IMAGE                                COMMAND                  CREATED             STATUS              PORTS                                                                                              NAMES
cc1cc3d65ff7        kabanero/java-spring-boot2:0.3       "/.appsody/appsody-c…"   2 minutes ago       Up 2 minutes        0.0.0.0:5005->5005/tcp, 0.0.0.0:8080->8080/tcp, 0.0.0.0:8443->8443/tcp, 0.0.0.0:35729->35729/tcp   auth-ms-spring
```

### Validating the application

Now, you can validate the application as follows.

```
curl -i \
   -X POST \
   -u bluecomputeweb:bluecomputewebs3cret \
   http://localhost:8080/oauth/token?grant_type=password\&username=user\&password=password\&scope=blue
```

If it is successful, you will see something like below.

```
$ curl -i    -X POST    -u bluecomputeweb:bluecomputewebs3cret    http://localhost:8080/oauth/token?grant_type=password\&username=user\&password=password\&scope=blue
HTTP/1.1 200
Cache-Control: no-store
Pragma: no-cache
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Tue, 05 May 2020 07:23:15 GMT

{"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg3MDY1OTUsInVzZXJfbmFtZSI6InVzZXIiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiMTBmMmExNGMtOWZmNi00OWI5LWE4NzUtNGVjYzAwNjZkNmY0IiwiY2xpZW50X2lkIjoiYmx1ZWNvbXB1dGV3ZWIiLCJzY29wZSI6WyJibHVlIl19.2vzcyYbm6lYUv0iNl9B15uCBngIbcIsGs2ulC98sAds","token_type":"bearer","refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VyIiwic2NvcGUiOlsiYmx1ZSJdLCJhdGkiOiIxMGYyYTE0Yy05ZmY2LTQ5YjktYTg3NS00ZWNjMDA2NmQ2ZjQiLCJleHAiOjE1OTEyNTUzOTUsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI0MmMyZjFhZS05NTAyLTQ3M2ItOTAwMy1iODc5NjE0MDFlMGMiLCJjbGllbnRfaWQiOiJibHVlY29tcHV0ZXdlYiJ9.P6eOlzoBk8-d0KKK_7725U0wICosO1G0SYPv8r62-uM","expires_in":43199,"scope":"blue","jti":"10f2a14c-9ff6-49b9-a875-4ecc0066d6f4"}
```

Originally, in the [storefront](https://github.com/ibm-garage-ref-storefront/refarch-cloudnative-storefront) application, this microservice will talk to [customer](https://github.com/ibm-garage-ref-storefront/customer-ms-spring) microservice and validate the credentials. To make it easy for local validation, we enabled a test user which can be used to verify the functionality of this application.

- Also you can access the swagger ui at http://localhost:8080/swagger-ui.html

![Auth Swagger UI](static/swagger_auth.png?raw=true)

### Exiting the application

To exit the application, just press `Ctrl+C`.

It shows you something like below.

```
^CRunning command: docker stop auth-ms-spring
[Container] 2020-05-05 07:31:39.015  INFO 178 --- [      Thread-16] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
[Container] [INFO] ------------------------------------------------------------------------
[Container] [INFO] BUILD SUCCESS
[Container] [INFO] ------------------------------------------------------------------------
[Container] [INFO] Total time:  13:47 min
[Container] [INFO] Finished at: 2020-05-05T07:31:39Z
[Container] [INFO] ------------------------------------------------------------------------
Closing down development environment.
```

## Conclusion

You have successfully deployed and tested the Auth Microservice in local Docker Containers using Appsody.

To see the Auth application working in a more complex microservices use case, checkout our Microservice Reference Architecture Application [here](https://github.com/ibm-garage-ref-storefront/refarch-cloudnative-storefront).
