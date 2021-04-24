export default {
    generic: {
        overview: 'Übersicht',
        error: 'Fehler',
    },
    errors: {
        back: 'Zurück',
        notFound: 'Entschuldigung, diese Seite existiert nicht',
        internalserror: 'Ein unerwarteter Fehler ist aufgetreten',
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
    login: {
        login: 'Einloggen',
        noAccountYet: 'Sie haben noch kein Benutzerkonto?',
        messages: {
            loginSuccess: 'Einloggen erfolgreich',
        },
        errors: {
            wrongCredentials: 'Falscher Benutzername oder Passwort',
        },
    },
};
