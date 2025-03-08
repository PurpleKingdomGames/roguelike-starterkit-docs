# Artwork

![The Zilk 16x16 tile sheet from the dwarf fortress repository](/img/Zilk_16x16.png "The Zilk 16x16 tile sheet from the dwarf fortress repository")

## Finding artwork

One of the great things about roguelikes is that they're usually ASCII art, and there is a wealth of available art "packs" that were created for the well known roguelike, [Dwarf Fortress](https://en.wikipedia.org/wiki/Dwarf_Fortress).

**This is excellent news for programmers!**

You can go ahead and build a game and it will look ...exactly like all the other ones! The quality of your game will be judged on the strength of your ability to code up a world, not on your ability to draw trees and people. Perfect!

This starter pack takes a Dwarf Fortress image that looks like this:

![A dwarf fortress tile map](/img/Anikki_square_10x10.png "A dwarf fortress tile map")

([There are lots more of them to choose from!](https://dwarffortresswiki.org/Tileset_repository))

The project then uses custom shaders that allow you to set the foreground and background colours to render your world based on any of the standard format tile sheets you can find / make. You can even use lighting effects, if you like.

## Making artwork

### Additional fonts

If all you need is the extended ASCII set in a different font, look into Indigo's font generators (build settings) and chose the indexed grid layout with a max line length of 16 and the extended ASCII set. This will NOT (most like) give you nice characters for all the walls and so on, but it will produce a tile sheet compatible with the roguelike starter kit, in a custom font, with all the usual characters in place.

### Custom tile sheets

For a fully custom graphical style you'll need to roll up your sleeves and use an editor! It is worth mentioning that you are not limited to black and white though, here's a custom sheet for full color graphics from a talk.

![A full colour tilesheet](/img/colour-tilesheet.png "A full colour tilesheet")
![A demo using the full colour tilesheet](/img/lighting_added.png "A demo using the full colour tilesheet")
