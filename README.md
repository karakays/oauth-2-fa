
# Two-factor authentication with [OAuth 2.0](https://tools.ietf.org/html/rfc6749)

## About

A demonstration of how one-time passwords can be used with OAuth 2. 

## Requirements

JRE 1.8, maven and an otp authenticator

## Installation

```
$ git clone https://github.com/karakays/oauth-2-fa.git
$ cd oauth-2-fa/ && mvn clean package
$ java -jar target/app-0.0.1-SNAPSHOT.jar
```

## Getting started

* You can use any authenticator client to generate OTP codes, e.g. [otp-py](https://github.com/karakays/otp-py)

* Sample users can be found in `/src/main/resources/data.sql`.

* Sample endpoints 

    Public endpoints don't require authentication.

    * `/check-health`

    Secure endpoints cannot be accessed without being authenticated.

    * `/app/random`
	* `/app/time`


## Usage

```
$ curl http://localhost:8000/check-health

< HTTP/1.1 200
```

Unauthenticated requests on secure endpoints result with 401 error as shown below.

```
$ curl http:localhost:8000/app/random

< HTTP/1.1 401
{"error":"unauthorized","error_description":"Full authentication is required to access this resource"}
```

To authenticate, password credentials need to be provided. 

```
$ curl -u app:app -X POST -d "username=johnd&password=jwtpass" http://localhost:8000/tokens?grant_type=password

{"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGhpcy1pcy1hLXJlc291cmNlLWlkIl0sImV4cCI6MTUyNzQ3OTI5NSwidXNlcl9uYW1lIjoiam9obmQiLCJqdGkiOiJjNzI1OGNmYS1kMTNiLTRkZmMtOTYzMS04NDBlMjE0ZThmOWQiLCJjbGllbnRfaWQiOiJhcHAiLCJzY29wZSI6WyJ0aGlzLWlzLXJlYWQtc2NvcGUiLCJ0aGlzLWlzLXdyaXRlLXNjb3BlIiwiY3VzdG9tLXNjZW9wLTEiXX0.i4itJzHwGONkksqXfIN73F9d-a_9opGAvywPQJWG_iY","token_type":"bearer","refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGhpcy1pcy1hLXJlc291cmNlLWlkIl0sInVzZXJfbmFtZSI6ImpvaG5kIiwic2NvcGUiOlsidGhpcy1pcy1yZWFkLXNjb3BlIiwidGhpcy1pcy13cml0ZS1zY29wZSIsImN1c3RvbS1zY2VvcC0xIl0sImF0aSI6ImM3MjU4Y2ZhLWQxM2ItNGRmYy05NjMxLTg0MGUyMTRlOGY5ZCIsImV4cCI6MTUzMDAyODA5NSwianRpIjoiZDA0N2JjYjItZjFkZi00Yzc2LTljZDktMzY4MjQ1MTc3MDVhIiwiY2xpZW50X2lkIjoiYXBwIn0.5Edn_jZTSjHxNgC_1W5DX5cy0TSSpodEsO6skoGTZyQ","expires_in":43199,"scope":"this-is-read-scope this-is-write-scope custom-sceop-1","jti":"c7258cfa-d13b-4dfc-9631-840e214e8f9d"}
```

If a user has two-factor authentication enabled, it needs to provide OTP token as well - observe

```
$ curl -u app:app -X POST -d "username=admin&password=jwtpass" http://localhost:8000/tokens?grant_type=password

< HTTP/1.1 400
{"error":"invalid_grant","error_description":"OTP code is mandatory"}
```

```
$ curl -u app:app -X POST -d "username=admin&password=jwtpass&otp=123456" http://localhost:8000/tokens?grant_type=password

< HTTP/1.1 400
{"error":"invalid_grant","error_description":"Invalid OTP code"}
```

```
$ curl -u app:app -X POST -d "username=admin&password=jwtpass&otp=007629" http://localhost:8000/tokens?grant_type=password

{"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGhpcy1pcy1hLXJlc291cmNlLWlkIl0sImV4cCI6MTUyNzQ3OTI5NSwidXNlcl9uYW1lIjoiam9obmQiLCJqdGkiOiJjNzI1OGNmYS1kMTNiLTRkZmMtOTYzMS04NDBlMjE0ZThmOWQiLCJjbGllbnRfaWQiOiJhcHAiLCJzY29wZSI6WyJ0aGlzLWlzLXJlYWQtc2NvcGUiLCJ0aGlzLWlzLXdyaXRlLXNjb3BlIiwiY3VzdG9tLXNjZW9wLTEiXX0.i4itJzHwGONkksqXfIN73F9d-a_9opGAvywPQJWG_iY","token_type":"bearer","refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGhpcy1pcy1hLXJlc291cmNlLWlkIl0sInVzZXJfbmFtZSI6ImpvaG5kIiwic2NvcGUiOlsidGhpcy1pcy1yZWFkLXNjb3BlIiwidGhpcy1pcy13cml0ZS1zY29wZSIsImN1c3RvbS1zY2VvcC0xIl0sImF0aSI6ImM3MjU4Y2ZhLWQxM2ItNGRmYy05NjMxLTg0MGUyMTRlOGY5ZCIsImV4cCI6MTUzMDAyODA5NSwianRpIjoiZDA0N2JjYjItZjFkZi00Yzc2LTljZDktMzY4MjQ1MTc3MDVhIiwiY2xpZW50X2lkIjoiYXBwIn0.5Edn_jZTSjHxNgC_1W5DX5cy0TSSpodEsO6skoGTZyQ","expires_in":43199,"scope":"this-is-read-scope this-is-write-scope custom-sceop-1","jti":"c7258cfa-d13b-4dfc-9631-840e214e8f9d"}
```

## License

Project is under [MIT license](https://opensource.org/licenses/MIT)
