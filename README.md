# Simple Mention

A Minecraft Fabric mod that adds mention functionality to chat using `@username` syntax.

## Features

- **Mention Detection**: Automatically detects `@username` mentions in chat messages
- **Player Suggestions**: Auto-complete player names when typing `@`
- **Customizable Formatting**: Choose who sees formatted mentions
- **Sound Notifications**: Play sounds when mentioned
- **Desktop Notifications**: Show system tray notifications when mentioned
- **Multi-language Support**: English and French translations included

## Configuration

The mod can be configured through ModMenu or by editing the config file directly.

### Options

- **Enabled**: Enable or disable the Simple Mention feature
- **Play Sound**: Play a sound when mentioned in chat
- **Desktop Notification**: Show a desktop notification when mentioned
- **Only Mentioned Players See Formatting**: When enabled, only mentioned players will see colored mentions. When disabled, all players see formatted mentions.
- **Ignore Own Mentions**: When enabled, players won't receive notifications when they mention themselves

## How It Works

### Mention Formatting

By default, only the mentioned player sees the formatted (colored) mention. Other players see the mention as plain text. This can be changed in the configuration.

**Example:**
- Player A sends: "Hey @PlayerB, check this out!"
- Player B sees: "Hey **@PlayerB** (colored), check this out!"
- Player C sees: "Hey @PlayerB, check this out!" (plain text)

### Player Suggestions

When typing `@` in chat, the mod will suggest all online players. Simply click on a suggestion or use arrow keys to select.

### Notifications

When a player is mentioned, they receive:
1. A chat message notification
2. A sound (if enabled)
3. A desktop notification (if enabled)

## Installation

1. Install Fabric Loader for Minecraft 1.20.1
2. Install Fabric API
3. Download and install this mod
4. (Optional) Install ModMenu for in-game configuration

## Dependencies

- Fabric Loader (>=0.16.14)
- Fabric API
- Minecraft 1.20.1
- Java 17+

## Development

### Building

```bash
./gradlew build
```

### Running

```bash
./gradlew runClient
```

## License

MIT License - see LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Changelog

### Version 1.0.0
- Initial release
- Basic mention functionality
- Sound and desktop notifications
- Player suggestions
- Configurable formatting behavior 