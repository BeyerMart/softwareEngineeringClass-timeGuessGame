This document provides a glossary with a translation between english and german terms relevant for this project. Sometimes alternative terms are used in the software they are noted in brackets.

| German Name | English Name | Description |
|:--- | :--- | :--- |
|Würfel| cube (die) |The cube is the  device connected via Bluetooth to a RaspberryPi, it needs to be configured so each facet can be mapped to a specific activity and available points. Needs to be connected to a room. One is needed per game.
|RaspberryPi |RaspberryPi | A small computer getting all the data from the cube and sending it to the backend. Configuration of the cube is done via command line in the cube application.
|Thema | topic | A category of words.
|Begriff | term | A single word which belongs to a topic and will need explanation.
|Team | team | A group of players. There needs to be at least one user per team and in total at least two players per team.
|Team an der Reihe | active team | The team which has to explain and guess a term. There can only be one active team at a time in a game.
|Spieler | player | A person wanting to play / playing a game. Is not necessary logged in and does not need to have a digital device
|Lokaler Mitspieler | Virtual Player | A player not logged in and playing on the same device as its corresponding user.
|Benutzer | user | Logged in player.
|Spieleverwalter | manager | A user with additional privileges, like editing the terms and topics.
|Administrator | admin | A manager with additional privileges, like managing games and managers.
|Spiel | game | A started room, in which multiple rounds are played
|Spielraum | room | A room for games. Teams can be created, users can join this room and the room starts a game. A room can also start another game, once the prior has finished. A RaspberryPi needs to be connected to this room. 
|Spielrunde | round | A round starts with one player (of the currently active team) picking a term, rolling the cube and explaining the term in the given manner. The other member(s) of that team need(s) to guess the term correctly in the given time. It ends with an opposite team accepting or declining the answer and thus accounting of points.
|Spielauswahl | room selection | This is acting as the home screen. Any visitor to the website can see this page. All active / open rooms are listed here. In the task this is referred as "virtuelle Spielelobby", but we decided to not use the term "lobby", as a room could also be called lobby. 
|Erklären | explain | The process of communicating an term to teammates. It is not defined in what way this is done.
|Aufgabe / Aktivität | task (Activity) | The specific kind of explanation
|Pantomime | pantomime | Explain the term without talking. Only use your body
|Reim | rhyme| Explain the term only using other words, which rhyme with the given term
|Sprechen | speak | Explain the term in speech, but do not say it (neither a translation of it)
|Zeichnen | draw | Explain the term with a drawing. Do not speak at all.