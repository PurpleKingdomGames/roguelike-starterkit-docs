# The Terminal Material

## ASCII Art Materials

Rendering ASCII art means setting three things:

1. The character (char)
1. The foreground color (implemented as a tint to retain shading)
1. The background color (which overrides a "mask" color with another color value - the mask color is magenta by default)

### `TerminalText`

This is a material for use with Indigo's standard `Text` primitive. In addition to foreground and background `TerminalText` also supports a solid colour dropshadow.

Looks great but has a glaring problem: Changing colors mid-artwork (e.g. setting the text red and the border blue) is a pain, you need to use another `Text` instance and make them line up!

Great for pop-up menus, and monochrome sections of ASCII art, or maps that aren't too big. After that, a new strategy may be needed.

### `TerminalMaterial`

This material is a near drop-in replacement for `TerminalText`. It does not support the drop shadow feature, but has a leaner implementation for performance sensitive contexts. Supposed to be used with the `TerminalEmulator`/`RogueTerminalEmulator`'s `toCloneTiles` method.

Additionally, `TerminalMaterial` will attempt to preserve tile colours. If you apply a foreground colour to a tile, it will only apply the tint to greyscale pixel values. This means you can have a tree with a brown trunk, but tint just the leaf colours to autumn reds, yellows, and greens.
