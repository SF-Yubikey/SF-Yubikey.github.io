---
title: Microsoft Graph API
date: 2020-10-29 13:02:00 -05:00
position: 0
---

In order to find a way to avoid using Microsoft Authenticator when performing an initial login to an Azure Active Directory (Azure AD) account, a potential solution was to pre-load the necessary information into the user accounts through an API. We were able to find two APIs that work with Azure AD; Azure AD Graph API and Microsoft Graph API. Both of these, however, do not have the capability required for our purposes. First lets look at the Azure AD Graph API.

# Azure AD Graph API
The first problem with using this API comes up right away.
[AzureADAPIwarning.PNG](/uploads/AzureADAPIwarning.PNG)
