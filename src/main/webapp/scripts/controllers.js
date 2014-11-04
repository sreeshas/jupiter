'use strict';

/* Controllers */

jupiterApp.controller('MainController', function ($scope, $http, GoogleMaps) {
    var map;
    $scope.unsavedCab = {};
    $scope.addOrUpdateCab = false;
    $scope.searchCab=true;
    $scope.findCab=false;
    $scope.deleteCab=false;



    $scope.searchButtonClicked = function(option) {
       resetView();
       $scope.searchCab=true;
    }
    $scope.findButtonClicked = function(option) {
        resetView();
        $scope.findCab=true;
    }
    $scope.addOrUpdateButtonClicked = function(option) {
        resetView();
        $scope.addOrUpdateCab=true;
    }
    $scope.deleteButtonClicked = function(option) {
        resetView();
        $scope.deleteCab=true;
    }

    function resetView(){
        $scope.addOrUpdateCab = false;
        $scope.searchCab=false;
        $scope.findCab=false;
        $scope.deleteCab=false;
    }

    function setup() {
        var initializedMap = GoogleMaps.getMap();
        if ( initializedMap == null) {
            initializeMap();
        } else {
            //TODO:
            //Figure out the best way to swap out divs
            //It is not a best practice to manipulate dom in controller.
            map = initializedMap;
            $('#map-canvas').append(map.getDiv());
        }
    }

    function getData() {

        $http.get('/cabs?latitude=37.39715&longitude=-122.02412&limit=5&radius=80000').
            success(function(data, status, headers, config) {

                data.forEach(function(entry) {
                    GoogleMaps.addSavedCabOnMap(entry);
                });

            }).
            error(function(data, status, headers, config) {
                alert(data);
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
    }

    function onCurrentPositionResult(position) {
        var mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(position.coords.latitude, position.coords.longitude)
        };
        map = new google.maps.Map(document.getElementById('map-canvas'),
            mapOptions);
        var circleOptions = {
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35,
            map: map,
            center: mapOptions.center,
            radius: 200
        }
        GoogleMaps.setMapOptions(mapOptions);
        GoogleMaps.setCircleOptions(circleOptions);
        GoogleMaps.addMap(map);
        //Add a circle around current location.
        var locationCircle = new google.maps.Circle(circleOptions);
        GoogleMaps.setLocationCircle(locationCircle);
        //Add a point on current location
        var marker = new google.maps.Marker({
            position: mapOptions.center,
            map: map,
            title: "Latitude :" + position.coords.latitude + " \nLongitude: "+position.coords.longitude

        });
        GoogleMaps.setCurrentLocationMarker(marker);
        google.maps.event.addListener(map, 'click', function(event) {
            onMapClick(event.latLng);
        });
        getData();

    }
    function initializeMap() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(onCurrentPositionResult)
        }
    }

    function onMapClick(location) {
        $scope.$apply(function(){
            resetView();
            $scope.addOrUpdateCab= true;
            $scope.unsavedCab.latitude = location.lat();
            $scope.unsavedCab.longitude = location.lng();
        });
    }


    //setup();
    google.maps.event.addDomListener(window, 'load', setup);




    });

jupiterApp.controller('AdminController', function ($scope) {
    });

jupiterApp.controller('LanguageController', function ($scope, $translate, LanguageService) {
        $scope.changeLanguage = function (languageKey) {
            $translate.use(languageKey);

            LanguageService.getBy(languageKey).then(function(languages) {
                $scope.languages = languages;
            });
        };

        LanguageService.getBy().then(function (languages) {
            $scope.languages = languages;
        });
    });

jupiterApp.controller('MenuController', function ($scope) {
    });

jupiterApp.controller('LoginController', function ($scope, $location, AuthenticationSharedService) {
        $scope.rememberMe = true;
        $scope.login = function () {
            AuthenticationSharedService.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            });
        }
    });

jupiterApp.controller('LogoutController', function ($location, AuthenticationSharedService) {
        AuthenticationSharedService.logout();
    });

jupiterApp.controller('SettingsController', function ($scope, Account) {
        $scope.success = null;
        $scope.error = null;
        $scope.settingsAccount = Account.get();

        $scope.save = function () {
            Account.save($scope.settingsAccount,
                function (value, responseHeaders) {
                    $scope.error = null;
                    $scope.success = 'OK';
                    $scope.settingsAccount = Account.get();
                },
                function (httpResponse) {
                    $scope.success = null;
                    $scope.error = "ERROR";
                });
        };
    });

jupiterApp.controller('RegisterController', function ($scope, $translate, Register) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.register = function () {
            if ($scope.registerAccount.password != $scope.confirmPassword) {
                $scope.doNotMatch = "ERROR";
            } else {
                $scope.registerAccount.langKey = $translate.use();
                $scope.doNotMatch = null;
                Register.save($scope.registerAccount,
                    function (value, responseHeaders) {
                        $scope.error = null;
                        $scope.errorUserExists = null;
                        $scope.success = 'OK';
                    },
                    function (httpResponse) {
                        $scope.success = null;
                        if (httpResponse.status === 304 &&
                                httpResponse.data.error && httpResponse.data.error === "Not Modified") {
                            $scope.error = null;
                            $scope.errorUserExists = "ERROR";
                        } else {
                            $scope.error = "ERROR";
                            $scope.errorUserExists = null;
                        }
                    });
            }
        }
    });

jupiterApp.controller('ActivationController', function ($scope, $routeParams, Activate) {
        Activate.get({key: $routeParams.key},
            function (value, responseHeaders) {
                $scope.error = null;
                $scope.success = 'OK';
            },
            function (httpResponse) {
                $scope.success = null;
                $scope.error = "ERROR";
            });
    });

jupiterApp.controller('PasswordController', function ($scope, Password) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.changePassword = function () {
            if ($scope.password != $scope.confirmPassword) {
                $scope.doNotMatch = "ERROR";
            } else {
                $scope.doNotMatch = null;
                Password.save($scope.password,
                    function (value, responseHeaders) {
                        $scope.error = null;
                        $scope.success = 'OK';
                    },
                    function (httpResponse) {
                        $scope.success = null;
                        $scope.error = "ERROR";
                    });
            }
        };
    });

jupiterApp.controller('SessionsController', function ($scope, resolvedSessions, Sessions) {
        $scope.success = null;
        $scope.error = null;
        $scope.sessions = resolvedSessions;
        $scope.invalidate = function (series) {
            Sessions.delete({series: encodeURIComponent(series)},
                function (value, responseHeaders) {
                    $scope.error = null;
                    $scope.success = "OK";
                    $scope.sessions = Sessions.get();
                },
                function (httpResponse) {
                    $scope.success = null;
                    $scope.error = "ERROR";
                });
        };
    });

 jupiterApp.controller('MetricsController', function ($scope, MetricsService, HealthCheckService, ThreadDumpService) {

        $scope.refresh = function() {
            HealthCheckService.check().then(function(promise) {
                $scope.healthCheck = promise.data;
            },function(promise) {
                $scope.healthCheck = promise.data;
            });

            $scope.metrics = MetricsService.get();

            $scope.metrics.$get({}, function(items) {

                $scope.servicesStats = {};
                $scope.cachesStats = {};
                angular.forEach(items.timers, function(value, key) {
                    if (key.indexOf("web.rest") != -1 || key.indexOf("service") != -1) {
                        $scope.servicesStats[key] = value;
                    }

                    if (key.indexOf("net.sf.ehcache.Cache") != -1) {
                        // remove gets or puts
                        var index = key.lastIndexOf(".");
                        var newKey = key.substr(0, index);

                        // Keep the name of the domain
                        index = newKey.lastIndexOf(".");
                        $scope.cachesStats[newKey] = {
                            'name': newKey.substr(index + 1),
                            'value': value
                        };
                    }
                });
            });
        };

        $scope.refresh();

        $scope.threadDump = function() {
            ThreadDumpService.dump().then(function(data) {
                $scope.threadDump = data;

                $scope.threadDumpRunnable = 0;
                $scope.threadDumpWaiting = 0;
                $scope.threadDumpTimedWaiting = 0;
                $scope.threadDumpBlocked = 0;

                angular.forEach(data, function(value, key) {
                    if (value.threadState == 'RUNNABLE') {
                        $scope.threadDumpRunnable += 1;
                    } else if (value.threadState == 'WAITING') {
                        $scope.threadDumpWaiting += 1;
                    } else if (value.threadState == 'TIMED_WAITING') {
                        $scope.threadDumpTimedWaiting += 1;
                    } else if (value.threadState == 'BLOCKED') {
                        $scope.threadDumpBlocked += 1;
                    }
                });

                $scope.threadDumpAll = $scope.threadDumpRunnable + $scope.threadDumpWaiting +
                    $scope.threadDumpTimedWaiting + $scope.threadDumpBlocked;

            });
        };

        $scope.getLabelClass = function(threadState) {
            if (threadState == 'RUNNABLE') {
                return "label-success";
            } else if (threadState == 'WAITING') {
                return "label-info";
            } else if (threadState == 'TIMED_WAITING') {
                return "label-warning";
            } else if (threadState == 'BLOCKED') {
                return "label-danger";
            }
        };
    });

jupiterApp.controller('LogsController', function ($scope, resolvedLogs, LogsService) {
        $scope.loggers = resolvedLogs;

        $scope.changeLevel = function (name, level) {
            LogsService.changeLevel({name: name, level: level}, function () {
                $scope.loggers = LogsService.findAll();
            });
        }
    });

jupiterApp.controller('AuditsController', function ($scope, $translate, $filter, AuditsService) {
        $scope.onChangeDate = function() {
            AuditsService.findByDates($scope.fromDate, $scope.toDate).then(function(data){
                $scope.audits = data;
            });
        };

        // Date picker configuration
        $scope.today = function() {
            // Today + 1 day - needed if the current day must be included
            var today = new Date();
            var tomorrow = new Date(today.getFullYear(), today.getMonth(), today.getDate()+1); // create new increased date

            $scope.toDate = $filter('date')(tomorrow, "yyyy-MM-dd");
        };

        $scope.previousMonth = function() {
            var fromDate = new Date();
            if (fromDate.getMonth() == 0) {
                fromDate = new Date(fromDate.getFullYear() - 1, 0, fromDate.getDate());
            } else {
                fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
            }

            $scope.fromDate = $filter('date')(fromDate, "yyyy-MM-dd");
        };

        $scope.today();
        $scope.previousMonth();

        AuditsService.findByDates($scope.fromDate, $scope.toDate).then(function(data){
            $scope.audits = data;
        });
    });

