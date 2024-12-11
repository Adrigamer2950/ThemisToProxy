# ThemisToProxy
<p align="center">
    <img src="https://raw.githubusercontent.com/Adrigamer2950/ThemisToProxy/refs/heads/master/logo.png" height="200" alt="Logo" />
</p>

Get Themis' notifications from anywhere in your network

Available for **Bukkit/Spigot/Paper** and **Velocity** (**BungeeCord** support soon)

![Build](https://github.com/Adrigamer2950/AdriAPI/actions/workflows/build.yml/badge.svg)

# Installation
- Install the plugin in your proxy and backends that use [**Themis Anti Cheat**](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-21-bedrock-support-paper-compatibility-free-optimized.90766/)
- Restart your proxy and backends
- You're done! Every player that has permission to see Themis' notifications will receive a notification
  if a player gets flagged in any server.

# Config
Only available in the proxy. Default config:
```yaml
message-format: "§6%server% §8| §r%message%"
```