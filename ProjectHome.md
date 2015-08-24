ZSOA is maintained by Zodiac Innovation Team.

It has two parts, the server and the client. In the server side you will construct your business objects simply by inheriting from BusinessLogic, SessionBusinessLogic or PrivateBusinessLogic, depending on the security level that you need. Each business object will be public to be called, but you can use the annotations Revoke, RevokeAll and Grant to manage the privileges of  an user's profile. For data objects, you will implement the design pattern DAO and DTO. The DAO design pattern will let you to add several data sources as different dbms or maybe another web service, using the same interface but with a different implementation. And now, what is a DTO? In every communication you need a message, well, DTO is your message, it is like a JavaBean, but this DTO can be automatically serialize to XML and send it to the client.

What about the security?
Well, I like the security part. This is my first project of this type and I'm very excited. As I said you have the Revoke, RevokeAll and Grant annotations to control the access of your users to the functions in the private business objects because it is based on the user's profile.

And this is favorite part, I added a time based one time password to the solution. This means that even if someone still your session token, it won't be useful because it only works in a short period of lifetime.

In the other hand, the client. By the moment it has the minimal requirements. Just call the business objects, handle the session and throw the server side exceptions.

I'll continue developing this project, and of course I accept suggestions.

By the way, if you want, you can use this project to make your life easier but remember, read the GNU GPL License and keep it free (everything is about the freedom).