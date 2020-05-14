###### auth-ms-spring

# Running Auth App on CP4Apps

*This project is part of the 'IBM Cloud Native Reference Architecture' suite, available at
https://github.com/ibm-garage-ref-storefront/refarch-cloudnative-storefront*

## Table of Contents

* [Introduction](#introduction)
    + [APIs](#apis)
* [Pre-requisites](#pre-requisites)
* [Auth application on CP4Apps](#auth-application-on-cp4apps)
    + [Get the Auth application](#get-the-auth-application)
    + [Application manifest](#application-manifest)
    + [Project Setup](#project-setup)
    + [Deploy the app using Kabanero Pipelines](#deploy-the-app-using-kabanero-pipelines)
      * [Access tekton dashboard](#access-tekton-dashboard)
      * [Create registry secrets](#create-registry-secrets)
      * [Create Webhook for the app repo](#create-webhook-for-the-app-repo)
      * [Deploy the app](#deploy-the-app)
* [Conclusion](#conclusion)

## Introduction

This project demonstrates how to authenticate the API user as well as enable OAuth 2.0 authorization for all OAuth protected APIs in the [Storefront](https://github.com/ibm-garage-ref-storefront/refarch-cloudnative-storefront) reference application.

![Application Architecture](static/auth.png?raw=true)

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

* [RedHat Openshift Cluster](https://cloud.ibm.com/kubernetes/catalog/openshiftcluster)

* IBM Cloud Pak for Applications
  + [Using IBM Console](https://cloud.ibm.com/catalog/content/ibm-cp-applications)
  + [OCP4 CLI installer](https://www.ibm.com/support/knowledgecenter/en/SSCSJL_4.1.x/install-icpa-cli.html)

* Docker Desktop
  + [Docker for Mac](https://docs.docker.com/docker-for-mac/)
  + [Docker for Windows](https://docs.docker.com/docker-for-windows/)

* Command line (CLI) tools
  + [oc](https://www.okd.io/download.html)
  + [git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
  + [appsody](https://appsody.dev/docs/getting-started/installation)

* Deploy Customer Microservice - Refer the instructions [here](https://github.com/ibm-garage-ref-storefront/customer-ms-spring/blob/master/docs/CP4APPS.md). Auth microservice is dependent on the Customer microservice. It gets the customer information to validate the credentials.

## Auth application on CP4Apps

### Get the Auth application

- Clone auth repository:

```bash
git clone https://github.com/ibm-garage-ref-storefront/auth-ms-spring.git
cd auth-ms-spring
```

### Application manifest

When you see the project structure, you should be able to find an `app-deploy.yaml`. This is generated as follows.

```
appsody deploy --generate-only
```

This generates a default `app-deploy.yaml` and on top of this we added necessary configurations that are required by the Catalog application.

### Project Setup

- Create a new project if it does not exist. Or if you have an existing project, skip this step.

```
oc new-project storefront
```

- Once the namespace is created, we need to add it as a target namespace to Kabanero.

Verify if kabanero is present as follows.

```
$ oc get kabaneros -n kabanero
NAME       AGE   VERSION   READY
kabanero   9d    0.6.1     True
```

- Edit the yaml file configuring kabanero as follows.

```
$ oc edit kabanero kabanero -n kabanero
```

- Finally, navigate to the spec label within the file and add the following targetNamespaces label.

```
spec:
  targetNamespaces:
    - storefront
```

### Deploy the app using Kabanero Pipelines

#### Access tekton dashboard

- Open IBM Cloud Pak for Applications and click on `Instance` section. Then select `Manage Pipelines`.

![CP4Apps](static/cp4apps_pipeline.png?raw=true)

- This will open up the Tekton dashboard.

![Tekton dashboard](static/tekton.png?raw=true)

#### Create registry secrets

- To create a secret, in the menu select `Secrets` > `Create` as below.

![Secret](static/secret.png?raw=true)

Provide the below information.

```
Name - <Name for secret>
Namespace - <Your pipeline namespace>
Access To - Docker registry>
username - <registry user name>
Password/Token - <registry password or token>
Service account - kabanero-pipeline
Server Url - Keep the default one
```

- You will see a secret like this once created.

![Docker registry secret](static/docker_registry_secret.png?raw=true)

#### Create Webhook for the app repo

- For the Github repo, create the webhook as follows. To create a webhook, in the menu select `Webhooks` > `Create webhook`

We will have below

![Webhook](static/webhook.png?raw=true)

Provide the below information.

```
Name - <Name for webhook>
Repository URL - <Your github repository URL>
Access Token - <For this, you need to create a Github access token with permission `admin:repo_hook` or select one from the list>
```

To know more about how to create a personal access token, refer [this](https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line).

- Now, enter the pipeline details.

![Pipeline Info](static/pipeline_info.png?raw=true)

- Once you click create, the webhook will be generated.

![Auth Webhook](static/webhook_auth.png?raw=true)

- You can also see in the app repo as follows.

![Auth Repo Webhook](static/webhook_auth_repo.png?raw=true)

#### Deploy the app

Whenever we make changes to the repo, a pipeline run will be triggered and the app will be deployed to the openshift cluster.

- To verify if it is deployed, run below command.

```
oc get pods
```

If it is successful, you will see something like below.

```
$ oc get pods
NAME                                   READY   STATUS    RESTARTS   AGE
auth-ms-spring-57b7f9cb94-q69cz        1/1     Running   0          50s
```

- You can access the app as below.

```
oc get route
```

This will return you something like below.

```
$ oc get route
NAME                  HOST/PORT                                                                                                                      PATH   SERVICES              PORT       TERMINATION   WILDCARD
auth-ms-spring        auth-ms-spring-storefront.csantana-demos-ocp43-fa9ee67c9ab6a7791435450358e564cc-0000.us-east.containers.appdomain.cloud               auth-ms-spring        8080-tcp                 None
```

- Validate the application as follows.

```
curl -i \
   -X POST \
   -u bluecomputeweb:bluecomputewebs3cret \
   http://auth-ms-spring-storefront.csantana-demos-ocp43-fa9ee67c9ab6a7791435450358e564cc-0000.us-east.containers.appdomain.cloud/oauth/token?grant_type=password\&username=user\&password=password\&scope=blue
```

If it is successful, you will see something like below.

```
$ curl -i \
>    -X POST \
>    -u bluecomputeweb:bluecomputewebs3cret \
>    http://auth-ms-spring-storefront.csantana-demos-ocp43-fa9ee67c9ab6a7791435450358e564cc-0000.us-east.containers.appdomain.cloud/oauth/token?grant_type=password\&username=user\&password=password\&scope=blue
HTTP/1.1 200
Pragma: no-cache
Cache-Control: no-store
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 14 May 2020 10:28:07 GMT
Set-Cookie: 30636e55b840a5dc7c81922509ee9c9b=f884dd75143d446d66eb2b7d770e0b97; path=/; HttpOnly

{"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODk0OTUyODcsInVzZXJfbmFtZSI6InVzZXIiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiZjcwMDc4NDctYWM1ZC00M2U3LTlmMDctMDViZjBjNDMzM2Q4IiwiY2xpZW50X2lkIjoiYmx1ZWNvbXB1dGV3ZWIiLCJzY29wZSI6WyJibHVlIl19.ehC1zlRIGCq-9XfetnchPplBu8DOkapqezVaSXT3Kjo","token_type":"bearer","refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VyIiwic2NvcGUiOlsiYmx1ZSJdLCJhdGkiOiJmNzAwNzg0Ny1hYzVkLTQzZTctOWYwNy0wNWJmMGM0MzMzZDgiLCJleHAiOjE1OTIwNDQwODcsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI2ZjljMjBlMy1lZWZmLTQ5ZjktOWU3Ny1mNTMwY2UxOWU1NGMiLCJjbGllbnRfaWQiOiJibHVlY29tcHV0ZXdlYiJ9.mk6LxYJCZUPYxXuL1A3ZnsfllN7ICqS3pLs1B5tHo-4","expires_in":43199,"scope":"blue","jti":"f7007847-ac5d-43e7-9f07-05bf0c4333d8"}
```

## Conclusion

You have successfully deployed and tested the Auth Microservice on Openshift using IBM Cloud Paks for Apps.

To see the Auth application working in a more complex microservices use case, checkout our Microservice Reference Architecture Application [here](https://github.com/ibm-garage-ref-storefront/refarch-cloudnative-storefront).
