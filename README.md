# Advanced Whitelist

<!-- modrinth_exclude.start -->
### - [Download on Modrinth](https://modrinth.com/plugin/advanced-whitelist) -
<!-- modrinth_exclude.end -->

Like a normal whitelist, but with powerful extra features to improve your Minecraft server experience. Players can send join requests directly when trying to connect, making access management easier and more interactive.

Administrators can review and accept requests instantly, keeping full control while reducing manual effort.

## Advanced Features

<details>
<summary>Request System</summary>

Players send a join request to an administrator on the server (if available) when they try to join. The administrator can accept the request directly in the chat and add the player to the whitelist.

![Request System](https://cdn.modrinth.com/data/cached_images/2024b80742f621e5b98712efb03651235dabb14a.webp)

</details>

<details>
<summary>Discord Integration</summary>

Users can add a Discord bot using a token to manage the whitelist directly from their Discord server via bot commands.  
The available commands are the same as on the Minecraft server.

![Discord Integration](https://cdn.modrinth.com/data/cached_images/9c1159d5e201e5ca6dc94fc6f290075d6ee24afd.webp)

</details>

## Commands

| **Command** | **Permission** | **Description** |
| --- | --- | --- |
| `/whitelist <add/remove> <player>` | `advancedwhitelist.player` | Add or remove a player from the whitelist |
| `/whitelist list` | `advancedwhitelist.list` | Show all whitelisted players |
| `/whitelist toggle/on/off` | `advancedwhitelist.toggle` | Enable or disable the whitelist |
| `/whitelist set` | `advancedwhitelist.player` | Clear the current list and add all online players |
| `/whitelist requests` | `advancedwhitelist.requests` | Show all pending join requests |

## Permissions

- `advancedwhitelist.admin` → Permission to manage all features  
- `advancedwhitelist.notify` → Permission to receive join requests in the chat  

## Configuration

| **Value** | **Description** | **Default** |
| --- | --- | --- |
| `Language` | Change the plugin language (supported: English, German) | `en` |
| `RequestsExpiration` | Time in minutes until a join request expires | `10` |

### Discord Bot Integration

| **Value** | **Description** | **Default** |
| --- | --- | --- |
| `Enabled` | Enable or disable the Discord bot | `false` |
| `Token` | Discord bot token | `X` |

## How to Create and Set Up a Discord Bot

<details>
<summary>Create a Discord Application</summary>

1. Go to the **Discord Developer Portal**  
   https://discord.com/developers/applications  
2. Click **New Application**.
3. Enter a name for your bot (e.g. `Advanced Whitelist`).
4. Click **Create**.

</details>

<details>
<summary>Create the Bot User</summary>

1. Open **Bot** in the left menu.
2. Click **Add Bot** and confirm.
3. (Optional) Upload a bot icon and change the username.
4. Enable the following **Privileged Gateway Intents**:
   - Presence Intent  
   - Server Members Intent  
   - Message Content Intent  
5. Click **Save Changes**.

⚠️ These intents are required if your bot reads messages or processes commands.

</details>

<details>
<summary>Copy the Bot Token</summary>

1. In the **Bot** section, click **Reset Token**.
2. Copy the generated token.
3. Store it securely – never share it publicly.

→ The token is the password for your bot.

</details>

<details>
<summary>Invite the Bot to a Discord Server</summary>

1. Go to **OAuth2 → URL Generator**.
2. Select the scope `bot`.
3. Choose one of the following permissions:
   - **Administrator** (recommended)
   - or only the permissions your bot needs
4. Copy the generated URL.
5. Open it in your browser and authorize the bot.

⚠️ You need the **Manage Server** permission.

</details>

<details>
<summary>Insert the Token into the Config File</summary>

### Example: `config.yml`

```yaml
Discord-Bot:
  Enabled: true
  Token: "YOUR_BOT_TOKEN_HERE"
```

Save the file and restart the server.

</details> 

<details> 
<summary>Start the Bot</summary>

Once the Minecraft server is running, the bot will appear online on your Discord server.

</details>

If you need help or want to suggest new features, feel free to join our Discord:
-> https://discord.blackninja.live
