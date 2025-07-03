# Buffered
Buffered is a mod building resource that provides an API and utilities for rendering custom shaders more efficiently in Minecraft.
It is designed for the Quilt mod loader and helps mod developers integrate shader-based effects without directly using Mojang's code.
This is heavily inspired by Veil which you can check out [here](https://github.com/FoundryMC/Veil).

## General Info
Similar to Veil, this mod is not actually one you download and place in ./mods, rather its a mod for mod developers to include in their own mods.
This isn't meant to be downloaded and played with, but rather its meant to be used when developing mods.

## Requirements
Quilt - 1.20.x (I believe, specifically ment for 1.20.4)
Java - 17 or above

## Installation / Usage (for developers)
Add the following Maven repository and dependency to your mod’s ```build.gradle```:

```groovy
repositories {
    maven { url 'https://m00nb0ygame8.github.io/Buffered/maven/' }
}

dependencies {
    modImplementation "me.moonboygamer:Buffered:${libs.versions.buffered.get()}"
}
```

And this in your mod's ```gradle/libs.versions.toml```:

```toml
[versions]
# ...
buffered = "1.0.2+1.20.4"
# ...
```
