---
title: Microsoft Graph API
date: 2020-10-29 13:02:00 -05:00
position: 0
---

In order to find a way to avoid using Microsoft Authenticator when performing an initial login to an Azure Active Directory (Azure AD) account, a potential solution was to pre-load the necessary information into the user accounts through an API. We were able to find two APIs that work with Azure AD; Azure AD Graph API and Microsoft Graph API. Both of these, however, do not have the capabilities required for our purposes. First lets look at the Azure AD Graph API.

# Azure AD Graph API

The first problem with using this API comes up right away.

![AzureADAPIwarning.PNG](/uploads/AzureADAPIwarning.PNG)

As shown above, this API is no longer being worked on and is almost completely obsolete. This API still has fringe uses, so let's examine the operations it supports on user objects:

* Create new user in a directory

* Get a user's detailed properties, such as their groups

* Update a user's properties, such as their location and phone number, or change their password

* Check a user's group membership for role-based access

* Disable a user's account or delete it entirely

You can also perform similar operations other objects such as groups and applications, but these aren't relevant to the matter at hand. For a full list of what the API can do, see Azure AD Graph API reference [[1]](https://docs.microsoft.com/en-us/previous-versions/azure/ad/graph/api/api-catalog). Unfortunately, none of these operations allow us to accomplish our goal. Since this API is no longer being updated, we can say, for sure, this API will not be useful to us.

# Microsoft Graph API

Since the resources from the Azure AD API development were moved to the development of the Microsoft Graph API, the Microsoft Graph API offers the best chance for pre-loading security key or Yubico Authenticator data before a users initial login.

Right out of the gate we notice that there are two versions of the Microsoft Graph API; v1.0 and beta. v1.0 includes generally available APIs and should be used for all production apps. beta includes APIs that are currently in preview, and constantly being updated. Since these updates might break the APIs, beta should only be used to test apps in development [[2]](https://docs.microsoft.com/en-us/graph/use-the-api).

In v1.0, the most promising methods were update user, create user, and update settings. For a full list of methods for the user resource type see [[3]](https://docs.microsoft.com/en-us/graph/api/resources/user?view=graph-rest-1.0). 

Update user was the most straight forward option to check. Unfortunately we are unable to modify authentication information except for the password[[4]](https://docs.microsoft.com/en-us/graph/api/user-update?view=graph-rest-1.0&tabs=http). Next, we checked if we can use create user to make a user account that already has the information. This also proved to not be possible [[5]](https://docs.microsoft.com/en-us/graph/api/user-post-users?view=graph-rest-1.0&tabs=http). Lastly, we gave update settings a good look. Once again we were unable to use this to change the users authentication information [[6]](https://docs.microsoft.com/en-us/graph/api/usersettings-update?view=graph-rest-1.0).

In beta we found exactly what we need. There's only one caveat: it's only in preview [[7]](https://docs.microsoft.com/en-us/graph/whats-new-overview). 
![New Oct 2020 Graph features](/uploads/gaph%20api%20grail.PNG)

We were frustratingly close to delivering a resounding "Yes! This is possible through the Microsoft Graph API." Unfortunately, the only authentication method we are able to change are fido2 security key information and passwordless signin with microsoft authenticator [[8]](https://docs.microsoft.com/en-us/graph/api/resources/authenticationmethodspolicies-overview?view=graph-rest-beta&preserve-view=true).

# Conclusion
Can the Microsoft Graph API be used to modify/add authentication information to users who haven't had their initial login, or need to set up MFA? Not to the extent that we need. The features that are coming and may very well be here soon, but in the current version of the API, it just isn't possible. The beta version paints a brighter picture of the future, but even with the features that are currently in production, we can't do everything that we need to.  