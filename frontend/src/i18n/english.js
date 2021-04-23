export default {
    generic: {
        overview: 'Overview',
        error: 'Error',
        profile: 'Profile',
    },
    errors: {
        back: 'Go back',
        notFound: 'Sorry, this page was not found',
        internalserror: 'An unexpected server error occurred',
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
    login: {
        login: 'Login',
        noAccountYet: 'Don\'t have an account yet?',
        messages: {
            loginSuccess: 'Login successful',
        },
        errors: {
            wrongCredentials: 'Wrong username or password',
        },
    },
};
