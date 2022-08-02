# AthenaProtection
Simple 2FA Protection to Avoid Griefer on Your Server.

Usage :

- /access [key]
To Access


Default Config

```
reload:
  permission: athenaprotection.reload
  message:
    - "&eAthena Protection Plugin Reloaded!"

##Set your default key here in-case your staff forgot about the access key.
default-key: "AthenaProtection"

##Set your permission detect here. This is important to detect staff permission.
##This permission will give access to all AthenaProtection command.
permission-detect: "essentials.jail"

##Checking if player granted permission in-game (Will check every 1 seconds). If player has permission on permission-detect it will
##automically cast the Protection (Example: CommandSync Exploit that can give permission after joining the server)
##I recommend this to always set TRUE
permission-grants-detect: true
```
