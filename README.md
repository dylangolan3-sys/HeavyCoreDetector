# Heavy Core Detector

A Fabric mod for Minecraft 1.21.1 that automatically opens Ominous Vaults when a Heavy Core item is detected inside.

## Features

- ✅ Scans nearby Ominous Vaults every client tick (minimal cooldown)
- ✅ Detects Heavy Core items in vault inventory
- ✅ Automatically opens vaults containing Heavy Core
- ✅ Handles fast item cycling as vaults rotate items
- ✅ 16-block detection radius

## Installation

1. Download the latest JAR from releases
2. Place it in your `.minecraft/mods/` folder
3. Launch Minecraft with Fabric loader

## Building from Source

```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`

## How It Works

The mod:
1. Hooks into client tick events
2. Scans for Ominous Vault block entities nearby
3. Checks the vault's NBT data for Heavy Core items
4. Auto-interacts with the vault to open it

## License

MIT
