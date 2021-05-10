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
    },
    errors: {
        back: 'Zurück',
        notFound: 'Entschuldigung, diese Seite existiert nicht',
        internalserror: 'Ein unerwarteter Fehler ist aufgetreten',
        userNotFound: 'Benutzer konnte nicht gefunden werden',
        validation: {
            nameRequired: 'Name ist erforderlich',
            termNameRequired: 'Begriffsname ist erforderlich',
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
        importTerms: 'Begriffe importieren',
        foundTerms: '{termCount} Begriffe gefunden. Klicken Sie zum Importieren.',
        importedTerm: '{termName} wurde erfolgreich importiert.',
        importedError: '{termName} konnte nicht importiert werden.',
        importSuccess: 'Alle Begriffe wurden erfolgreich importiert.',
        selectTopic: 'Themengebiet auswählen',
        selectFile: 'Datei auswählen',
        createNewTopic: 'Neues Themengebiet hinzufügen',
        createTopic: 'Themengebiet hinzufügen',
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
        },
    },
    profile: {
        profile: 'Profil',
        updateUser: 'Benutzer aktualisieren',
        logout: 'Ausloggen',
        logoutSuccess: 'Ausloggen erfolgreich',
        userUpdatedSuccess: 'Benutzer wurde erfolgreich aktualisiert',
        lastPlayedWith: 'Zuletzt gespielt mit',
        registrationDate: 'Registrierungsdatum',
    },
};
