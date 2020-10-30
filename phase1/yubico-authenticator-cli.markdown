---
title: Yubico Authenticator CLI
date: 2020-10-30 04:45:00 -05:00
position: 1
---

# Yubico Authenticator CLI

By Caleb Jiang

This information is for Windows x64 only. Installation may differ for other OSs, but once the CLI is installed, everything else \*should\* work the same.

First download and install Yubikey Manager. [Windows x64 download](https://developers.yubico.com/yubikey-manager-qt/Releases/yubikey-manager-qt-latest-win64.exe)

By default, the software should install into C:\\Program Files\\Yubico\\YubiKey Manager

The CLI can simply be accessed via the command prompt by changing the directory to C:\\Program Files\\Yubico\\YubiKey Manager and invoking **ykman**

[Official CLI documentation](https://support.yubico.com/hc/en-us/articles/360016614940-YubiKey-Manager-CLI-ykman-User-Manual)

![ykman.png](/uploads/ykman.png)

## How Yubico Authenticator interfaces with ykman

Yubico Authenticator interfaces with ykman whenever it is launched, a Yubikey is inserted, or a credential is modified. (added or deleted) Because of this, any modifications to the credentials stored on the physical Yubikey will not be reflected in the application until one of the above actions (application restarted, Yubikey reinserted, or credential added) is taken. 

## Managing and modifying the stored credentials

**ykman oath add (name)** for adding a new credential. You'll be prompted for the secret key after invoking the command.

**ykman oath delete (name)** for deleting an existing credential. You'll be prompted for confirmation, type y to confirm.

**ykman oath list** for a list of the names of all stored credentials.

**ykman oath code** for a list of the names and current TOTP codes of all stored credentials.

## Accessing the CLI programmatically

Credentials can be added and accessed programmatically by writing and calling a batch file. For example in Java, you can use **Runtime. getRuntime(). exec(batchFileName, null, new File("."));** to run a batch file. From the batch file, you can **cd C:\\Program Files\\Yubico\\YubiKey Manager** and run the necessary commands to add a credential or grab codes from there. You can pass in the necessary parameters and read the output with **process.waitFor()** and **BufferedReader** to get the output.