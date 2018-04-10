# Custom Menus

_JSON-base custom guis for Minecraft!_

Wondering what this is? Well, do you remember that propular mod caled CustomMainMenu?

CustomMainMenus was a mod that allowed you to change the title screen
 for Minecraft to contain anything you like.It worked by editing a handy json file it dumped somewhere.
It also made it possible to add your own screens as long as they were linked to by some button
on the title screen.

*long inhale*

Now _this_ does exactly that _and more_!

"Uh wuuuh!?" you say?

It's true! Custom Menus allows you to make changes to the game's title screen. But it _also_ extends that functionality to _any_ screen!

By default the mod contains json files for _almost_ every screen included in the game. They're fully functional; and fully customizable!

You can also add to them and create your own screens. Existing screens can include functions from other ones,
and even navigate to mod-added screens. The entire system is designed to be extensible, so modders are free to add their own elements/widgets/and actions.

But that's not all! You can control the window's title, _and_ all of this is loaded as part of the game's resourcepacks system, meaning
servers can specify custom interfaces!

----

## Documentation

Documentation will be coming along in a while, maybe a wiki (for the self-motivated).
The avid reader may also note the used of the word 'almost'. All attempts were made to support every part of the game,
however two (three if you include in-game interfaces) of them could not be supported. Those two are, namely,
the world creation screen, and the world customisation screen.

Everything else if free game, though!

---

## Installation

LiteLoader is required. After installing that, move the downloaded .litemod file into `.minecraft/mods`.


## Building
Requires jdk8, git, and optionally gradle.

Checkout this repository and build it.

```cmd
cd ..
git clone https://github.com/Sollace/CustomMenus
cd CustomMenus
./gradlew build
```
