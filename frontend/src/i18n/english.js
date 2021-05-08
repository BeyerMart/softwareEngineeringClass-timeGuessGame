export default {
    generic: {
        overview: 'Overview',
        error: 'Error',
        profile: 'Profile',
        name: 'Name',
    },
    errors: {
        back: 'Go back',
        notFound: 'Sorry, this page was not found',
        internalserror: 'An unexpected server error occurred',
        userNotFound: 'User not found',
        validation: {
            nameRequired: 'Name is required',
            termNameRequired: 'Term name is required',
        },
    },
    signup: {
        signup: 'Sign Up',
        email: 'Email',
        username: 'Username',
        password: 'Password',
        confirmPassword: 'Confirm password',
        createAccount: 'Create account',
        accountExists: 'Already have an account?',
        errors: {
            emailRequired: 'Email is required',
            emailInvalid: 'Email is invalid',
            usernameRequired: 'Username is required',
            passwordRequired: 'Password is required',
            confirmPasswordRequired: 'Confirm password is required',
            confirmPasswordMatch: 'Passwords must match',
        },
        messages: {
            signupSuccess: 'Signup successful! Please Login',
        },
    },
    game: {
        team: 'Team',
        teamName: 'Team name',
        enemy: 'Enemy',
        points: 'Points',
        score: 'Score',
        topic: 'Topic',
    },
    login: {
        login: 'Login',
        noAccountYet: 'Don\'t have an account yet?',
        loginAgain: 'Please login again',
        messages: {
            loginSuccess: 'Login successful',
        },
        errors: {
            wrongCredentials: 'Wrong username or password',
        },
    },
    dashboard: {
        dashboard: 'Dashboard',
        topics: 'Topics',
        term: 'Begriff',
        players: 'Players',
        games: 'Gamer',
        createdBy: 'Created by',
        termImporter: 'Term importer',
        importTerms: 'Import terms',
        foundTerms: '{termCount} Found. Click to import.',
        importedTerm: '{termName} was successfully imported.',
        importSuccess: 'All terms were imported successfully.',
        selectTopic: 'Themengebiert auswählen',
        selectFile: 'Datei auswählen',
        createNewTopic: 'Create new term',
        createTopic: 'Create topic',
        createTerm: 'Create term',
        messages: {
            topicCreateSuccess: 'Topic created!',
            topicCreateSuccessMessage: '{topicName} was created successfully',
            termCreateSuccess: 'Term created!',
            termCreateSuccessMessage: '{termName} was created successfully',
        },
    },
    profile: {
        profile: 'Profile',
        updateUser: 'Update user',
        logout: 'Logout',
        logoutSuccess: 'Logout successful',
        userUpdatedSuccess: 'User was updated successfully',
        lastPlayedWith: 'Last played with',
    },
};
