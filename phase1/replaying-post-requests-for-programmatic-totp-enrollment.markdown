---
title: Replaying POST Requests for Programmatic TOTP Enrollment
date: 2020-11-06 10:17:00 -06:00
position: 4
---

# Replaying the POST requests

By Caleb Jiang

We were able to successfully replay the post request that replied with the TOTP secret so that we could automate the enrollment process. In the browser, during the enrollment process, the following is the response:

`)]}',`\
`{"RegistrationType":3,"QrCode":"(base64 of QR code png trimmed for brevity)","ActivationCode":null,"Url":null,"SameDeviceUrl":"","AccountName":"SF Insider:cjiang@sfinsider.onmicrosoft.com","SecretKey":"zdndcldvxwmb7nfv","AffinityRegion":null}`

There is a clearly identifiable SecretKey (base32 TOTP secret) in the JSON that can be used in the challenge response to enroll the MFA method. When replaying the next request with the secretKey in the payload, the server returns a VerificationContext that is used for the next request alongside the current TOTP OTP to complete the enrollment. The following is an example response for the next request (AddSecurityInfo):

`)]}',`\
`{"Type":3,"VerificationState":1,"Data":null,"VerificationContext":"C5LBw6HkKkyjOK4fYhgfzc2wZ8mWcxkVigEpCVB6fhsltrcFObiM3Jikj1OTQzuGQ+H04uxvirebIDcQuu8OquAT4SlE2+yKC2ZIRix/ejGcOSrWSn0sJ/cPKjEL0+g8oC0tGgRJbkIK8umvraihgGxIkhE4KNSzNUiybHbV8z7NboQftNEFIh6UmiK0vS1MG/1t3WwD9mqQAUhAo8dEfr/gL0+2I5nTKkR51PM/P5WaToeHyoacCJ/dURzRMtMtLRgFV0BrD51AMDq8HDdBsLF1Fv/eIEK7qCbk0QRcff4=","ErrorCode":0}`

When using the exact same headers as a recent request, the server responds as expected with HTTP 200 and the response needed for the next step. 

### Unique Values Based on Session/User

Though most of the header fields seem to be the same across requests, obvious some need to differ depending on the user and session. I've noticed that the following fields are unique each time: SessionCtx, Authorization, and Cookie. Additionally, between the three requests, InitializeMobileAppRegistration has a content length of 22, AddSecurityInfo of 80, and VerifySecurityInfo of 363. All other headers are the same across requests. Additionally, InitializeMobileAppRegistration always has a payload of `{"securityInfoType":3}`, AddSecurityInfo of `{"Type":3,"Data":"{\"secretKey\":\"fs5xl5whhgz6p6cp\",\"affinityRegion\":null}"}`with the SecretKey from the response of InitializeMobileAppRegistration , and VerifySecurityInfo of `{"Type":3,"VerificationContext":"C5LBw6HkKkyjOK4fYhgfzc2wZ8mWcxkVigEpCVB6fhsltrcFObiM3Jikj1OTQzuGQ+H04uxvirebIDcQuu8OquAT4SlE2+yKC2ZIRix/ejGcOSrWSn0sJ/cPKjEL0+g8oC0tGgRJbkIK8umvraihgGxIkhE4KNSzNUiybHbV8z7NboQftNEFIh6UmiK0vS1MG/1t3WwD9mqQAUhAo8dEfr/gL0+2I5nTKkR51PM/P5WaToeHyoacCJ/dURzRMtMtLRgFV0BrD51AMDq8HDdBsLF1Fv/eIEK7qCbk0QRcff4=","VerificationData":"502291"},` with the VerificationContext from  AddSecurityInfo and VerificationData from the current TOTP OTP.

`)]}',`\
`{"CID":"dfbb61e8-ea76-4e8a-8f80-352555e77c41","Date":"2020-11-06T16:52:15.7582379Z","Exception":null}`

Another interesting thing to note is that it doesn't seem like InitializeMobileAppRegistration is necessary at all, since you can fill in your own SecretKey into AddSecurityInfo's payload and it seems like everything works as normal.