var require = {
    baseUrl: "/mobile/js",

    'paths': {
        'jquery': 'libs/jquery-1.10.1.min',
        'validate': 'libs/jquery.validate.min',
        'validate-ex': 'validate-ex'
    },

    'shim': {
        'jquery.validate': {
            deps: ['jquery']
        }
    }
};