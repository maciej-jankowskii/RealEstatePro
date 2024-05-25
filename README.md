# Estateese

### This is my first application written after about six months of learning Java and Spring. I am currently creating the graphical interface using the React library. This is an application where I used my professional experience since I worked for about 2 years as a real estate agent. It is a simple CRUD application, secured with Spring Security. I used Liquibase to track changes in the database. The application allows users to register, log in to their accounts, register employees, add various types of properties, edit and delete them, and create offers based on them. I used basic HTTP requests such as POST, GET, PUT, and DELETE.

## 1. If You want, here You will find the front-end code with short demo on YouTube:
<p><a href="https://github.com/maciej-jankowskii/estateese-frontend">Click here -> Estateese</a></p>


## 2.Run Locally and test using Postmann

Clone the project to Your local repository.


```bash
  git clone https://github.com/maciej-jankowskii/estateese-backend
```

The Dockerfile and docker-compose file have already been prepared, so just use the command below to run the application

```bash
  docker compose up
```

Download Postman from the official website. It is a tool that will allow you to test my application's endpoints.

```bash
  https://www.postman.com/
```

## 3. Or use Swagger: 
<p><a href="http://localhost:8080/swagger-ui/index.html#/">Click here -> Swagger</a></p>

However, please remember that also in this case you must first log in and use the generated token in the Authorize section.

Login data: 

```
{
  "email": "john@realestate.com",
  "password": "hard"
}
```

Send a POST request to the specified address.
```http
  POST /api/auth/login
```


To add an apartment property:

```http
  POST /api/apartments
```
Example data below:

```
{
    "address": "123 Main Street",
    "price": 250000.00,
    "description": "Description.",
    "area": 120.5,
    "rooms": 3,
    "bathrooms": 2,
    "duplexApartment": false,
    "buildingType": "SKYSCRAPER",
    "floor": 5,
    "elevator": true,
    "balcony": true,
    "garage": false,
    "yearOfConstruction": 2010,
    "standard": "HIGHT_STANDARD"
}
```

You will find all the necessary data for creating this and other properties in the DTO directory.

You can also find an apartment by ID number.

```http
  GET/api/apartments/{id}
```
You can find all apartments
```http
  GET/api/apartments
```

You can update a specific object. Remember to include the fields you want to edit in the request body
```http
  PUT/api/apartments/update-apartment/{id}
```
You can also delete apartment by ID number
```http
  DELETE/api/apartments/delete-apartment/{id}
```
You can send the exact same requests for other types of properties, such as:
#### Commercial Properties, Houses, and Lands.


_____
###
You can also create customers. In my application, a customer is a property owner. You will need customers in the next step to create offers. For this entity, you can create, edit, retrieve, and delete resources, just like before.
Just use other path. 
_____
Let's discuss offers now. You can create them using the path:
```http
  POST/api/offers
```
Similarly to previous cases, you can retrieve an individual resource as well as all of them.
Just change the request type and the path.

When creating an offer, you assign it to a employee and a customer (the property owner). Therefore, you can retrieve offers for a customer.
Just send request:
```http
  GET/api/offers/client/{id}
```

Editing and deleting work the same way as in previous cases.

You can simulate the sale of an offer by simply sending a request, and the application will mark the offer as sold
```http
  PATCH/api/offers/sold/{id}
```


###
#### THANK YOU!
###







