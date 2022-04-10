Tested in Ubuntu 20.04 and Macos Big Sur 11.6

Keep in system tray
![image](https://user-images.githubusercontent.com/48907268/162616170-4461714c-0716-444b-9a37-7af1db50dcf6.png)



# Run At Startup

To run it in Macos create a .sh file with this content

```
#!/bin/zsh
## This is only if you have several sdk in your machine and you need to manage them
## see sdk_man for reference, you should have it installed
export SDKMAN_DIR="$HOME/.sdkman"
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java 11.0.13-zulu
## this is the only important part that runs the jar file
java -jar /Users/username/TurkeyDesktop/TurkeyDesktop-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Then go to ~/Library/LaunchAgents
create a file devs.mrp.turkeydesktop.plist
inside this file copy this adapted to your personal case

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Label</key>
        	<string>devs.mrp.turkeydesktop</string>
	<key>Program</key>
        	<string>/Users/username/TurkeyDesktop.sh</string>
        <key>RunAtLoad</key>
        	<true/>
        <key>KeepAlive</key>
        	<true/>
    </dict>
</plist>
```

To run it in Ubuntu at startup

create a script .sh file with this content (you can do the same SDK_MAN stuff as above)
```
#!/bin/bash
java -jar ~/TurkeyDesktop-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Then search for "startup apps" application and add a new command
```
/home/username/initTurkey.sh
```

