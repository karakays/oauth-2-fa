usage of client scope
usage of user authentication

token=$(curl -u webapp:webapp -X POST -d "username=johnd&password=jwtpass" http://localhost:8000/tokens?grant_type=password | jq -r '.access_token')

curl -H "Authorization: Bearer $token" http://localhost:8000/app/random

DefaulTokenServices implements ResourceServerTokenServices and AuthorizationServerTokenServices, serves as main service class for tokenstore and tokenconverter.
    
ResourceServerTokenServices ->
	OAuth2Authentication loadAuthentication(String accessToken) 
    
AuthorizationServerTokenServices -> 
	Auth2AccessToken createAccessToken(OAuth2Authentication authentication)
	OAuth2AccessToken refreshAccessToken(String refreshToken, TokenRequest tokenRequest)

### AuthorizationServer Configuration

Purpose: to authorize by credentials and generating access tokens if correct credentials are provided

### ResourceServer Configuration

Purpose: Protect resources based on access tokens provided.

OAuth2AuthenticationProcessingFilter to convert JWT into Authentication

### Clients

Every client has its own resource, scope and grant types.

Clients are assigned to resources. Access tokens are generated based on these resources clients have. Every client can access only its own resources. Are resources audience?

resource server configuration
this endpoint can be access by clients having scopes

### Grant types

Clients need to be granted with certain grant types. If a client attempts to get a token through an unauthorized grant type, it gets an error. 

Password grants are switched on by injecting an AuthenticationManager, if not set explicitly per client.

AuthorizationServerConfigurerAdapter

 @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(clientId) // start building a client
            .secret(clientSecret)
            // client can be restricted to a certain grant type if necessary
            // there are certain beans AuthenticationManager, AuthenticationManager, etc.
            // that need to injected in order to enable grants implicitly
            // otherwise set explicitly as below
            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials")
            // Scopes are authorities of a client. Not to mix with authorities (roles) of a user.
            // A client can have many scopes
            .scopes(scopeRead, scopeWrite, "custom-sceop-1")
            // a client can have multiple resources
            .resourceIds(resourceIds);
    }
    

### OAuth2 grant autorization_type

grant: authorization_type

hit http://localhost:8000/oauth/authorize?client_id=app&redirect_uri=http://google.com&response_type=code

VIEW A
spring renders default login form for the end-user to login

VIEW B
if successfully logged in, spring returns default view for the user to authorize or deny 'client_id'

if user approves, spring redirects browser to google.com?code=XYZ

if user denies it, spring redirects browser to google.com?error=access_denied&error_description=User%20denied%20access

Client gets authorization code from query string as shown above and makes a request to get access token

curl -u app:app -X POST "http://localhost:8000/token?grant_type=authorization_code&redirect_uri=google.com&code=RQeyEd


### Spring Authentication

Via @EnableWebSecurity, enable spring security and hint to use all the defaults

/oauth/token?grant_type=password

1. client authentication

  BasicAuthenticationFilter -> DaoAuthenticationProvider.authenticate(Authentication)

  Authentication contains username (app) and WebAuthenticationDetails.

  authenticate():
	preAuthenticationChecks() : check if account expired, deleted, locked etc.
	additionalAuthenticationChecks() : get hash of provided password using PasswordEncoder configured before. Compare passwords.
	
2. end-user password authentication

  Arrives at TokenEndpoint.

  do scope checks of the client.

  DaoAuthenticationProvider.authenticate(Authentication)

  Create Oauth2AccessToken and return.

### AuthenticationProvider 

AuthenticationProvider.authenticate(Authentication)
	-> DaoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken)
		-> TotpAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken)
		
#### Resources

* http://projects.spring.io/spring-security-oauth/docs/oauth2.html
* https://alexbilbie.com/guide-to-oauth-2-grants/
* https://github.com/nydiarra/springboot-jwt