# Numismatics Calculator

## What This Does

This is a client-side forge mod that adds calculator functionality to coin items from the mod "Create: Numismatics" by IThundxr.  
This is useful because those coins have values like `1`, `8`, `16`, `64`, `512` and those are VERY hard to calculate for random sums like "75" if you have those coins as the main economy on an SMP and you trade everything for those.  
With the calculator you won't need to struggle with your system calculator, since this one is built into your game and has a lot of functionality specifically for this purpose.  

## Usage And Functionality

(TODO)

## Supported Versions

Currently only Forge 1.20.1

## Building From Source Code

- Launch your installed [Intellij IDEA](https://www.jetbrains.com/idea/) (Community Edition will work just fine)  
- File -> New -> Project from Version Control -> paste `https://github.com/DaylightDragon/NumismaticsCalculator.git` in there, or just clone the repository in any other way  
- Select JDK 17, [Adoptium Temurin JDK](https://adoptium.net/temurin/releases?version=17&os=any&arch=any) recommended  
- Let `Gradle` load/sync the project's configuration. Or manually start it by finding the `Gradle` icon (elephant) on the right and pressing "Sync all Gradle projects" (2 arrows)  
- Make sure the bar on the bottom right disappeared after loading everything  
- In `Gradle` tasks run `forgegradle runs` -> `genIntellijRuns`.
- Now, **to build**, run task `build` -> `build` to compile the project into a jar in `/build/libs/<...>-all.jar`  
Or, to run in **dev environment**, find "runClient" run configuration on top, select it and execute it with the run/debug button. Make sure you generated your IDE runs.  
