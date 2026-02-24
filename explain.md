Concepts Explanation: OAuth2 & Token Blacklisting
This document explains two advanced security concepts implemented in Lab 7: OAuth2 with Google and Token Blacklisting.

1. OAuth2 (Google Login)
   What is it?
   OAuth2 is a protocol that allows a user to grant a third-party application (like our Hospital App) access to their resources (like their Google Profile) without sharing their password.

The Flow (Step-by-Step)
User Clicks "Login with Google":

The browser redirects the user to Google's limit page.
URL: https://accounts.google.com/o/oauth2/auth?...
User Signs In on Google:

The user enters their Gmail and Password on Google's site. Our app never sees this password.
Google asks: "Do you want to share your Name and Email with Hospital App?"
User clicks "Yes".
Google Redirects Back:

Google sends the user back to our app with a special code.
URL: http://localhost:8080/login/oauth2/code/google?code=xyz...
Backend Swaps Code for Token:

Our Spring Boot backend (behind the scenes) takes that code and talks to Google directly: "Here is the code, give me the user's details."
Google verifies the code and replies with an Access Token and the User's Info (Name, Email, Picture).
User Persistence (What we added):

CustomOAuth2UserService
intercepts this final step.
It looks at the email (e.g., bruce@gmail.com).
Check: Do we have this user in our users database table?
No: Create a new row. username=bruce@gmail.com, role=RECEPTIONIST.
Yes: Update their name or picture if changed.
Finally, we log the user in to our app. 2. Token Blacklisting (Logout)
The Problem with JWTs
A JWT (JSON Web Token) is a stateless "access card".

Stateless: The server signs it and gives it to you. The server does not keep a copy in the database.
Verification: When you send a request, the server just checks the signature.
Logout Issue: If you want to log out, you can delete the token from your browser. BUT, if a hacker copied your token before you deleted it, they can still use it until it expires (e.g., in 1 hour). The server doesn't know you "logged out".
The Solution: Blacklisting
To fix this, we implement a Blacklist.

The Blacklist: A list of "Revoked Tokens".

Ideally, this is stored in a fast database like Redis.
For this lab, we use an In-Memory Set (Java Set<String>).
Logout Action:

When you verify call /auth/logout, the server takes your JWT.
It adds the JWT string to the Blacklist.
The Gatekeeper (Filter):

Every time any request comes in (e.g., GET /api/patients), the JwtAuthenticationFilter does an extra check:
Standard Check: Is the signature valid? (Yes)
Blacklist Check: Is this specific token string in the Blacklist?
Yes: Block request! (401 Unauthorized).
No: Allow request.
Visual Analogy
JWT: A stamped movie ticket.
Standard Validation: Usher checks if the stamp is real.
Blacklisting: The theater manager writes down the serial numbers of stolen tickets on a whiteboard. The usher now checks the stamp AND the whiteboard.
