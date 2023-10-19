# Real-EstatePRO
### Real Estate App is a comprehensive application designed to streamline the operations of a real estate agency. Leveraging a RESTful API, this application offers a range of features and functionalities to facilitate real estate-related tasks. It is secured using Spring Security with JWT token generation to ensure data protection and user authentication. The application allows for registration and login, addition of various types of properties, creation of offers, and reservations.The application features a simple mortgage calculator that calculates the estimated creditworthiness. It utilizes basic HTTP request methods such as POST, GET, PUT, PATCH, and DELETE.

## Run Locally

Clone the project to Your local repository.

Remember to create a database schema locally and configure the 'application.yml' file before running it. You should provide your own credentials in that file.

```bash
  git clone https://github.com/maciej-jankowskii/real-estate
```

Download Postman from the official website. It is a tool that will allow you to test my application's endpoints.


```bash
  https://www.postman.com/
```

Enjoy !


## API Reference

#### Before You start using the application, register a new user.

```http
  POST /api/auth/register
```
You can use my example data:
```
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@realestate.com",
  "password": "password"
}
```
Now you need to log in using the previous credentials. Send a GET request to the specified address.
```http
  GET /api/auth/login
```
Your data:
```
{
  "email": "john@realestate.com",
  "password": "password"
}
```
The application will generate a token for you. Remember to include it with every request. You need to paste it in the 'Authorization' section -> 'Bearer Token'.
<img src="https://github.com/maciej-jankowskii/real-estate/blob/348bb075d3da77630028c51f4aec823c070b6861/token.png" alt="project-screenshot" width="800" height="400/">

Now you can enjoy and fully utilize the application. I've prepared some test real estate data for you.

To add an apartment property:
```http
  POST /apartment
```
Please remember that both the Apartment entity and any other property inherit from the Property entity. To save a property, the request must also include fields from the Property entity. Example below:

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
  GET/apartment/{id}
```
You can find all apartments
```http
  GET/apartment/getAll
```
You can filter properties, meaning you can search for them based on specified criteria. Just send the request in POSTMAN application, example:
```http
  GET/apartment/filtered?maxPrice=2000000&balcony=true
```

You can update a specific object. Remember to include the fields you want to edit in the request body
```http
  PATCH/apartment/{id}
```
You can also delete apartment by ID number
```http
  DELETE/apartment/{id}
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
  POST/offer
```
Similarly to previous cases, you can retrieve an individual resource as well as all of them.
Just change the request type and the path.

When creating an offer, you assign it to a employee and a customer (the property owner). Therefore, you can retrieve offers for a customer.
Just send request:
```http
  GET/offer/client/{id}
```

Editing and deleting work the same way as in previous cases.

You can simulate the sale of an offer by simply sending a request, and the application will mark the offer as sold
```http
  PATCH/offer/sell/{id}
```
You can also filter properties. The application allows you to retrieve all current or all inactive offers.
```http
  GET/offer/available
```
or
```http
  GET/offer/sold
```
Very similar capabilities are offered by one of the latest features, which is property reservation.

As a final option, I'd like to show you a small addition: a mortgage affordability calculator that calculates the estimated mortgage affordability.

Just send request:
```http
  POST/credit/calculate
```

Please remember to include the necessary data, an example is provided below:

```
{
    "monthlyIncome": 8000,
    "monthlyExpenses": 2500,
    "interestRate": 0.06,
    "loanTerm": 20,
    "downPayment": 80000
}
```



###
#### THANK YOU!
###
