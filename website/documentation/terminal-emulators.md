# Terminal Emulators

## The `TerminalEmulator` and the `RogueTerminalEmulator`

The terminal emulators are the foundation of the library, and provide a simple 'terminal-like' object you can use to represent grids of ASCII tiles. They come in two flavors, both of which have the same interface but behave differently:

1. The `TerminalEmulator` is the recommended default. It is safe and immutable and easy to use, perfect for getting started. The drawback of the `TerminalEmulator` is that the performance isn't brilliant, and at some point you may find it necessary to start caching results (typically in your view model).
2. The `RogueTerminalEmulator` is a more dangerous version of the `TerminalEmulator`! It is a mutable structure, and as such, has much better performance characteristics. As with all mutable data types, it must be handled with more care. Generally speaking though, you should be able to use it as a drop in replacement for the `TerminalEmulator` without the need for caching or even holding long term instances of the terminal object.

### Rendering with `CloneTiles`

To render the contents of a `TerminalEmulator` or a `RogueTerminalEmulator` we need to output `CloneTiles`.

Performance will vary by scene complexity, specifically how many unique colour combinations you have in place. In other words, if all your tiles are identical, the scene will be boring but rendering quickly. If every tile is unique then rendering will be slow(er).
