---
title: YubiEnterprise Delivery
date: 2020-10-31 06:53:00 -05:00
position: 1
---

# YubiEnterprise Delivery

YubiEnterprise Delivery API is a REST API where a lot of things have been setup for easy user interactions. This guide will show you how to interact with the resources. The API allows you to view or edit resources in the system that pertain to accounts, authentication, organizations, users, purchaseOrders, inventory, shipments, addresses, countries and products.

Upon obtaining the YubiEnterprise account and generating a new API token, make sure to copy the token and store it in a safe place. After leaving the page, the token is no longer viewable and the only thing that can be seen is the active token reference. YubiEnterprise Delivery Auditor account types are not able to revoke an API token and create another.
Navigating past the page will only give the active token reference, so make sure to copy the API token at the time of API token creation.

![1.png](/uploads/1.png)

**Note: **The token is very long and should not be short like the active token reference
In the case that the API token is not stored, reset the account.

**Note:** With the tokens, each one is scoped to specific regions such as US/CANADA and EU. The tokens can be used to ship only to their specific regions. So, in the case of making a request for a new shipment using a token from the US/CANADA region, the shipment can only be delivered within the US/CANADA region.

**Authentication Example:**

To authenticate with an API token and test requests,
Create a bash script that uses the token like this:
curl "https://api.console.yubico.com/v1/shipments"
--header "Authorization: Bearer TOKEN… "

![2.png](/uploads/2.png)

This specific example uses GET to obtain the list for all shipments and information regarding them.

Change the end of the https to the corresponding action referred in the [apidocs.](https://console.yubico.com/apidocs/)

For example, this action to list the shipments is a GET request, only asking for the return of information about the resource. The response then lists the information about all shipments. It uses the end of the https which is  /shipments

![11.png](/uploads/11.png)

Change this to whatever type of requests you want to make from the [apidocs.](https://console.yubico.com/apidocs/)

https://api.console.yubico.com/v1/shipments \*change this\*

To change the option add -X ACTION after the curl
By default the example above uses GET. GET, as it's name implies only gets information about the resource.
ACTIONS can be GET, POST, PUT, DELETE
The [apidocs](https://console.yubico.com/apidocs/) only supports these actions for use.

Alternative option to test the API: (Reqbin API testing tool)
From the previous example,
curl "https://api.console.yubico.com/v1/shipments" \
--header "Authorization: Bearer TOKEN… "
Use the part after the curl https://api.console.yubico.com/v1/shipments and put it in the first field to the left of GET.
To change the type of request sent click on GET, where you can change it to something like POST or DELETE.
Put the token in the field that is labeled Token
Hit Send to start the request.

This is the same information that is received from using the script method.
The Resource information is returned in content and displays things such as carrier.

![3.png](/uploads/3.png)

For carrier, it lists the shipping service used for one of the shipments as USPS.

A JSON view of the return looks like this:

![4.png](/uploads/4.png)

**Setting up your return as a JSON**

To do this with the curl method, you would need to add

**What to do with the response message?**

In this part of the guide, I will be explaining one possible way to use the return from your GET request.

The message return can be in JSON format. So in this case, you could take the JSON return and turn it into a String.

Then use the String however you want it, such as grabbing parts of the String and displaying it to the user.

For example, if you need the carrier part of the string, you would only get that part of it and then choose how you want to display it to the user.

**Example of the GET message and working code with it:**

\(Edit later)

**How to bulk deliver?**

\(Edit later)