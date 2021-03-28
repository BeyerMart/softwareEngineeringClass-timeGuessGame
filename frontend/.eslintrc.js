module.exports = {
    root: true,
    env: {
        browser: true,
        commonjs: true,
        es6: true,
    },
    plugins: [
        'vue',
    ],
    extends: [
        'eslint:recommended',
        'plugin:vue/recommended',
        'airbnb-base',
        'plugin:import/recommended',
    ],
    parserOptions: {
        parser: 'babel-eslint',
    },
    rules: {
        indent: ['error', 4],
        'no-plusplus': 'off',
        'max-len': 'off',
        'import/prefer-default-export': 'off',
        'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'vue/html-indent': ['error', 4, {
            attribute: 1,
            baseIndent: 1,
            closeBracket: 0,
            alignAttributesVertically: true,

        }],
        'import/no-extraneous-dependencies': 'off',
        'no-param-reassign': [
            'error',
            {
                props: true,
                ignorePropertyModificationsFor: [
                    'state',
                ],
            },
        ],
        'prefer-promise-reject-errors': 'off',
        'import/no-unresolved': 'off',
    },
};
