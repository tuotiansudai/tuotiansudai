var require = {
    //baseUrl: "../js",

    'paths': {
        // text plugin
        'text': 'libs/text',

        //lib
        "jquery": "libs/jquery-1.10.1.min",
        "jquery.validate": "libs/jquery.validate.min",
        "mustache": "libs/mustache.min",
        "moment": "libs/moment.min"
    },

    'shim': {
        'jquery.validate': {
            deps: ['jquery']
        }
    }
};

