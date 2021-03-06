CREATE TABLE OAUTH_CLIENT_DETAILS ( CLIENT_ID VARCHAR(255) PRIMARY KEY, RESOURCE_IDS VARCHAR(255), CLIENT_SECRET VARCHAR(255),
  SCOPE VARCHAR(255), AUTHORIZED_GRANT_TYPES VARCHAR(255), WEB_SERVER_REDIRECT_URI VARCHAR(255), AUTHORITIES VARCHAR(255),
  ACCESS_TOKEN_VALIDITY INTEGER, REFRESH_TOKEN_VALIDITY INTEGER, ADDITIONAL_INFORMATION VARCHAR(4096), AUTOAPPROVE VARCHAR(255));
  
INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES,
	ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY) VALUES
    ('curl', 'web-resource', 'secret', 'read,write', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000),
	('webapp', 'web-resource', 'webapp', 'read,write', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000),
	('iosapp', 'web-resource', 'iosapp', 'read', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000),
	('andapp', 'admin-resource', 'andapp', 'read', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000);
 
INSERT INTO user_credentials (id, password, username, secret, is_2fa_enabled) VALUES
	(1, '$2a$10$nLrCuyqhrIK3x2M7EYIn7u7LEaXbAJTvPuY4QQeBBvdnuPn4j9kqe', 'alice', NULL, false),
	(2, '$2a$10$nLrCuyqhrIK3x2M7EYIn7u7LEaXbAJTvPuY4QQeBBvdnuPn4j9kqe', 'bob', 'JBSWY3DPEHPK3PXP', true);