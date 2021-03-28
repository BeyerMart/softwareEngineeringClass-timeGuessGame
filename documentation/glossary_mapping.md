| German Name | English Name | Description |
|:--- | :--- | :--- |
|Würfel| cube |The cube is the device connected via Bluetooth to a raspberry pi, it can be rolled and there is a mapping between cube side and points / task. Needs to be connected to a room. One is needed per game.
|RaspberryPi |RaspberryPi | A small computer getting all the data from the cube and sending it to the backend
|Thema | theme| A category of words
|Begriff | item | A single word which belongs to a theme and will need explanation
|Team | team | A group of players. There needs to be at least one user per team.
|Spieler | player | A person wanting to play / playing a game. Is not necessary logged in and has not digital device available
|Benutzer | user | Logged in player
|Spieleverwalter | manager | A user with additional privileges, like editing the items and themes
|Administrator | admin | A manager with additional privileges, like managing games and managers
|Spiel | game | A started room. _Not sure if needed_
|Spielraum | room | A game room. Teams can be created, users can join this room and the room can start
|SpieleAuswahl | room selection | This is a screen logged in users can see. All active / open rooms are listed here. In the task this is referred as "virtuelle Spielelobby", but we decided to not use the term "lobby", as a room could also be called lobby. 
|Erklären | explaining | The process of communicating an item to teammates. It is not defined in what way this is done.
|Aufgabe / Aktivität | task | The specific kind of explanation
|Pantomime | mime | Explain the item without talking. Only use your body
|Reim | rhyme| Explain the item only using other words, which rhyme with the given item
|Sprechen | speak | Explain the item in speech, but do not say it (neither a translation of it)
|Zeichnen | draw | Explain the item with a drawing. Do not speak at all.