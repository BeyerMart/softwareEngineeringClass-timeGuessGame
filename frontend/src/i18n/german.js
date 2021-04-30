export default {
    generic: {
        overview: 'Übersicht',
        error: 'Fehler',
        profile: 'Profil',
    },
    errors: {
        back: 'Zurück',
        notFound: 'Entschuldigung, diese Seite existiert nicht',
        internalserror: 'Ein unerwarteter Fehler ist aufgetreten',
        userNotFound: 'Benutzer konnte nicht gefunden werden',
        validation: {
            nameRequired: 'Name ist erforderlich',
            topicRequired: 'Themengebiet is erforderlich',
        },
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
        },
        messages: {
            signupSuccess: 'Registrierung erfolgreich! Bitte anmelden',
        },
    },
    game: {
        team: 'Team',
        teamName: 'Teamname',
        enemy: 'Gegner',
        points: 'Punkte',
        score: 'Punktestand',
        topic: 'Themengebiet',
        selectTopic: 'Themengebiet auswählen',
        createGame: 'Spiel erstellen',
        createNewRoom: 'Neuen Raum erstellen',
        createRoom: 'Raum erstellen',
        room: 'Spielraum',
        roomName: 'Raumname',
        numOfPlayers: 'Anzahl der Spieler',
        numOfTeams: 'Anzahl der Teams',
        games: 'Spiele',
        messages: {
            roomCreateSuccess: 'Raum wurde erfolgreich erstellt',
        },
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
    profile: {
        profile: 'Profil',
        updateUser: 'Benutzer aktualisieren',
        logout: 'Ausloggen',
        logoutSuccess: 'Ausloggen erfolgreich',
        userUpdatedSuccess: 'Benutzer wurde erfolgreich aktualisiert',
        lastPlayedWith: 'Zuletzt gespielt mit',
    },
};
