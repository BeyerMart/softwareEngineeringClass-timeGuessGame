export default {
    generic: {
        overview: 'Übersicht',
        error: 'Fehler',
        profile: 'Profil',
        back: 'Zurück',
        name: 'Name',
        confirmTitle: 'Sind Sie sicher?',
        confirmMessage: 'Sie können diese Aktion nicht rückgängig machen',
        yes: 'Ja',
        no: 'Nein',
        role: 'Rolle',
        universityOfInnsbruck: 'Universität Innsbruck',
        imprint: 'Impressum',
        tos: 'Nutzungsbedingungen',
        create: 'Erstellen',
        pleaseWait: 'Bitte warten',
        round: 'Runde',
        from: 'vom',
        join: 'Beitreten',
        date: 'Datum',
    },
    languages: {
        de: 'Deutsch',
        en: 'Englisch',
    },
    errors: {
        somethingWrong: 'Etwas ist schief gegangen',
        back: 'Zurück',
        notFound: 'Entschuldigung, diese Seite existiert nicht',
        internalserror: 'Ein unerwarteter Fehler ist aufgetreten',
        userNotFound: 'Benutzer konnte nicht gefunden werden',
        validation: {
            nameRequired: 'Name ist erforderlich',
            termNameRequired: 'Begriffsname ist erforderlich',
            topicRequired: 'Themengebiet is erforderlich',
            maxPointsIsRequired: 'Max. Punkte ist erforderlich',
            minMaxPointsRequired: 'Bitte geben sie einen Wert zwischen 5 und 10000 ein',
        },
        loginRequired: 'Login erforderlich',
    },
    signup: {
        signup: 'Registrieren',
        email: 'Email',
        username: 'Benutzername',
        password: 'Passwort',
        confirmPassword: 'Passwort bestätigen',
        createAccount: 'Benutzerkonto erstellen',
        accountExists: 'Haben Sie bereits ein Benutzerkonto?',
        errors: {
            emailRequired: 'E-Mail ist erforderlich',
            emailInvalid: 'E-Mail ist ungültig',
            usernameRequired: 'Benutzername ist erforderlich',
            passwordRequired: 'Passwort ist erforderlich',
            confirmPasswordRequired: 'Passwort bestätigen ist erforderlich',
            passwordsMatch: 'Passwörter müssen übereinstimmen',
            passwordMinLength: 'Das Passwort muss aus mindestens {minLength} Zeichen bestehen',
        },
        messages: {
            signupSuccess: 'Registrierung erfolgreich! Bitte anmelden',
        },
    },
    game: {
        virtualUser: 'Lokaler Spieler',
        team: 'Team',
        teamName: 'Teamname',
        enemy: 'Gegner',
        points: 'Punkte',
        score: 'Punktestand',
        topic: 'Themengebiet',
        createdAt: 'Erstelldatum',
        selectTopic: 'Themengebiet auswählen',
        createGame: 'Spiel erstellen',
        createNewRoom: 'Neuen Raum erstellen',
        createRoom: 'Raum erstellen',
        room: 'Spielraum',
        roomName: 'Raumname',
        numOfPlayers: 'Anzahl der Spieler',
        numOfTeams: 'Anzahl der Teams',
        games: 'Spiele',
        teams: 'Teams',
        leaveGame: 'Spiel verlassen',
        activity: 'Aktivität',
        winnerTeam: 'Gewinnerteam',
        messages: {
            youHaveBeenKicked: 'Der Host hat dich aus dem Raum entfernt!',
            roomCreateSuccess: 'Raum wurde erfolgreich erstellt',
            roomAlreadyFull: 'Der Raum ist bereits voll',
            youJoinedRoom: 'Raum erfolgreich beigetreten',
            youLeftGame: 'Spiel erfolgreich verlassen',
            roomRemoved: 'Der Raum wurde vom Host gelöscht.',
            userJoined: '{userName} ist beigetreten',
            userLeft: '{userName} hat den Raum verlassen',
            userJoinedTeam: 'ist dem Team beigetreten',
            userLeftTeam: 'hat das Team verlassen',
            userExists: '{username} ist bereits vorhanden',
            teamExists: 'Team ist bereits vorhanden',
            gameStartedCreateTeam: 'Du kannst kein neues Team erstellen nachdem das Spiel gestartet wurde',
            teamCreated: 'Team wurde erfolgreich erstellt',
            leaveRoom: 'Du hast den Raum erfolgreich verlassen',
            createVirtualUser: 'wurde erfolgreich erstellt',
            tryConnectPi: 'Versuche den Pi zu verbinden',
            connectPiFailed: 'Verbindungsaufbau zum Pi ist fehlgeschlagen',
            tryDisconnectPi: 'Versuche den Pi zu trennen',
            disconnectPiFailed: 'Versuch, den Pi zu trennen ist fehlgeschlagen',
            refreshPis: 'Pis wurden aktualisiert',
            noPermissionsToCreateGame: 'Nur ein Host kann ein neues Spiel erstellen',
            twoTeamsRequired: 'Es benötigt mindestens zwei Teams',
            gameNotStartedYet: 'Das Spiel ist noch nicht gestartet',
            joinedGame: 'Spiel beigetreten',
            teamJoined: 'Team erfolgreich beigetreten',
            waitingForNextRound: 'Warten auf die nächste Runde',
            validatePoints: 'Bitte validieren Sie die Punkte',
            beingValidated: 'Die Punkte werden validiert',
            lookUpTheTerm: 'schauen Sie den Begriff an!',
            resultConfirmed: 'Ergebnis bestätigt',
            resultRejected: 'Regelverstoß gemeldet',
        },
        rollTheDice: 'Bitte würfeln Sie',
        maxPoints: 'Maximale Punkte',
        guessedTerm: 'Begriff erraten',
        notGuessedTerm: 'Begriff nicht erraten',
        ruleViolation: 'Regelverstoß',
        currentlySpectating: 'Sie sind gerade Zuschauer',
    },

    login: {
        login: 'Einloggen',
        noAccountYet: 'Sie haben noch kein Benutzerkonto?',
        loginAgain: 'Bitte erneut einloggen',
        messages: {
            loginSuccess: 'Einloggen erfolgreich',
        },
        errors: {
            wrongCredentials: 'Falscher Benutzername oder Passwort',
        },
    },
    dashboard: {
        dashboard: 'Dashboard',
        topics: 'Themengebiete',
        term: 'Begriff',
        players: 'Spieler',
        games: 'Spiele',
        createdBy: 'Erstellt von',
        termImporter: 'Begriff-importer',
        correctGuesses: 'Richtig erraten',
        numOfAppearances: 'Anzahl der Erscheinungen',
        importTerms: 'Begriffe importieren',
        foundTerms: '{termCount} Begriffe gefunden. Klicken Sie zum Importieren.',
        importedTerm: '{termName} wurde erfolgreich importiert.',
        importedError: '{termName} konnte nicht importiert werden.',
        importSuccess: 'Alle Begriffe wurden erfolgreich importiert.',
        selectTopic: 'Themengebiet auswählen',
        selectFile: 'Datei auswählen',
        createNewTopic: 'Neues Themengebiet hinzufügen',
        createTopic: 'Themengebiet hinzufügen',
        createTeam: 'Team erstellen',
        editTopic: 'Themengebiet bearbeiten',
        createTerm: 'Begriff hinzufügen',
        options: 'Einstellungen',
        messages: {
            topicCreateSuccess: 'Themengebiet hinzugefügt!',
            topicEditSuccess: 'Themengebiet bearbeitet!',
            topicCreateSuccessMessage: '{topicName} wurde erfolgreich hinzugefügt',
            topicEditSuccessMessage: '{topicName} wurde erfolgreich bearbeitet',
            topicDeleteSuccess: 'Themengebiet wurde erfolgreich entfernt',
            termCreateSuccess: 'Begriff hinzugefügt!',
            termCreateSuccessMessage: '{termName} wurde erfolgreich hinzugefügt',
            userDeleteSuccess: 'Benutzer gelöscht.',
            userDeleteSuccessMessage: 'Benutzer: {userName} wurde erfolgreich gelöscht.',
        },
    },
    profile: {
        profile: 'Profil',
        updateUser: 'Benutzer aktualisieren',
        logout: 'Ausloggen',
        logoutSuccess: 'Ausloggen erfolgreich',
        userUpdatedSuccess: 'Benutzer wurde erfolgreich aktualisiert',
        lastPlayedWith: 'Letzten Teammitglieder',
        registrationDate: 'Registrierungsdatum',
        role: 'Rolle',
        selectRole: 'Rolle auswählen',
        stats: 'Statistiken',
        nothingYet: 'Noch nichts',
        noGamesPlayed: 'Noch keine Spiele gespielt',
        totalNumGamesPlayed: 'Gesamtzahl gespielter Spiele',
    },
    room: {
        spectate: 'Zuschauen',
        playersNeedToJoin: 'Spieler müssen noch Teams beitreten',
        createTeam: 'Neues Team erstellen',
        createVirtualUser: 'Lokalen Spieler erstellen',
        leaveRoom: 'Diesen Raum verlassen',
        startGame: 'Spiel starten',
        joinGame: 'Spiel beitreten',
        selectPi: 'Wähle den PI aus',
        selectPiList: 'PI auswählen',
        noPiFound: 'Keine PIs gefunden',
        connectedWithPi: 'Mit {connectedPi} verbunden!',
    },
    copy2clipboard: {
        copyButtonText: 'Invite URL kopieren',
        copySuccess: 'URL erfolgreich in die Zwischenablage kopiert',
        copyError: 'URL konnte nicht kopiert werden',
    },
};
