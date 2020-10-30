---
title: Yubico Authenticator CLI
date: 2020-10-30 04:45:00 -05:00
position: 1
---

By Caleb Jiang

This information is for Windows x64 only. Installation may differ for other OSs, but once the CLI is installed, everything else \*should\* work the same.

First download and install Yubikey Manager. [Windows x64 download](https://developers.yubico.com/yubikey-manager-qt/Releases/yubikey-manager-qt-latest-win64.exe)

By default, the software should install into C:\\Program Files\\Yubico\\YubiKey Manager

The CLI can simply be accessed via the command prompt by changing the directory to C:\\Program Files\\Yubico\\YubiKey Manager and invoking **ykman**

[Official CLI documentation](https://support.yubico.com/hc/en-us/articles/360016614940-YubiKey-Manager-CLI-ykman-User-Manual)

![ykman.png](/uploads/ykman.png)

## How Yubico Authenticator interfaces with ykman

Yubico Authenticator interfaces with ykman whenever it is launched, a Yubikey is inserted, or a credential is modified. (added or deleted) Because of this, any modifications to the credentials stored on the physical Yubikey will not be reflected in the application until one of the above actions (application restarted, Yubikey reinserted, or credential added) is taken. 

## Modifying the stored credentials