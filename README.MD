# Simple application with REST API

Application starts with connecting to localhost postgresql database on 5432 port. Connect will be to postgres schema.
You should set as environment variables your postgres username and password as POSTGRES_USERNAME, POSTGRES_PASSWORD. By
default, uses username and password postgres, postgres.

1. Swagger API is open for watching all operations
2. For execution any operation of 'users' API, you need to sign up to API. It's easy. You can watch request in swagger '
   auth-api-controller'.
3. Access to 'users' API by bearer token. After your registration, you should go to 'login' path in '
   auth-api-controller', and you will get bearer token.
4. Any operation with 'users' API must be with bearer token.
5. For checking integration tests with postman, you may use integration_tests.json in postman folder.

- Uses hibernate second level cache
- Uses quartz for job scheduling
- Uses springfox for openapi codegen from yml
- Uses JWT bearer token for auth
- Uses liquibase for DB migrations
- Uses validation in requests