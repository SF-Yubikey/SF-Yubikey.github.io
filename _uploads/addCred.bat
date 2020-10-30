cd C:\Program Files\Yubico\YubiKey Manager
set /p name=What is the name of the new credential?: 
ykman oath add %name%
set /p asd="Press enter to close."