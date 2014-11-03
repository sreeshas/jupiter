'use strict';

/* Services */
jupiterApp.factory('GoogleMaps', function ($http) {
    var map;
    var currentLongitude;
    var currentLatitude;
    var mapOptions;
    var circleOptions;
    var locationCircle;
    var currentLocationMarker;
    var cabImage = 'images/cab.png'; //cab image.
    var unsavedCabImage = 'images/unsavedcab.png'
    var currentUnsavedCab;


    function addUnSavedCabOnMap(location) {
         if (currentUnsavedCab) {
             currentUnsavedCab.setMap(null);
         }
         currentUnsavedCab = new google.maps.Marker({
            position: location,
            map: map,
            icon: unsavedCabImage,
            title: "Latitude :" + location.lat() + " \nLongitude : "+location.lng()
        });
    }

    function addSavedCabOnMap(data){
        var cab = new google.maps.Marker({
            position: new google.maps.LatLng(data.latitude,data.longitude),
            map: map,
            icon: cabImage,
            title: "Latitude :" + data.latitude + " \nLongitude : "+data.longitude
        });
    }


    function addMap(initializedMap) {
        map = initializedMap;
        google.maps.event.addListener(map, 'click', function(event) {
            addUnSavedCabOnMap(event.latLng);
        });
    }
    function getMap() {
        return map;
    }

    function getCurrentLatitude(){
        return currentLatitude;
    }

    function getCurrentLongitude(){
        return  currentLongitude;
    }

    function setCurrentLongitude(longitude){
        currentLongitude = longitude;
    }

    function setCurrentLatitude(latitude){
        currentLatitude = latitude;
    }

    function getMapOptions() {
        return mapOptions;
    }

    function setMapOptions(newMapOptions){
        mapOptions = newMapOptions;
    }
    function getCircleOptions() {
        return circleOptions;
    }
    function setCircleOptions(newCircleOptions) {
        circleOptions = newCircleOptions;
    }
    function setLocationCircle(newLocationCircle){
        locationCircle = newLocationCircle;
    }
    function getLocationCircle(){
        return locationCircle;
    }
    function getCurrentLocationMarker(){
        return currentLocationMarker;
    }
    function setCurrentLocationMarker(locationMarker){
        currentLocationMarker = locationMarker
    }
    return {
        addMap: addMap,
        getMap: getMap,
        setMapOptions: setMapOptions,
        getMapOptions: getMapOptions,
        setCurrentLatitude: setCurrentLatitude,
        setCurrentLongitude: setCurrentLongitude,
        getCircleOptions: getCircleOptions,
        setCircleOptions: setCircleOptions,
        getLocationCircle: getLocationCircle,
        setLocationCircle: setLocationCircle,
        getCurrentLocationMarker: getCurrentLocationMarker,
        setCurrentLocationMarker: setCurrentLocationMarker,
        addSavedCabOnMap: addSavedCabOnMap

    }
});

jupiterApp.factory('LanguageService', function ($http, $translate, LANGUAGES) {
        return {
            getBy: function(language) {
                if (language == undefined) {
                    language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');
                }

                var promise =  $http.get('/i18n/' + language + '.json').then(function(response) {
                    return LANGUAGES;
                });
                return promise;
            }
        };
    });

jupiterApp.factory('Register', function ($resource) {
        return $resource('app/rest/register', {}, {
        });
    });

jupiterApp.factory('Activate', function ($resource) {
        return $resource('app/rest/activate', {}, {
            'get': { method: 'GET', params: {}, isArray: false}
        });
    });

jupiterApp.factory('Account', function ($resource) {
        return $resource('app/rest/account', {}, {
        });
    });

jupiterApp.factory('Password', function ($resource) {
        return $resource('app/rest/account/change_password', {}, {
        });
    });

jupiterApp.factory('Sessions', function ($resource) {
        return $resource('app/rest/account/sessions/:series', {}, {
            'get': { method: 'GET', isArray: true}
        });
    });

jupiterApp.factory('MetricsService',function ($resource) {
        return $resource('metrics/metrics', {}, {
            'get': { method: 'GET'}
        });
    });

jupiterApp.factory('ThreadDumpService', function ($http) {
        return {
            dump: function() {
                var promise = $http.get('dump').then(function(response){
                    return response.data;
                });
                return promise;
            }
        };
    });

jupiterApp.factory('HealthCheckService', function ($rootScope, $http) {
        return {
            check: function() {
                var promise = $http.get('health').then(function(response){
                    return response.data;
                });
                return promise;
            }
        };
    });

jupiterApp.factory('LogsService', function ($resource) {
        return $resource('app/rest/logs', {}, {
            'findAll': { method: 'GET', isArray: true},
            'changeLevel':  { method: 'PUT'}
        });
    });

jupiterApp.factory('AuditsService', function ($http) {
        return {
            findAll: function() {
                var promise = $http.get('app/rest/audits/all').then(function (response) {
                    return response.data;
                });
                return promise;
            },
            findByDates: function(fromDate, toDate) {
                var promise = $http.get('app/rest/audits/byDates', {params: {fromDate: fromDate, toDate: toDate}}).then(function (response) {
                    return response.data;
                });
                return promise;
            }
        }
    });

jupiterApp.factory('Session', function () {
        this.create = function (login, firstName, lastName, email, userRoles) {
            this.login = login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.userRoles = userRoles;
        };
        this.invalidate = function () {
            this.login = null;
            this.firstName = null;
            this.lastName = null;
            this.email = null;
            this.userRoles = null;
        };
        return this;
    });

jupiterApp.factory('AuthenticationSharedService', function ($rootScope, $http, authService, Session, Account) {
        return {
            login: function (param) {
                var data ="j_username=" + param.username +"&j_password=" + param.password +"&_spring_security_remember_me=" + param.rememberMe +"&submit=Login";
                $http.post('app/authentication', data, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    Account.get(function(data) {
                        Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);
                        $rootScope.account = Session;
                        authService.loginConfirmed(data);
                    });
                }).error(function (data, status, headers, config) {
                    $rootScope.authenticationError = true;
                    Session.invalidate();
                });
            },
            valid: function (authorizedRoles) {

                $http.get('protected/authentication_check.gif', {
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    if (!Session.login) {
                        Account.get(function(data) {
                            Session.create(data.login, data.firstName, data.lastName, data.email, data.roles);
                            $rootScope.account = Session;

                            if (!$rootScope.isAuthorized(authorizedRoles)) {
                                event.preventDefault();
                                // user is not allowed
                                $rootScope.$broadcast("event:auth-notAuthorized");
                            }

                            $rootScope.authenticated = true;
                        });
                    }
                    $rootScope.authenticated = !!Session.login;
                }).error(function (data, status, headers, config) {
                    $rootScope.authenticated = false;
                });
            },
            isAuthorized: function (authorizedRoles) {
                if (!angular.isArray(authorizedRoles)) {
                    if (authorizedRoles == '*') {
                        return true;
                    }

                    authorizedRoles = [authorizedRoles];
                }

                var isAuthorized = false;
                angular.forEach(authorizedRoles, function(authorizedRole) {
                    var authorized = (!!Session.login &&
                        Session.userRoles.indexOf(authorizedRole) !== -1);

                    if (authorized || authorizedRole == '*') {
                        isAuthorized = true;
                    }
                });

                return isAuthorized;
            },
            logout: function () {
                $rootScope.authenticationError = false;
                $rootScope.authenticated = false;
                $rootScope.account = null;

                $http.get('app/logout');
                Session.invalidate();
                authService.loginCancelled();
            }
        };
    });
