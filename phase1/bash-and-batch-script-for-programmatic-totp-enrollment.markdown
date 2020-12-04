---
title: Bash and Batch Script for Programmatic TOTP Enrollment
date: 2020-12-04 17:51:00 -06:00
position: 2
---

By Caleb Jiang

The scripts are downloadable [here](/uploads/Yubikey%20Scripts.zip).

A video demonstrating how they work is available [here](https://youtu.be/YTxlQPr4Ya0).

There is a readme in the zip, for running the scripts. I'll also include a copy below:

\### yubioathadd.bat ###
\
All this script does is add a TOTP OATH credential to the OATH storage on a plugged in Yubikey.
\
Requires you to have Yubikey Manager installed in a Windows Environment in the default location. If your install is somewhere else, please change the file path in the first line of the script after cd to your install location.
\
If you run the script by default without changing it, it will add a TOTP credential with name "YourNameHere" with the secret JBSWY3DPEHPK3PXP. To view the newly added credential in the Yubico Authenticator GUI, you must restart Yubico Authenticator OR reinsert it in order for it to re-read the OATH credentials from the secure element on the device. You can edit these values by editing the script, or dynamically entering them by modifying the file in the program that is calling them.
\
You can also change the algo or other values by adding any of the following options before the name field:
\
`-o, --oath-type [HOTP|TOTP]     Time-based (TOTP) or counter-based (HOTP)
`\
`credential.  [default: TOTP]
`\
`-d, --digits [6|7|8]            Number of digits in generated code.
`\
`[default: 6]
`\
`-a, --algorithm [SHA1|SHA256|SHA512]
`\
`Algorithm to use for code generation.
`\
`[default: SHA1]
`\
`-c, --counter INTEGER           Initial counter value for HOTP credentials.
`\
`-i, --issuer TEXT               Issuer of the credential.
`\
`-p, --period INTEGER            Number of seconds a TOTP code is valid.
`\
`[default: 30]
`\
`-t, --touch                     Require touch on YubiKey to generate code.
`\
`-f, --force                     Confirm the action without prompting.
`\
`-h, --help                      Show this message and exit.
`\

\
\### msfttotpadd.sh ###
\
This script will not work unless you give it a valid Bearer JWT token, SessionCtx, and Box.SessionCookie request cookies, saved as variables at the top of the script.
\
You will also need to have bash installed in your system, since Windows curl doesn't support --compressed. If you have git installed, git bash works and should automatically launch when the script is double clicked.
\
Fill in those fields with valid values and run the script. You will be prompted by the script to provide the OTP from a TOTP secret, the secret that is being added to the MS account. After providing the secret, a new TOTP credential with the secret just provided should be added to the MS account.