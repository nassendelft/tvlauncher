# Custom Android TV launcher

This project aims to create a new launcher for Android TV.

## Status

Although you can already install and use it, it is very much early stage.
Currently it only displays a grid of application banners with a very crude selection state.
It can only open any application that has
[specific Android TV functionality only](https://developer.android.com/training/tv/start/start#leanback-req).
But this might change as we go further in the project.

## Features

As mentioned this project is in it's infancy so the list is quite short.
Here are the current features:

- Home grid to start an application
- Open system settings from home screen
- Automatic app update and start the update installation from within the app

### New features

Check the [Projects](https://github.com/nassendelft/tvlauncher/projects/1) page to see what we're
currently working on or the [Feature discussion](https://github.com/nassendelft/tvlauncher/discussions/1)
to see what could be in the future.

## Installation

Don't care about the technicalities and just want to install it?
Use the [installer](https://github.com/nassendelft/tvlauncher-installer/releases) to download and
install the latest version then setup your device for a custom launcher.

## Advanced installation

If you are familiar with, and know how to use, [ADB](https://developer.android.com/studio/command-line/adb)
you can simply run these 3 steps to get you started:

1) Download the [latest release APK](https://github.com/nassendelft/tvlauncher/releases)
2) Install the APK: `adb install launcher-vx.x.x.apk`
3) Change the default launcher on your device

Note: If your device doesn't support changing launchers you can disable the default one by calling
`adb shell pm disable-user com.google.android.tvlauncher`.
If you want to enable the default launcher again you can call
`adb shell pm enable com.google.android.tvlauncher`.

# Contributing

There are multiple way to contribute to this project, not just by adding code, you can also
report bugs or participate in discussions.

## Report bugs

If you see any bugs like crashes, incorrect shown data or just broken UI like missing animations
you can report them in [Issues](https://github.com/nassendelft/tvlauncher/issues). Feel free
to report anything you feel is needed. Include the version you are using and a description of the
problem your facing and it'll be picked up as soon as possible.

## Submit Pull Requests

This project is open for any [Pull Requests](https://github.com/nassendelft/tvlauncher/pulls).
Whether it be bug fixes or new features. Please try to match the code style of the project as
much as possible. Other then that there are no real rules for them (for now anyway).

## Participate in the Discussions

[Discussions](https://github.com/nassendelft/tvlauncher/discussions) are great way to start
conversations about the project. Here we can talk about anything related about the project
like the direction it should take, technical details about the implementation, etc.
