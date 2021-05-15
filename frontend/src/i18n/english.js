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
        universityOfInnsbruck: 'University of Innsbruck',
        imprint: 'Imprint',
        tos: 'Terms of Service',
        create: 'Create',
        pleaseWait: 'Please wait',
    },
    languages: {
        de: 'German',
        en: 'English',
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
            maxPointsIsRequired: 'Max points is required.',
            minPointsRequired: 'Please choose at least 5 points',
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
        teams: 'Teams',
        messages: {
            roomCreateSuccess: 'Room was created successfully',
            roomAlreadyFull: 'Room is already full',
            youJoinedRoom: 'You joined the room successfully',
            roomRemoved: 'The Room was deleted by the host.',
            userJoined: ' joined',
            userJoinedTeam: 'joined the Team',
            userLeftTeam: 'left the Team',
            teamExists: 'already exists',
            gameStartedCreateTeam: 'You can\'t create a new Team after the Game has started',
            teamCreated: 'Team successfully created',
            leaveRoom: 'You successfully left the room',
            createVirtualUser: 'was successfully created',
            tryConnectPi: 'Trying to connect Pi',
            connectPiFailed: 'Failed to connect Pi',
            tryDisconnectPi: 'Trying to disconnect Pi',
            disconnectPiFailed: 'Failed to disconnect Pi',
            refreshPis: 'Refreshed pis',
            noPermissionsToCreateGame: 'Only a host can create a new game',
            twoTeamsRequired: 'At least two teams are required',
            gameNotStartedYet: 'Game has not started yet',
            joinedGame: 'Joined game',
            teamJoined: 'Team joined successfully',
            waitingForNextRound: 'Waiting for next round',
        },
        rollTheDice: 'Please roll the dice',
        maxPoints: 'Max points',
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
        createTeam: 'Create team',
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
    room: {
        playersNeedToJoin: 'Players yet to join a team',
        createTeam: 'Create new team',
        createVirtualUser: 'Create new virtual player',
        leaveRoom: 'Leave this room',
        startGame: 'Start game',
        joinGame: 'Join game',
    },
};
