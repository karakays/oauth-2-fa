Two-factor authentication with OAuth 2.0
========================

[![Build Status](https://travis-ci.org/karakays/oauth-2-fa.svg?branch=master)](https://travis-ci.org/karakays/oauth-2-fa?branch=master) 
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


## About

A demonstration of how one-time passwords can be used with OAuth 2 password grant type. 

## Requirements

JRE 1.8, maven and an otp authenticator

## Installation

```
$ git clone https://github.com/karakays/oauth-2-fa.git
$ cd oauth-2-fa/ && mvn package
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
{
  "error": "unauthorized",
  "error_description": "Full authentication is required to access this resource"
}
```

To authenticate, password credentials need to be provided. 

```
$ curl -u app:app -X POST -d "username=johnd&password=jwtpass"\   
http://localhost:8000/tokens?grant_type=password

{
  "access_token":  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsieS1yZXNvdXJjZSJdLCJleHAiOjE1MjgzMTIxMTEsInVzZXJfbmFtZSI6ImpvaG5kIiwianRpIjoiMTdhYTMxZjktNTMxYS00NjJiLWE2NTctY2YwZWFlMzNjYzZmIiwiY2xpZW50X2lkIjoieWFwcCIsInNjb3BlIjpbInJlYWQiXX0.Y-onGiYPMYnhhnn2NmXLnu4aC7swoDxeUv6OZLr8M6I",
  "token_type": "bearer",  
  "refresh_token":   "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsieS1yZXNvdXJjZSJdLCJ1c2VyX25hbWUiOiJqb2huZCIsInNjb3BlIjpbInJlYWQiXSwiYXRpIjoiMTdhYTMxZjktNTMxYS00NjJiLWE2NTctY2YwZWFlMzNjYzZmIiwiZXhwIjoxNTMwODkzMzExLCJqdGkiOiJjMzM2ZDU0ZS03YjQxLTQzOGItODE1Zi1kNjIyYmZlZjI1NTAiLCJjbGllbnRfaWQiOiJ5YXBwIn0.lx0S6AYqbaEdkUf7oaG6hQ1cRDAa6YBig-wD71djUC8",   
  "expires_in": 10799,   
  "scope": "read",   
  "jti": "17aa31f9-531a-462b-a657-cf0eae33cc6f"
}
```

If a user has two-factor authentication enabled, it needs to provide OTP token as well - observe

```
$ curl -u app:app -X POST -d "username=admin&password=jwtpass"\  
http://localhost:8000/tokens?grant_type=password

< HTTP/1.1 400
{
  "error": "invalid_grant",
  "error_description": "OTP code is mandatory"
}
```

```
$ curl -u app:app -X POST -d "username=admin&password=jwtpass&otp=123456"\  
http://localhost:8000/tokens?grant_type=password

< HTTP/1.1 400
{
  "error": "invalid_grant",
  "error_description": "Invalid OTP code"
}
```

```
$ curl -u app:app -X POST -d "username=admin&password=jwtpass&otp=007629"\  
http://localhost:8000/tokens?grant_type=password

{
  "access_token":  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsieS1yZXNvdXJjZSJdLCJleHAiOjE1MjgzMTIxMTEsInVzZXJfbmFtZSI6ImpvaG5kIiwianRpIjoiMTdhYTMxZjktNTMxYS00NjJiLWE2NTctY2YwZWFlMzNjYzZmIiwiY2xpZW50X2lkIjoieWFwcCIsInNjb3BlIjpbInJlYWQiXX0.Y-onGiYPMYnhhnn2NmXLnu4aC7swoDxeUv6OZLr8M6I",
  "token_type": "bearer",  
  "refresh_token":   "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsieS1yZXNvdXJjZSJdLCJ1c2VyX25hbWUiOiJqb2huZCIsInNjb3BlIjpbInJlYWQiXSwiYXRpIjoiMTdhYTMxZjktNTMxYS00NjJiLWE2NTctY2YwZWFlMzNjYzZmIiwiZXhwIjoxNTMwODkzMzExLCJqdGkiOiJjMzM2ZDU0ZS03YjQxLTQzOGItODE1Zi1kNjIyYmZlZjI1NTAiLCJjbGllbnRfaWQiOiJ5YXBwIn0.lx0S6AYqbaEdkUf7oaG6hQ1cRDAa6YBig-wD71djUC8",   
  "expires_in": 10799,   
  "scope": "read",   
  "jti": "17aa31f9-531a-462b-a657-cf0eae33cc6f"
}
```

## License

Project is under [MIT license](https://opensource.org/licenses/MIT)
