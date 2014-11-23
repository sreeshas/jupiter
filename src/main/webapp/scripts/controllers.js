'use strict';

/* Controllers */

jupiterApp.controller('MainController', function ($scope, $http, GoogleMaps, CabService) {
    $scope.unsavedCab = {};
    resetView();
    $scope.result = [];
    $scope.searchradius=50; //default set to 50m
    $scope.searchlimit=10;  //default set to 10 results.
    $scope.searchResults=[];
    $scope.currentLocation = {};
    var latregex = new RegExp("^-?([1-8]?[0-9]\\.{1}\\d{1,50}$|90\\.{1}0{1,50}$)");
    var longregex =  new RegExp("^-?((([1]?[0-7][0-9]|[1-9]?[0-9])\\.{1}\\d{1,50}$)|[1]?[1-8][0]\\.{1}0{1,50}$)");

    //this does not work always and has weird load issues. Fix it later.
    //google.maps.event.addDomListener(window, 'load', setup);
    $(setup);

    function setup() {
        var initializedMap = GoogleMaps.getMap();
        if (!initializedMap) {
            initializeMap();
        } else {
            //TODO:
            //Figure out the best way to swap out divs
            //It is not a best practice to manipulate dom in controller.
            $('#map-canvas').append(initializedMap.getDiv());
        }
        $scope.searchCab=true;
    }

    function initializeMap() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(onCurrentPositionResult)
        }
    }

    function onCurrentPositionResult(position) {
        $scope.$apply(function() {
            $scope.currentLocation = {
                latitude:  position.coords.latitude,
                longitude: position.coords.longitude
            }
        });
        $scope.$watch('currentLocation.latitude', function() {

            if (latregex.exec($scope.currentLocation.latitude)) {
                setOrUpdateCurrentLocationOnMap();
            } else {
                console.log("Invalid latitude value",$scope.currentLocation.latitude);
            }

        });
        $scope.$watch('currentLocation.longitude', function() {
            if (longregex.exec($scope.currentLocation.longitude)) {
                setOrUpdateCurrentLocationOnMap();
            } else {
                console.log("Invalid longitude value",$scope.currentLocation.longitude);
            }
        });
        //Listen for changes in current radius and update the map.
        $scope.$watch('searchradius', function(newValue, oldValue) {
            GoogleMaps.setRadius(newValue);
        });

        GoogleMaps.setCurrentLatitude($scope.currentLocation.latitude);
        GoogleMaps.setCurrentLongitude($scope.currentLocation.longitude);
        GoogleMaps.addMap(document.getElementById('map-canvas'));
        GoogleMaps.setCurrentLocationMarker($scope.currentLocation);
        GoogleMaps.addClickEventListener(onMapClick);

    };

    function onMapClick(location) {
        $scope.$apply(function(){
            resetView();
            if (GoogleMaps.getLocationCircle().getMap()){
                GoogleMaps.toggleCircle();
            }
            $scope.addOrUpdateCab= true;
            $scope.unsavedCab.latitude = location.lat();
            $scope.unsavedCab.longitude = location.lng();
        });
    }

    function reinitialize() {
        GoogleMaps.clearSavedCabs();
        GoogleMaps.clearUnsavedCab();
    }

    function clearResults() {
        $scope.result = [];
        $scope.searchResults=[];
    }

    function resetView(){
        $scope.addOrUpdateCab = false;
        $scope.searchCab=false;
        $scope.findCab=false;
        $scope.deleteCab=false;
    }

    $scope.searchButtonClicked = function(option) {
        resetView();
        reinitialize();
        $scope.searchCab=true;
        clearResults();
        //Pan Map to center of current location.
        GoogleMaps.panMap(new google.maps.LatLng(GoogleMaps.getCurrentLatitude(), GoogleMaps.getCurrentLongitude()));
        if (!GoogleMaps.getLocationCircle().getMap()){
            GoogleMaps.toggleCircle();
        }
    };
    $scope.findButtonClicked = function(option) {
        resetView();
        $scope.findCab=true;
        clearResults();
        if (GoogleMaps.getLocationCircle().getMap()){
            GoogleMaps.toggleCircle();
        }
    };
    $scope.addOrUpdateButtonClicked = function(option) {
        resetView();
        reinitialize();
        $scope.addOrUpdateCab=true;
        clearResults();
        if (GoogleMaps.getLocationCircle().getMap()){
            GoogleMaps.toggleCircle();
        }
    };
    $scope.deleteButtonClicked = function(option) {
        resetView();
        $scope.deleteCab=true;
        clearResults();
        if (GoogleMaps.getLocationCircle().getMap()){
            GoogleMaps.toggleCircle();
        }
    }
    $scope.onFind = function() {
        clearResults();
        if (!$scope.findCabId) {
            $scope.result = {error: "Cab Id cannot be empty"};
            return;
        }
        $scope.result = {};
        CabService.find($scope.findCabId).
            success(function(data, status, headers, config) {
                if (data) {
                    reinitialize();
                    $scope.result.findCab= { msg: 'ID : ' + data.id + ' Latitude: ' + data.latitude + ' Longitude ' + data.longitude};
                    GoogleMaps.panMap(new google.maps.LatLng(data.latitude, data.longitude));
                    GoogleMaps.addSavedCabOnMap(data);

                } else {
                    $scope.result.findCab= {error:"Cab " +$scope.findCabId+" not found"};
                }

            }).
            error(function(data, status, headers, config) {
                alert(data);
            });
    }
    $scope.onAddUpdate = function() {
        clearResults();
        CabService.addOrUpdateCab($scope.unsavedCab.id, $scope.unsavedCab.latitude, $scope.unsavedCab.longitude).
            success(function(data, status, headers, config) {
                if (status == 200) {
                    $scope.result.addUpdate = { msg: "Successfully saved Cab " + $scope.unsavedCab.id};
                    GoogleMaps.addSavedCabOnMap($scope.unsavedCab);
                    GoogleMaps.clearUnsavedCab();
                }
            }).
            error(function(data, status, headers, config) {
                if (status == 404){
                    $scope.result.addUpdate= {error:"Cab " +$scope.unsavedCab.id+" could not be saved"};
                } else {
                    alert(status);
                }
            });
    };
    $scope.onDelete= function() {
        clearResults();
        if (!$scope.deleteCabId) {
            $scope.result = {error: "Cab Id cannot be empty"};
            return;
        }
        CabService.deleteCab($scope.deleteCabId).
            success(function(data, status, headers, config) {
                if (status == 200) {
                    $scope.result.deleteCab = { msg: "Successfully deleted Cab " + $scope.deleteCabId};
                    reinitialize();
                }
            }).
            error(function(data, status, headers, config) {
                if (status == 404){
                    $scope.result.deleteCab= {error:"Cab " +$scope.deleteCabId+" not found"};
                } else {
                    alert(status);
                }
            });
    }
    $scope.search = function() {
        clearResults();
        CabService.search($scope.currentLocation.latitude,
            $scope.currentLocation.longitude,
            $scope.searchradius,
            $scope.searchlimit
        ).success(function(data, status, headers, config) {
                reinitialize();
                if (data.length >= 1) {
                    $scope.searchResults = data;
                    $scope.searchResults.count = data.length;
                    data.forEach(function(entry) {
                        GoogleMaps.addSavedCabOnMap(entry);
                    });

                } else {
                    $scope.result.searchCab =  {
                        error : "No Cabs found"
                    }
                }
            }).
            error(function(data, status, headers, config) {
                alert(status);
            });
    };




    function setOrUpdateCurrentLocationOnMap() {
        GoogleMaps.setCurrentLatitude($scope.currentLocation.latitude);
        GoogleMaps.setCurrentLongitude($scope.currentLocation.longitude);
        GoogleMaps.panMap(new google.maps.LatLng($scope.currentLocation.latitude, $scope.currentLocation.longitude));
        GoogleMaps.setMapOptions();
        GoogleMaps.setCurrentLocationMarker($scope.currentLocation);

    }



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

