# Contact API Spec

## Create

Endpoint: POST /api/contacts

Request Header:

- X-TOKEN-API: Token (Mandatory)

Request Body:

```json
{
  "firstname": "Muhamad",
  "lastname": "Ikroman",
  "email": "ikroman9a@gmail.com",
  "phone": "0808080080"
}
```

Response Body (Success):

```json
{
  "data": {
    "id": "random string",
    "firstname": "Muhamad",
    "lastname": "Ikroman",
    "email": "ikroman9a@gmail.com",
    "phone": "0808080080"
  }
}
```

Response Body (Failed):

```json
{
  "errors": "Failed Create Contact"
}
```

## Update

Endpoint: PUT /api/contacts/{idContact}

Request Header:

- X-TOKEN-API: Token (Mandatory)

Request Body:

```json
{
  "firstname": "Muhamad",
  "lastname": "Ikroman",
  "email": "ikroman9a@gmail.com",
  "phone": "0808080080"
}
```

Response Body (Success):

```json
{
  "data": {
    "id": "random string",
    "firstname": "Muhamad",
    "lastname": "Ikroman",
    "email": "ikroman9a@gmail.com",
    "phone": "0808080080"
  }
}
```

Response Body (Failed):

```json
{
  "errors": "Failed Create Contact"
}
```

## Get

Endpoint: GET /api/contacts/{idContact}

Request Header:

- X-TOKEN-API: Token (Mandatory)

Response Body (Success):

```json
{
  "data": {
    "id": "random string",
    "firstname": "Muhamad",
    "lastname": "Ikroman",
    "email": "ikroman9a@gmail.com",
    "phone": "0808080080"
  }
}
```

Response Body (Failed, 404):

```json
{
  "errors": "Failed Create Contact"
}
```

## Search

Endpoint: GET /api/contacts

Query Param:
- name: String, firstname or lastname (Optional)
- email: String, email (Optional)
- phone: String, phone (Optional)
- page: Integer, start from 0, default 0
- size: Integer, default 10

Request Header:

- X-TOKEN-API: Token (Mandatory)

Response Body (Success):

```json
{
  "data": [
    {
      "id": "random string",
      "firstname": "Muhamad",
      "lastname": "Ikroman",
      "email": "ikroman9a@gmail.com",
      "phone": "0808080080"
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
  }
}
```

Response Body (Failed):

```json
{
  "data": "contact is not found"
}
```

## Remove

Endpoint: DELETE /api/contacts/{idContact}

Request Header:

- X-TOKEN-API: Token (Mandatory)

Response Body (Success):

```json
{
  "data": "OK"
}
```

Response Body (Failed):

```json
{
  "data": "contact is not found"
}
```