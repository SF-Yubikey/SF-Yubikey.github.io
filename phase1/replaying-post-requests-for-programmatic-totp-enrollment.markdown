---
title: Programmatic MFA Enrollment
date: 2020-11-06 10:17:00 -06:00
position: 4
---

By Caleb Jiang

Towards the end of my research, I discovered that besides self-service, [there is also an administrator batch registration method](https://support.yubico.com/hc/en-us/articles/360015669179-Using-YubiKeys-with-Azure-MFA-OATH-TOTP) of self-generating TOTP secrets and [Yubico being able to pre-program Yubikeys](https://www.yubico.com/products/manufacturing/programming-options/) with the secrets before shipping directly to a customer. Admins are able to [directly upload a csv of secret keys with corresponding upns](https://techcommunity.microsoft.com/t5/azure-active-directory-identity/hardware-oath-tokens-in-azure-mfa-in-the-cloud-are-now-available/ba-p/276466) into the Azure console to mass auto-enroll users with TOTP keys. This would easily solve the primary problem of bypassing the need for a mobile authenticator without the use of hacky workarounds or writing and running custom software.

### Replaying the Requests

We were successfully able to replay the POST requests used during the enrollment of a TOTP authenticator. ([YouTube demo](https://www.youtube.com/watch?v=Os1TCClk4aQ), watch at 2x speed and turn down the volume) In the browser, during the enrollment process, the following is the response:

`)]}',`\
`{"RegistrationType":3,"QrCode":"(base64 of QR code png trimmed for brevity)","ActivationCode":null,"Url":null,"SameDeviceUrl":"","AccountName":"SF Insider:cjiang@sfinsider.onmicrosoft.com","SecretKey":"zdndcldvxwmb7nfv","AffinityRegion":null}`

There is a clearly identifiable SecretKey (base32 TOTP secret) in the JSON that can be used in the challenge response to enroll the MFA method. When replaying the next request with the secretKey in the payload, the server returns a VerificationContext that is used for the next request alongside the current TOTP OTP to complete the enrollment. The following is an example response for the next request (AddSecurityInfo):

`)]}',`\
`{"Type":3,"VerificationState":1,"Data":null,"VerificationContext":"C5LBw6HkKkyjOK4fYhgfzc2wZ8mWcxkVigEpCVB6fhsltrcFObiM3Jikj1OTQzuGQ+H04uxvirebIDcQuu8OquAT4SlE2+yKC2ZIRix/ejGcOSrWSn0sJ/cPKjEL0+g8oC0tGgRJbkIK8umvraihgGxIkhE4KNSzNUiybHbV8z7NboQftNEFIh6UmiK0vS1MG/1t3WwD9mqQAUhAo8dEfr/gL0+2I5nTKkR51PM/P5WaToeHyoacCJ/dURzRMtMtLRgFV0BrD51AMDq8HDdBsLF1Fv/eIEK7qCbk0QRcff4=","ErrorCode":0}`

When using the exact same headers as a recent request, the server responds as expected with HTTP 200 and a new TOTP token is enrolled, and verified working when signing in. After each token is enrolled, it is given a unique id that looks like `b80dab22-0294-43b9-b26b-2f8796024e86`, used when the authentication method is modified or deleted.

### Unique Values Based on Session/User

Though most of the header fields seem to be the same across requests, obvious some need to differ depending on the user and session. I've noticed that the following fields are unique each time: SessionCtx, Authorization, and Cookie.

The easiest one to get is the SessionCtx. There is a post request to [/securityinfo/Authorize](/totp-enroll-requests/authorize/) every time a page is accessed and the request can be replayed for a new SessionCtx. In the request, you need a Bearer token and various values from BOX.SessionCacheKey, which are a little harder to get. SessionCtx seems to expire very often and a new one is requested anytime the page is refreshed, after more than a few minutes passes, or if anything goes wrong. 

For the Bearer, it seems to be refreshed every hour by default and persists across page refreshes and is valid for all requests to a specific subdomain. It seems like there is a code and code_verifier [in the payload when requesting a Bearer](/totp-enroll-requests/token-60-minutes/) that is valid for 60 minutes while further token requests use the refresh token from the first request to get other Bearer tokens that are only valid for 10 minutes. I'm still pretty confused about where the code and code_verifier are stored in the browser, as well as when the longer term token is used vs the shorter term one. This is an area that requires further research.

BOX.SessionCacheKey is stored as a request cookie, and can simply be read from the local storage where cookies are stored. However, when trying to figure out when the cookies were received, I came up empty. [This StackOverflow thread](https://stackoverflow.com/a/11800394) says that every request cookie must be a response cookie from a previous request, but I went through every single request from before the Authorize request that used the request cookies BOX.SessionCacheKey and not a single one had it as a response cookie. So the methods for requesting these cookies are still unknown to me.

Below: Network activity when loading https://mysignins.microsoft.com/security-info. I scanned through every request before Authorize and didn't find anything about BOX.SessionCacheKey.![Screenshot 2020-11-13 081542.png](/uploads/Screenshot%202020-11-13%20081542.png)

Even more interestingly, the only time the site cookies are actually used is in the initial HTTP request of loading the page. Any further JS requests all used the mythical response-request cookies. I have to say, I definitely lack experience in this area.

![Screenshot 2020-11-13 081817-54fc49.png](/uploads/Screenshot%202020-11-13%20081817-54fc49.png)

Between the three requests, InitializeMobileAppRegistration has a content length of 22, AddSecurityInfo of 80, and VerifySecurityInfo of 363. All other headers are the same across requests. Additionally, InitializeMobileAppRegistration always has a payload of {"securityInfoType":3}, AddSecurityInfo of `{"Type":3,"Data":"{\"secretKey\":\"fs5xl5whhgz6p6cp\",\"affinityRegion\":null}"}` with the SecretKey from the response of InitializeMobileAppRegistration , and VerifySecurityInfo of `{"Type":3,"VerificationContext":"C5LBw6HkKkyjOK4fYhgfzc2wZ8mWcxkVigEpCVB6fhsltrcFObiM3Jikj1OTQzuGQ+H04uxvirebIDcQuu8OquAT4SlE2+yKC2ZIRix/ejGcOSrWSn0sJ/cPKjEL0+g8oC0tGgRJbkIK8umvraihgGxIkhE4KNSzNUiybHbV8z7NboQftNEFIh6UmiK0vS1MG/1t3WwD9mqQAUhAo8dEfr/gL0+2I5nTKkR51PM/P5WaToeHyoacCJ/dURzRMtMtLRgFV0BrD51AMDq8HDdBsLF1Fv/eIEK7qCbk0QRcff4=","VerificationData":"502291"},` with the VerificationContext from AddSecurityInfo and VerificationData from the current TOTP OTP. Another interesting thing to note is that it doesn't seem like InitializeMobileAppRegistration is necessary at all, since you can fill in your own SecretKey into AddSecurityInfo's payload and it seems like everything works as normal.

If you get an HTTP error 500 with a response that looks like the one shown below, it means you messed up. Usually it's because you played a request out of order and invalidated the SessionCtx, used an old Authorization, Cookie, or SessionCtx, have the incorrect content length, or are missing some headers.

`)]}',`\
`{"CID":"dfbb61e8-ea76-4e8a-8f80-352555e77c41","Date":"2020-11-06T16:52:15.7582379Z","Exception":null}`

### MFA Type Codes?

Keith talked a little about RegistrationType codes in his post - the numbers used to describe what kind of authenticator is being enrolled in the initial InitializeMobileAppRegistration request. These registration type codes seem to be unique to InitializeMobileAppRegistration, however. When loading the security-info page, [AvailableAuthenticationInfo is called](/totp-enroll-requests/availableauthenticationinfo/) to get information about each authentication method. (i.e. whether it can be deleted, last updated time, CanBeDefault, CanAdd, etc.) Here, there's a different list of type codes that are used to identify each type of authenticator. MobilePhone is 4, Fido is 12, Email is 8, and AuthenticatorApp is 1.

### Automating FIDO2 Enrollment?

Currently there is no way of mass enrolling FIDO2 keys from the admin side in Azure. Even if there were, I don't even think this is theoretically possible because of the touch requirement for the Yubikey to generate an access key. The only way to pre-enroll the keys would be for IT staff to manually do it, and the speed at which IT staff could physically touch each key to enroll them, setting a pin, as well as track and deliver the correctly paired one to each employee even if all UI prompts were automated during the enrollment process just wouldn't be worth it. I did try to record and replay the HTTP requests, but the requests have to be completed within a modern browser to properly communicate with the OS to access the hardware-level APIs needed to interface with a USB or NFC device. (Yes, there is fallback to keyboard emulation in the spec, but I don't think Microsoft supports that, at least on Windows) Obviously, with headless Chromium, this is theoretically possible within a custom program, but users would still have to touch the security key themselves and set a pin, so all such a program would do is save a couple instances of clicking "next" and "ok." Especially if the first step of TOTP enrollment were already complete with one of the methods above, I don't think users would have that much difficulty clicking through some prompts and setting a pin themselves.