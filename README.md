# Numismatics Calculator

# What This Does

This is a client-side forge mod that adds calculator functionality to coin items from the mod `"Create: Numismatics" by IThundxr`.  

This is useful because those coins have values like `1`, `8`, `16`, `64`, `512` and those are VERY hard to calculate for random sums like "75" if you have those coins as the main economy on an SMP and you trade everything for those.  

With the calculator you won't need to struggle with your system calculator, since this one is built into your game and has a lot of functionality specifically for this purpose.  

# Usage And Functionality

Video Showcase:

[![Video Showcase](images/videoShowcaseThumbnail.png)](https://www.youtube.com/watch?v=ALnLrNNucFA)

## Total Slot Value

![Total Slot Value](images/tooltip.png)

By holding down `SHIFT` (can be configured) you can see the total value of every single slot with a coin.  

Remember, that you can check if you have the right sum available to pay someone with the `Selection` feature from the `Summation Tab` in the `Main Overlay`.  

## Control Panel

When opening your inventory, you'll see the mod's **Control Panel**.  

![Control Panel](images/controlPanel.png)

It lets you toggle the visibility of the **Main Overlay** and open mod's settings  

Both actions are available as keybindings too:

![Keybindings](images/keybindings.png)

With those you can always toggle the `Main Overlay` with a key right from your inventory, even without the `Control Panel` visible.  

Additionally you can open the mod's `Config Screen` with a key too.  
  
The Main Overlay contains **two tabs** visible on the top:  

![Tabs](images/postitionAnimation.gif)
They even have a smooth position animation! (and it's configurable)

## Summation Tab

![Summation Tab](images/sumMode.png)

In the first tab you have a text displaying your total coins **in the inventory**, and coins in the selected area.  

You can toggle selection mode with the "Select button".  

It even **works in CHESTS** and all other containers:
![Summation Tab](images/chestSum.png)

## Conversion Tab

![Conversion Tab](images/conversionModeDefault.png)

In the second tab you can enter an amount of `¤` to actual coins. By default, it'll tell you a general coin composition as if you had infinite coins.

Since the original mod has a coin type for `1 ¤`, there is never expected any sum in return.  

And there is a second mode accessible with the `Only available` checkbox:

![Only available Mode](images/conversionModeAvailable.png)

This mode will only work with coins in your inventory and tell you if you don't have enough.

"Only Available" mode will take into account the cases when you **have to OVERPAY or you DON'T HAVE ENOUGH**:

![Overpaying](images/overpaing.png)

![Missing](images/missing.png)

## Mod Config Screen

![Mod Config Screen](images/config.png)

By pressing the second button in your `Control Panel` or just by pressing a configured keybind, you can open the mod's `Config Screen`. Here you can toggle features on and off.  

# Notes

Current known bugs:

- None! :D

Feel free to let me known if you encounter any.

# Supported Versions

- 1.20.1 - Forge, Fabric (and Quilt)  
- `1.21.1 - Coming Soon (NeoForge)`  

# Building From Source Code

*Read extra notes below too  

- Launch your installed [Intellij IDEA](https://www.jetbrains.com/idea/) (Community Edition will work just fine)  
- File -> New -> Project from Version Control -> paste `https://github.com/DaylightDragon/NumismaticsCalculator.git` in there, or just clone the repository in any other way  
- Select JDK 23 (read below\*), [Adoptium Temurin JDK](https://adoptium.net/temurin/releases?version=23&os=any&arch=any&mode=filter) recommended  
- Let `Gradle` load/sync the project's configuration. Or manually start it by finding the `Gradle` icon (elephant) on the right and pressing "Sync all Gradle projects" (2 arrows)  
- Make sure the bar on the bottom right disappeared after loading everything  
- In `Gradle` tasks now use `forge / fabric -> Tasks -> loom -> runClient` and to run the game and `forge / fabric -> Tasks -> build -> build` to create a jar in `/(forge/fabric)/build/libs/<...>.jar`.

## Extra notes:

The final version (Architectury branch) of the project has been using **GraalVm JDK 23 with language level 17**.  
So I'm not sure what else the project runs ideally on, haven't check.  
