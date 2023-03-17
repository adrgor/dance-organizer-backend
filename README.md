# PORTS:
## Services
- user: 8000-8009
- event: 8010-8019
- participant: 8020-8030

## Infrastructure
- api-gateway: 8080-8089
- email-service:8090-8099
- naming-server: 8761

#ERROR CODES
- 1000 - Specified email is already in use
- 1001 - Specified username is already in use
- 1002 - Specified user has not been activated yet
- 1003 - Specified password is invalid for given user
- 1004 - Specified user was not found