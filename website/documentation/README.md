# Roguelike Starter-kit

![Roguelike ascii art in Indigo](/img/roguelike.gif "Roguelike ascii art in Indigo")

The [Roguelike Starter-kit](https://github.com/PurpleKingdomGames/roguelike-starterkit) is a library for use with [Indigo](https://indigoengine.io/) to provide terminal-like rendering functionality specifically for ASCII art style games, and roguelike games in particular.

All the examples and demos in these docs are presented using [Indigo](https://indigoengine.io/).

## Installation

Check the repo for the [latest release number](https://github.com/PurpleKingdomGames/roguelike-starterkit/releases), and substitute the `x.y.z` below accordingly.

sbt

```
libraryDependencies += "io.indigoengine" %%% "roguelike-starterkit" % "x.y.z"
```

Mill

```
def ivyDeps = Agg(ivy"io.indigoengine::roguelike-starterkit::x.y.z")
```

Scala-CLI

```
//> using dep io.indigoengine::roguelike-starterkit:x.y.z
```
