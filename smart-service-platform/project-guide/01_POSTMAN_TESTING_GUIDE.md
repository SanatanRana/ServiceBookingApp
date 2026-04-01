📮 Postman Testing Guide — Smart Service Booking Platform
Pre-Requisites Before Testing
IMPORTANT

Before you start testing, make sure ALL 7 services are running in this exact order:

Discovery Server (port 8761) — Start FIRST
Auth Service (port 8081)
User Service (port 8082)
Provider Service (port 8083)
Booking Service (port 8084)
Payment Service (port 8085)
API Gateway (port 8080) — Start LAST
Also make sure MySQL is running on port 3306.

Verify Eureka Dashboard
Open in browser: http://localhost:8761 You should see all 6 services listed as UP.

🔓 Step 1: Register a User
This creates a new user account in the system.

Field	Value
Method	POST
URL	http://localhost:8080/auth/register
Headers	Content-Type: application/json
Auth	None required
Body (raw → JSON):

json
{
    "username": "john",
    "password": "pass123",
    "role": "ROLE_USER"
}
Expected Response:

User saved successfully
Status: 200 OK

TIP

You can also register a provider with "role": "ROLE_PROVIDER" or admin with "role": "ROLE_ADMIN".

🔑 Step 2: Login (Get JWT Token)
This authenticates the user and returns a JWT token.

Field	Value
Method	POST
URL	http://localhost:8080/auth/login
Headers	Content-Type: application/json
Auth	None required
Body (raw → JSON):

json
{
    "username": "john",
    "password": "pass123"
}
Expected Response:

eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwic3ViIjoiam9obiIsImlhdCI6MTc0ODU...
Status: 200 OK

CAUTION

COPY THIS ENTIRE TOKEN! You will need it for ALL remaining requests.

How to use the token in Postman:

Go to the Authorization tab
Select Type → Bearer Token
Paste your token in the Token field
OR add manually as a Header:

Key	Value
Authorization	Bearer eyJhbGciOiJIUzI1NiJ9.eyJy...
👤 Step 3: Create User Profile
Creates the profile details for the logged-in user.

Field	Value
Method	POST
URL	http://localhost:8080/users
Headers	Content-Type: application/json
Auth	Bearer Token → paste your JWT
Body (raw → JSON):

json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210"
}
Expected Response:

json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210"
}
Status: 200 OK

👤 Step 4: Get User Profile
Fetches the user profile by ID.

Field	Value
Method	GET
URL	http://localhost:8080/users/1
Headers	None extra needed
Auth	Bearer Token → paste your JWT
Body	None
Expected Response:

json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210"
}
Status: 200 OK

🔧 Step 5: Create a Service Provider
Registers a service provider (like a Pandit, Electrician, etc.).

Field	Value
Method	POST
URL	http://localhost:8080/providers
Headers	Content-Type: application/json
Auth	Bearer Token → paste your JWT
Body (raw → JSON):

json
{
    "id": 1,
    "name": "Pandit Sharma",
    "serviceType": "Pandit",
    "experience": "10 Years",
    "city": "Delhi",
    "available": true
}
Expected Response:

json
{
    "id": 1,
    "name": "Pandit Sharma",
    "serviceType": "Pandit",
    "experience": "10 Years",
    "city": "Delhi",
    "available": true
}
Status: 200 OK

🔧 Step 6: Get Provider Profile
Field	Value
Method	GET
URL	http://localhost:8080/providers/1
Auth	Bearer Token → paste your JWT
Body	None
Expected Response:

json
{
    "id": 1,
    "name": "Pandit Sharma",
    "serviceType": "Pandit",
    "experience": "10 Years",
    "city": "Delhi",
    "available": true
}
🛠️ Step 7: Add a Service to a Provider
Adds a specific service offering under a provider.

Field	Value
Method	POST
URL	http://localhost:8080/providers/1/services
Headers	Content-Type: application/json
Auth	Bearer Token → paste your JWT
Body (raw → JSON):

json
{
    "serviceName": "Wedding Puja",
    "description": "Complete wedding puja ceremony with all rituals",
    "price": 5000
}
Expected Response:

json
{
    "id": 1,
    "serviceName": "Wedding Puja",
    "description": "Complete wedding puja ceremony with all rituals",
    "price": 5000.0,
    "providerId": 1
}
Status: 200 OK

🛠️ Step 8: Get All Services of a Provider
Field	Value
Method	GET
URL	http://localhost:8080/providers/1/services
Auth	Bearer Token → paste your JWT
Body	None
Expected Response:

json
[
    {
        "id": 1,
        "serviceName": "Wedding Puja",
        "description": "Complete wedding puja ceremony with all rituals",
        "price": 5000.0,
        "providerId": 1
    }
]
📋 Step 9: Create a Booking ⭐ (Most Important Test!)
This is the key test — it triggers inter-service communication between Booking Service → Payment Service via Feign Client.

Field	Value
Method	POST
URL	http://localhost:8080/bookings/create
Headers	Content-Type: application/json
Auth	Bearer Token → paste your JWT
Body (raw → JSON):

json
{
    "userId": 1,
    "providerId": 1,
    "serviceId": 1,
    "bookingDate": "2026-05-15"
}
Expected Response:

json
{
    "id": 1,
    "userId": 1,
    "providerId": 1,
    "serviceId": 1,
    "bookingDate": "2026-05-15",
    "status": "CONFIRMED"
}
NOTE

The status will be either "CONFIRMED" or "FAILED" — this is random on purpose because the Payment Service uses a mock payment processor with Random().nextBoolean(). Try the request multiple times to see both results!

What happens behind the scenes:

POST /bookings/create → Gateway → JWT ✅ → BookingService
    ↓
    Save booking (PENDING)
    ↓
    Feign Client → PaymentService (direct call, bypasses Gateway)
    ↓
    Payment processed (random SUCCESS/FAILED)
    ↓
    Update booking: SUCCESS → CONFIRMED | FAILED → FAILED
    ↓
    Return final booking to you
📋 Step 10: Get User's Bookings
Field	Value
Method	GET
URL	http://localhost:8080/bookings/user/1
Auth	Bearer Token → paste your JWT
Body	None
Expected Response:

json
[
    {
        "id": 1,
        "userId": 1,
        "providerId": 1,
        "serviceId": 1,
        "bookingDate": "2026-05-15",
        "status": "CONFIRMED"
    }
]
📋 Step 11: Get Provider's Bookings
Field	Value
Method	GET
URL	http://localhost:8080/bookings/provider/1
Auth	Bearer Token → paste your JWT
Body	None
Expected Response: Same as above (same booking, different query).

💳 Step 12: Get Payment Details
Check the payment record created automatically during booking.

Field	Value
Method	GET
URL	http://localhost:8080/payments/1
Auth	Bearer Token → paste your JWT
Body	None
Expected Response:

json
{
    "id": 1,
    "bookingId": 1,
    "amount": 500.0,
    "status": "SUCCESS",
    "paymentMethod": "CARD"
}
🔒 Step 13: Security Test — Access WITHOUT Token
Verify that protected routes are blocked without authentication.

Field	Value
Method	GET
URL	http://localhost:8080/users/1
Auth	None (remove the Bearer token!)
Body	None
Expected Response:

500 Internal Server Error
This proves your JWT security is working! ✅

🔑 Step 14: Validate Token (Optional)
You can verify if a token is still valid.

Field	Value
Method	GET
URL	http://localhost:8080/auth/validate?token=YOUR_JWT_TOKEN_HERE
Auth	None required
Body	None
Expected Response:

Token is valid
📊 Quick Reference — All Endpoints
#	Method	URL	Auth	Purpose
1	POST	/auth/register	❌ No	Register new user
2	POST	/auth/login	❌ No	Login, get JWT token
3	GET	/auth/validate?token=xxx	❌ No	Validate a JWT token
4	POST	/users	✅ JWT	Create user profile
5	GET	/users/{id}	✅ JWT	Get user profile
6	POST	/providers	✅ JWT	Create provider
7	GET	/providers/{id}	✅ JWT	Get provider profile
8	POST	/providers/{id}/services	✅ JWT	Add service to provider
9	GET	/providers/{id}/services	✅ JWT	Get provider's services
10	POST	/bookings/create	✅ JWT	Create booking (→ Payment)
11	GET	/bookings/user/{userId}	✅ JWT	Get user's bookings
12	GET	/bookings/provider/{providerId}	✅ JWT	Get provider's bookings
13	GET	/payments/{id}	✅ JWT	Get payment details
TIP

All URLs go through the Gateway at http://localhost:8080. Never use the individual service ports (8081-8085) directly in Postman — always use 8080.

❗ Common Errors & Solutions
Error	Cause	Solution
Connection refused	Service not running	Start all services in order
500 Internal Server Error on protected route	Missing JWT token	Add Authorization: Bearer <token> header
403 Forbidden on login	Wrong password or user doesn't exist	Register a new user first, then login
404 Not Found	Wrong URL or service not registered	Check Eureka dashboard at localhost:8761
Whitelabel Error Page	Opened service URL in browser	Use Postman instead — these are REST APIs, not web pages
Token expired	JWT valid for 30 minutes only	Login again to get a new token
🧪 Bonus: Add More Test Data
Register a Provider account:
json
POST /auth/register
{
    "username": "pandit_sharma",
    "password": "provider123",
    "role": "ROLE_PROVIDER"
}
Add another provider:
json
POST /providers (with JWT)
{
    "id": 2,
    "name": "Electrician Raju",
    "serviceType": "Electrician",
    "experience": "5 Years",
    "city": "Mumbai",
    "available": true
}
Add service to second provider:
json
POST /providers/2/services (with JWT)
{
    "serviceName": "AC Repair",
    "description": "Split and window AC repair and servicing",
    "price": 1500
}
Book the electrician:
json
POST /bookings/create (with JWT)
{
    "userId": 1,
    "providerId": 2,
    "serviceId": 2,
    "bookingDate": "2026-06-01"
}