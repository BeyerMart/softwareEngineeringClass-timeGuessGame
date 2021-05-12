export default {
    generic: {
        overview: 'Overview',
        error: 'Error',
        profile: 'Profile',
        name: 'Name',
        confirmTitle: 'Are you sure?',
        confirmMessage: 'You can’t undo this action',
        yes: 'Ja',
        no: 'Nein',
        role: 'Role',
        create: 'Create',
    },
    errors: {
        back: 'Go back',
        notFound: 'Sorry, this page was not found',
        internalserror: 'An unexpected server error occurred',
        userNotFound: 'User not found',
        validation: {
            nameRequired: 'Name is required',
            termNameRequired: 'Term name is required',
            topicRequired: 'Topic is required',
        },
        loginRequired: 'Login required',
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
        createdAt: 'Creation date',
        selectTopic: 'Select topic',
        createGame: 'Create game',
        createNewRoom: 'Create new room',
        createRoom: 'Create room',
        room: 'Room',
        roomName: 'Room name',
        numOfPlayers: 'Number of players',
        numOfTeams: 'Number of teams',
        games: 'Games',
        messages: {
            roomCreateSuccess: 'Room was created successfully',
            roomAlreadyFull: 'Room is already full',
            youJoinedRoom: 'You joined the room successfully',
            roomRemoved: 'The Room was deleted by the host.',
            userJoined: ' joined',
        },
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
        correctGuesses: 'Correct guesses',
        numOfAppearances: 'Num. of appearances',
        importTerms: 'Import terms',
        foundTerms: '{termCount} Found. Click to import.',
        importedTerm: '{termName} was successfully imported.',
        importedError: '{termName} could not be imported.',
        importSuccess: 'All terms were imported successfully.',
        selectTopic: 'Select topic',
        selectFile: 'Select file',
        createNewTopic: 'Create new term',
        createTopic: 'Create topic',
        editTopic: 'Edit topic',
        createTerm: 'Create term',
        options: 'Options',
        messages: {
            topicCreateSuccess: 'Topic created!',
            topicEditSuccess: 'Topic edited!',
            topicCreateSuccessMessage: '{topicName} was created successfully',
            topicEditSuccessMessage: '{topicName} was edited successfully',
            topicDeleteSuccess: 'Topic was deleted successfully',
            termCreateSuccess: 'Term created!',
            termCreateSuccessMessage: '{termName} was created successfully',
            userDeleteSuccess: 'User deleted',
            userDeleteSuccessMessage: 'User: {userName} was deleted successfully',
        },
    },
    profile: {
        profile: 'Profile',
        updateUser: 'Update user',
        logout: 'Logout',
        logoutSuccess: 'Logout successful',
        userUpdatedSuccess: 'User was updated successfully',
        lastPlayedWith: 'Last played with',
        registrationDate: 'Registraion date',
    },
};
