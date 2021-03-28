const path = require('path');

module.exports = {
    assetsDir: 'static',
    chainWebpack: (config) => {
        config.module
            .rule('eslint')
            .use('eslint-loader')
            .options({
                fix: true,
            });
    },
    configureWebpack: {
        resolve: {
            extensions: ['.js', '.vue', '.json'],
            alias: {
                '@': path.resolve(__dirname, 'src'),
            },
        },
    },
};
