---
title: Replaying POST Requests for Programmatic TOTP Enrollment
date: 2020-11-06 10:17:00 -06:00
---

# Replaying the POST requests

By Caleb Jiang

We attempted to replay the post request that replied with the TOTP secret so that we could automate the enrollment process. In the browser, during the enrollment process, the following is the response:

`)]}',`\
`{"RegistrationType":3,"QrCode":"(base64 of QR code png trimmed for brevity)","ActivationCode":null,"Url":null,"SameDeviceUrl":"","AccountName":"SF Insider:cjiang@sfinsider.onmicrosoft.com","SecretKey":"zdndcldvxwmb7nfv","AffinityRegion":null}`

There is a clearly identifiable SecretKey (base32 TOTP secret) in the JSON that can be used in the challenge response to enroll the MFA method. 