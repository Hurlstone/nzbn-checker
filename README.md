# NZBN checker
This is a program which returns a check of a New Zealand business using their NZBN. 

Checks if the NZBN entered is valid(exactly 13 digits long), checks if the api key is null,
creates the request to the api and then returns the result as shown in the example below.

## Requirements
* Java 21
* Maven
* MBIE NZBN Api key

# Example of program
### Input the NZBN
```
Enter an NZBN: 9429053415790
```
### Response
```
NZBN: 9429053415790
Business name: WORKLY LIMITED
Business status: Registered
Business registration date: 2026-02-04T09:01:14.000+1300
```
# Environment variables
Create a .env file in the directory containing: 
```NZBN_API_KEY=YOUR_API_KEY_HERE```
## Remember to never commit your .env file to GitHub! That is bad! 
