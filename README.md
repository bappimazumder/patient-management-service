# Patient Management Service

This repo will contain patient management APIs.


**Prerequisite: **
*  openjdk 17
*  Postgresql 10 or higher
*  git client


**Prepare Database: **

    psql -U postgres -d postgres

    create user pms_user with encrypted password '123';

    create database patient_management_db owner pms_user;

Build the war file using this command

    ./gradlew clean bootWar

The war will found on \build\libs\patient-management-service.war

Finally, For Deployment copy the generated war to /path/to/tomcat10/webapps/     directory as patient-management-service.war


For test please import the postman collection from this location

    resources/postman_collection/Patient-Management-API-Collection.postman_collection.json

Example API Request and Response Format

1. POST Request Example: Create Patient

   HTTP Method: POST

   Endpoint: /api/v1/pms/patient/create

   Description: Creates a new patient with the provided details.

   Request:

   URL: POST /api/v1/pms/patient/create

   Headers:

        Content-Type: application/json
        Authorization: No Auth

   Body:

        {
           "fullName":"Jewel",
           "age":23,
           "gender":"MALE",
           "contactNo":"01816904616",
           "address":"Golden Street, Dhaka",
           "patientType": "NON_EMERGENCY"
        }


  Response: HTTP Status Code: 200    

    {
        "status": "success",
        "message": "Patient created successfully",
        "data": {
            "fullName": "Jewel 3",
            "code": "PAT-1734490059",
            "age": 23,
            "gender": "MALE",
            "contactNo": "01816904616",
            "address": "Golden Street, Dhaka",
            "patientType": "NON_EMERGENCY",
            "activeStatus": true,
            "createDate": 1734490059004
        },
        "metadata": null
    }
  Request:
    Body:

        {
           "fullName":"Jewel",
           "age":23,
           "gender":"MALE",
           "contactNo":"01816904616",
           "address":"Golden Street, Dhaka",
           "patientType": "NON_EMERGENCY"
        }
  Response:
        HTTP Status Code: 400
    
        {
            "status": "failed",
            "message": "Invalid patient gender",
            "data": null,
            "metadata": null
        }

2. PUT Request Example: Update Patient

   HTTP Method: PUT

   Endpoint: /api/v1/pms/patient/update

   Description: Update patient details.

   Request:
   URL: PUT /api/v1/pms/patient/update

   Headers:

        Content-Type: application/json
        Authorization: No Auth
   Body:

        {
           "fullName":"Jewel 2",
           "code":"PAT-1734077244",
           "age":24,
           "gender":"MALE",
           "contactNo":"01816904616",
           "address":"Golden Street, Dhaka",
           "patientType": "NON_EMERGENCY"
        }

   Response:  HTTP Status Code: 200 OK

       {
           "status": "success",
           "message": "Patient updated successfully",
           "data": {
           "fullName": "Jewel 2",
           "code": "PAT-1734077244",
           "age": 24,
           "gender": "MALE",
           "contactNo": "01816904616",
           "address": "Golden Street, Dhaka",
           "patientType": "NON_EMERGENCY",
           "activeStatus": true,
           "createDate": 1734077244559
           },
           "metadata": null
       }

   Body:

        {
           "fullName":"Jewel 2",
           "code":"PAT-1734077244",
           "age":24,
           "gender":"MALE",
           "contactNo":"01816904616",
           "address":"Golden Street, Dhaka",
           "patientType": "NON_EMERGENCY2"
        }

   Response:  HTTP Status Code: 400 Bad Request

       {
           "status": "failed",
           "message": "Invalid patient type",
           "data": null,
           "metadata": null
       }  

   Body:

        {
           "fullName":"Jewel 2",
           "code":"PAT-1734077244",
           "age":24,
           "gender":"MALE",
           "contactNo":"01816904616",
           "address":"Golden Street, Dhaka",
           "patientType": "NON_EMERGENCY2"
        }

   Response:  HTTP Status Code: 400 Bad Request

       {
           "status": "failed",
           "message": "Invalid patient type",
           "data": null,
           "metadata": null
       }

3. GET Request Example: Get Patient Details

   HTTP Method: GET

   Endpoint: /api/v1/pms/patient/get

   Description: Get Patient Details

   Request:
   URL:  GET  /api/v1/pms/patient/get

   Headers:

        Content-Type: none
        Authorization: No Auth

   Request Param:

             "code":"PAT-1734077244",  


   Response: HTTP Status Code: 200 OK

        {
            "status": "success",
            "message": "Patient retrieved successfully",
            "data": {
                "fullName": "Jewel 2",
                "code": "PAT-1734077244",
                "age": 24,
                "gender": "MALE",
                "contactNo": "01816904516",
                "address": "Golden Street, Dhaka",
                "patientType": "NON_EMERGENCY",
                "activeStatus": true,
                "createDate": 1734077244559
            },
            "metadata": null
        }

  Request Param:

        "code":"PAT-17340772467",  
        
        
  Response: HTTP Status Code: 400 Bad Request
        
        {
            "status": "failed",
            "message": "Patient retrieved failed, invalid patient code",
            "data": null,
            "metadata": null
        }


4. GET Request Example: Get All Patient Details

   HTTP Method: GET

   Endpoint: /api/v1/pms/patient/getAll

   Description: Get ALl Patient Details

   Request:
   URL:  GET  /api/v1/pms/patient/getAll

   Headers:

        Content-Type: none
        Authorization: No Auth

   Request Param:

             "page":1,  
             "size":3,


Response: HTTP Status Code: 200 OK

        {
            "status": "success",
            "message": "Patient retrieved successfully",
            "data": [
                {
                    "fullName": "Karim",
                    "code": "PAT-1734490059",
                    "age": 23,
                    "gender": "MALE",
                    "contactNo": "01816504616",
                    "address": "Golden Street, Dhaka",
                    "patientType": "NON_EMERGENCY",
                    "activeStatus": true,
                    "createDate": 1734490059004
                },
                {
                    "fullName": "Rahim",
                    "code": "PAT-1734161793",
                    "age": 25,
                    "gender": "MALE",
                    "contactNo": "01817904619",
                    "address": "Golden Street, Dhaka",
                    "patientType": "NON_EMERGENCY",
                    "activeStatus": true,
                    "createDate": 1734161793386
                }                
            ],
            "metadata": {
                "totalItems": 4,
                "totalPages": 2,
                "currentPage": 0
            }
        }

## Steps to Dockerized an Application

Ensure that the database properties in your application.yml file match those in the docker-compose.yml file

Run the Application with Docker Compose

    docker-compose up --build

Stop the Containers

    docker-compose down
 