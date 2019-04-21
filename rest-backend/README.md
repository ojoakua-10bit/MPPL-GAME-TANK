# rest-backend

Backend for REST API for tank game.

## Documentation

### Authentication

Always provide these information on HTTP header when using our API. Otherwise your request will be rejected.

Field | Type | Description
----|----|----
App-Token | 64-characters string | Token for identifying the requesting application.
User-Token | 64-characters string | Token for identifying the requesting user. 

There are two kinds of App-Token: ADMIN and PLAYER. ADMIN token is used on web interface on server. PLAYER token is used on game client. 
If you use ADMIN token, `User-Token` field is not needed.

You can obtain a `User-Token` by sending these information via multipart form. Only `App-Token` is needed in this process.

WARNING: Requesting a `User-Token` expires the old `User-Token` for the same player. 

#### `POST /auth`

##### Required Parameters

Field | Type | Description
----|----|----
username | string | Username of a player.
password | string | Password of a player.

##### Response Structure

Field | Type | Description
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
user_token | 64-characters string | Requested user token.
user_data | [Player](#player-object) object | A Player object based on requested username.

### Player Object
Represents a player information.

#### Player Structure
Field | Type | Description
----|----|----
player_id | string | Unique id of this player.
username | string | Unique username that can identify the player. Username can be changed as long as it hasn't been used. 
player_name | string | Name of this player. Can be real name or alias.
xp | integer | Number of XP points that this player accumulate.
rank | integer | Rank of this player. Based on this player's XP points.
diamond_count | integer | Number of player's diamond.
gold_count | integer | Number of player's gold.
inventory | integer | Current player's inventory capacity.
avatar | string | Path to user's avatar.
online_status | boolean | Whether this user online or not.
ban_status | boolean | Whether this user is banned or not.

#### Example of Player Object
```json
{
    "player_id": "0d89db5aae3bf7797def",
    "username": "anko",
    "player_name": "Ichinomiya Anko",
    "xp": 23750,
    "rank": 4,
    "diamond_count": 87,
    "gold_count": 11300,
    "credit_balance": 169000,
    "inventory": 15,
    "avatar": "/static/images/0d89db5aae3bf7797def-anko.png",
    "online_status": false,
    "ban_status": false
}
```

