# Terminal UI

!["A colour palette made with the Terminal UI"](/img/color-palette.png)

The Roguelike Starterkit includes a UI system specifically for roguelikes / Terminal graphics. It is build directly on top of Indigo's UI system, but snaps everything to a terminal grid.

Please refer to the examples for usage.

### Comments on the design

Indigo's UI system is designed to generically provide the minimum number of building blocks needed to build more complex ui systems. For example, there is no radio button primitive because you can build radio buttons out of a component list, where each entry is a component group, of a switch and a label.

Following on from that trend, there is no value in providing 'terminal' instances of all of Indigo UI's primitives, since not all of them are visually presented. For example, there is a `TerminalButton` because this is a visual element, but there is no 'terminal component group' because it's a layout tool, and has no visual aspect to it.

#### Rendering / Graphics

Being part of the Roguelike Starterkit, under the covers the Terminal UI is rendered using terminal emulators and character sheets.

To change the graphics of your UI, all you have to do is replace the appropriate characters on a sprite sheet, and feed it to the game / UI system.
