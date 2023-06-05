Tested in Ubuntu 20.04 and Macos Big Sur 11.6

Keep in system tray

![image](https://user-images.githubusercontent.com/48907268/162616170-4461714c-0716-444b-9a37-7af1db50dcf6.png)

Main screen

![image](https://github.com/zemua/media/blob/main/Screenshot%202022-04-10%20at%2013.37.15.png)

Categorize processes as positive (sums points) or negative (uses points until reaching 0, then get blocked)

<img width="992" alt="image" src="https://user-images.githubusercontent.com/48907268/162616367-7d6aae58-50c3-4945-b7df-871847408c27.png">

Processes categorized as "Depends" can be categorized into "positive" or "negative" by the window title

<img width="628" alt="image" src="https://user-images.githubusercontent.com/48907268/162616476-b04cdee3-bc2b-4329-b148-57ca55817100.png">

You can create positive groups and negative groups, that can contain certain apps that you choose

<img width="995" alt="image" src="https://user-images.githubusercontent.com/48907268/162616515-70b7f8af-a56a-424f-af44-e86f8599f100.png">

<img width="992" alt="image" src="https://user-images.githubusercontent.com/48907268/162616532-fa30c729-377c-4724-8b90-18efff20b89b.png">

<img width="990" alt="image" src="https://user-images.githubusercontent.com/48907268/162616556-c6c20ccc-2ec7-4c44-bd02-0303972cc846.png">



You can add time to this group from external apps using synchronized txts, like from the Android app

<img width="993" alt="image" src="https://user-images.githubusercontent.com/48907268/162616585-28a38b76-1b39-433a-b2de-7fb21c8c908e.png">


Then you can add conditions for each group, if the conditions are not met, positive groups would not sum points, and negative groups would get directly blocked

<img width="991" alt="image" src="https://user-images.githubusercontent.com/48907268/162616626-14058276-0406-4766-a421-d66e49f7ac83.png">


If some apps categorized as "depends" are tools that you use for work, you can prevent them from closing even if you have a tab open with a title that is negative, so you don't lose your work.

<img width="994" alt="image" src="https://user-images.githubusercontent.com/48907268/162616668-8a6b4d4e-d4ec-4ec6-a512-0b1d02c1f818.png">


Among other options, choose the proportion of time you need to "work" in order to get 1 minute of "playing". For example default proportion 4 would require that you work 4 hours to get 1 hour of playing.

Select a lockdown time where any app would decrease points and negative apps would directly get closed, handy for going to sleep when you should.

Receive notifications when time is running out or you are approaching the lockdown time.

<img width="994" alt="image" src="https://user-images.githubusercontent.com/48907268/162616702-932397cc-66da-4075-8a6e-b70ef853967f.png">




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
java -Dapple.awt.UIElement="true" -jar /Users/username/TurkeyDesktop/TurkeyDesktop-1.0-SNAPSHOT-jar-with-dependencies.jar
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

WARNING: After the update to Mac's Ventura 13.4 I got problems having my machine run correctly the app on startup, that is because Mac's launchctl seems not to be very friendly with shell scripts, and the permission window for accesibility never pops up, and so Turkey app cannot fetch window titles. To fix this the launcher has to be a binary file, you can compile the script to binary with the following:
```
brew install shc
shc -f /Users/username/TurkeyDesktop.sh
mv /Users/username/TurkeyDesktop.sh.x /Users/username/TurkeyLauncher
```
And modify your devs.mrp.turkeydesktop.plist to point to /Users/username/TurkeyLauncher instead of /Users/username/TurkeyDesktop.sh



To run it in Ubuntu at startup

create a script .sh file with this content (you can do the same SDK_MAN stuff as above)
```
#!/bin/bash
java -jar ~/TurkeyDesktop-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Give this script file permissions to run as an application
Then search for "startup apps" application and add a new command
```
/home/username/initTurkey.sh
```

# Ubuntu

For this to work in Ubuntu you need to install the following

```sudo apt-get install xprintidle```
```sudo apt-get install xdotool```

xdotool works with X.org so if you have installed some recent version of Ubuntu you may probably be running Wayland and the program will not be able to detect the current window. Currently there are no substitutes for xdotool that work with Wayland, only partially https://github.com/atx/wtype and https://github.com/ReimuNotMoe/ydotool can perform some keyboard/mouse actions.

You can still choose to use X.org from the login screen in the configuration cog, details here: https://itsfoss.com/switch-xorg-wayland/

# Mac

To disable the application from closing instantly in Mac when you have the window focused and press "command+q" you can use the following workaround:
Go to System Preferences, then Keyboard. Select Shortcuts. On the left pane, find Accessibility. Now on the right pane enable Invert colors and then click on the shortcut box to reassign the shortcut to Cmd+Q
This may also save you a lot of pain when you have several tabs open in your browser or your PDF reader for example.
