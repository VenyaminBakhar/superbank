    HOW TO RUN

mvn package
java -jar "target/superbank-1.0-SNAPSHOT.jar"



    ACCOUNT API
1) Get account by id        GET http://localhost:3535/accounts/id

2) Delete account by id     DELETE http://localhost:3535/accounts/delete/id

3) Create account by id     POST http://localhost:3535/accounts
   Body example: { "holderName" : "NEW ACCOUNT HOLDER NAME", "balance" : "1000" }



    TRANSACTION API
1) Transfer money           POST http://localhost:3535/transactions
   Body example:  { "idFrom" : "1", "idTo" : "2", "amount" : "1000" }

2) Get transaction history  GET http://localhost:3535/transactions/history/:id
 ordered by transaction date



    HTTP RESPONSES
Get account api:
200 OK         "Account{id=2, holderName='Veniamin Bakhar', balance=1000000.0}"
404 Not found  "Account not found"

Create account api:
201 OK                      "Account{id=2, holderName='Veniamin Bakhar', balance=1000000.0}"
400 Bad Request             "Error message"
500 Internal Server Error   "Exception message"

Delete account api:
204 ""
404 Not found  "Account not found"
500 Internal Server Error   "Exception message"

Do transaction api:
200 OK                      "Transaction was successfully completed!"
400 Bad Request             "Error message"
500 Internal Server Error   "Exception message"

Get transaction history api:
200 OK                      "Transaction history"
404 Not Found               "Transaction history not found"
500 Internal Server Error   "Exception message"


    NOTES
"Get account" api performance was increased by caching.
