# Files

A file explorer app.

## Features

- Creating files.
- Renaming files.
- Moving files.
- Sending files to the trash.
- File navigation.
- Tabs.

## Screenshots

![image](https://user-images.githubusercontent.com/70250943/178124218-80de8500-c799-4e67-9457-a9da4b8a0f99.png)

## Supported Platforms

- Windows
- Linux

## Note for Linux Devices

"Send to Trash" functionality on Linux-based devices only works with
the `trash-cli` dependency. This was necessary because apparently no Java library has bothered to implement the FreeDesktop.org trash specification on Linux.

The dependency should be installable from your distribution's package manager,
or at the following GitHub page: https://github.com/andreafrancia/trash-cli
