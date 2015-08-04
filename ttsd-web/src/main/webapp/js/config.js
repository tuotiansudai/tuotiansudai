var require = {
    'paths': {
        // text plugin
        'text': 'libs/text',
        //lib
        "jquery": "libs/jquery-1.11.3",
        "jquery.validate": "libs/jquery.validate-1.14.0",
        "mustache": "libs/mustache.min",
        "moment": "libs/moment.min",
        'underscore': 'libs/underscore-1.8.3.min'
    },

    'shim': {
        'jquery.validate': {
            deps: ['jquery']
        }
    }
};

