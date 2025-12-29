# Advanced Whitelist

Like a normal whitelist, but with powerful extra features to improve your Minecraft server experience. Players can send join requests directly when trying to connect, making access management easier and more interactive.

Administrators can review and accept requests instantly, keeping full control while reducing manual effort.

---

## Advanced Features

<details>
<summary>Request System</summary>

Players send a join request to an administrator on the server (if available) when they try to join. The administrator can accept the request in the chat and add the player to the whitelist.

![Request System](https://cdn.modrinth.com/data/cached_images/2024b80742f621e5b98712efb03651235dabb14a.webp)

</details>   

<details>
<summary>Discord Integration</summary>

A User can add a Discord Bot with an Token, to mange the whitelist from their Discord server with bot-commands. Its the same commands like on the Minecraft server.

![Discord Integration](https://cdn.modrinth.com/data/cached_images/9c1159d5e201e5ca6dc94fc6f290075d6ee24afd.webp)

</details>  

---

## Commands

| **Command** | **Permission** | **Description** |
| --- | --- | --- |
| /whitelist <add/remove> <Player> | `advancedwhitelist.player` | Add/Remove player to the whitelist |
| /whitelist list | `advancedwhitelist.list` | Show all whitelisted player |
| /whitelist toggle/on/off | `advancedwhitelist.toggle` | Enable/Disable the whitelist |
| /whitelist set | `advancedwhitelist.player` | Clear the current list and adds all online player  |
| /whitelist requests | `advancedwhitelist.requests` | Show all pending requests by players |

---

## Permissions

`advancedwhitelist.admin` → Permission to mange all features

`advancedwhitelist.notify` → Permission to get player requests in the chat

---

## Configuration

| **Value** | **Description** | **Default** |
| --- | --- | --- |
| `Language` | Change the language of the plugin. (Supported languages: English, German) | `en` |
| `RequestsExpiration` | Change the time when the request from a player expired. | `10` |

### Discord Bot Integration

| **Value** | **Description** | **Default** |
| --- | --- | --- |
| `Enabled` | Change whether the Discord bot should be used | `false` |
| `Token` | Add a the token for the Discord Bot.  | X |

---

## How to Create and Set Up a Discord Bot

<details>
<summary>1. Create a Discord Application</summary>

1. Go to the **Discord Developer Portal**:

        https://discord.com/developers/applications

    2. Click **“New Application”**.
    3. Enter a name for your bot (for example: `Advanced Whitelist`).
    4. Click **Create**.

</details>

<details>
<summary>2. Create the Bot User</summary>

1. In the left menu, open **Bot**.
    2. Click **“Add Bot”** and confirm.
    3. (Optional) Upload a **bot icon** and change the **username**.
    4. Under **Privileged Gateway Intents**, enable:
        - Presence Intent
        - Server Members Intent
        - Message Content Intent
    5. Click **Save Changes**.

   ⚠️ These intents are required if your bot reads messages, reacts to joins, or processes commands.
</details>

<details>
<summary>3. Copy the Bot Token</summary>

1. In the **Bot** section, click **“Reset Token”**.
    2. Copy the generated **Bot Token**.
    3. **Store it securely** – never share this token publicly.

   → The token is the password of your bot. Anyone with it can fully control your bot.

</details>

<details>
<summary>4. Invite the Bot to a Discord Server</summary>

1. Go to **OAuth2 → URL Generator**.
    2. Under **Scopes**, select `bot`
    3. Under **Bot Permissions**, choose one of the following:
        - **Administrator** (recommended)
        - or select only the permissions your bot needs (e.g. Send Messages, Use Slash Commands)
    4. Copy the **Generated URL** at the bottom.
    5. Open the URL in your browser.
    6. Select your Discord server and click **Authorize**.

   ⚠️ You must have the **“Manage Server”** permission on the server.
</details>    

<details>
<summary>5. Insert the Token into the Config File</summary>

Depending on your project, the token is usually placed in a config file.

### Example: `config.yml`

  ```yaml
  Discord-Bot: # The Discord Integration for Manging the Whitelist
  Enabled: true # Enable Discord Bot
  Token: "YOUR_BOT_TOKEN_HERE" # Discord Bot Token
  ```

After inserting the token save the file and restart the server.

</details>

<details>
<summary>6. Start the Bot</summary>
    Once the Minecraft Server is running the bot will appear **online** in your Discord server.
</details>

---

If you need any help or want to share some ideas to add, just hop on our Discord ([discord.blackninja.live](http://discord.blackninja.live))

