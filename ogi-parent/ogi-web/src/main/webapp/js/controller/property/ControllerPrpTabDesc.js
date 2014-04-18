function ControllerPrpTabDesc($scope, Page, $routeParams, ServiceConfiguration, ServiceObject, ServiceAlert, $http, $log ,$modal) {

    // Get type of current category. Run query only if promise of current type is resolved
    $scope.types = [];
    $scope.httpGetCurrentType.success(function() {
        $http.get(ServiceConfiguration.API_URL+"/rest/category/"+$scope.prp.category.code+"/types").success(function (data) {
            $scope.types = data;
            $scope.types.push(typeOther);

            // to select the pointer must be identical
            $scope.saveData.type = ServiceObject.getObject(data, $scope.saveData.type, ["label"]);
        });
    });

    getLabels("ROOF","roofs" , "roof");
    getLabels("WALL","walls" , "wall");
    getLabels("INSULATION", "insulations" , "insulation");
    getLabels("PARKING", "parkings" , "parking");
    /**
     * Get labels for a type and populate scope with them
     * @param type type of label to get
     * @param vScope name of scope variable into store list of labels
     * @param vSaveData name of variable into store selection. It will be stored in $scope.saveData.<vSaveData>
     */
    function getLabels(type, vScope, vSaveData) {
        $scope[vScope] = [];
        $scope.httpGetCurrentType.success(function() {
            $http.get(ServiceConfiguration.API_URL+"/rest/label/"+type).success(function (data) {
                $scope[vScope] = data;
                $scope[vScope].push(labelOther);

                // to select item, the pointer must be identical
                $scope.saveData[vSaveData] = ServiceObject.getObject(data, $scope.saveData[vSaveData], ["label", "type"]);
            });
        });
    }

    // Get all states
    $scope.states = [];
    $scope.states[0] = null;
    // First state is a NON state
    $scope.httpGetCurrentType.success(function() {
        $http.get(ServiceConfiguration.API_URL+"/rest/state/").success(function (data) {
            // Create array index by order
            for(var i = 0; i < data.length ; i++) {
                $scope.states[data[i].order] = data[i];
            }
        });
    });

    $scope.$watch('saveData.stateOrder', function() {
        $scope.prp.state = $scope.states[$scope.saveData.stateOrder];
    });

    // Build date is a date and IHM allow only year. So complete with month and day to create a date
    $scope.$watch('saveData.buildDate', function() {
        if(!$scope.utils.isUndefinedOrNull($scope.saveData.buildDate)) {
            $scope.prp.buildDate = $scope.saveData.buildDate+"-01-01";
        }
    });
    // SAve only label in property type
    $scope.$watch('saveData.type', function() {
        if(!$scope.utils.isUndefinedOrNull($scope.saveData.type)) {
            $scope.prp.type = $scope.saveData.type.label;
        }
    });



    $scope.typeChange = function () {
        if($scope.saveData.type.label == "Autre") {
            $scope.openModalType();
        }
    }
    $scope.roofChange = function () { labelChange("roof", $scope.saveData.roof, $scope.openModalRoof); };
    $scope.wallChange = function () { labelChange("wall", $scope.saveData.wall, $scope.openModalWall); };
    $scope.insulationChange = function () { labelChange("insulation", $scope.saveData.insulation, $scope.openModalInsulation); };
    $scope.parkingChange = function () { labelChange("parking", $scope.saveData.parking, $scope.openModalParking); };
    /**
     *
     * @param vPrp name of variable in object realproperty into store the label
     * @param vSaveData variable which contains selected label
     * @param fOpenModal function to execute when "Autre" is selected
     */
    function labelChange(vPrp, vSaveData, fOpenModal) {
        if(vSaveData != null && vSaveData.label == "Autre") {
            fOpenModal();
        }
        else if(!$scope.utils.isUndefinedOrNull( $scope.saveData[vPrp])) {
            $scope.prp[vPrp] = vSaveData.label;
        }
    }

    // ##### MODAL TYPE #####
    $scope.openModalType = function () {
        var modalInstance = $modal.open({
            templateUrl: 'modalAddType.html',
            controller: ModalTypeInstanceCtrl,
            resolve: {
                labels: function(){
                    return {
                        title:"Ajouter un type de bien",
                        placeholder:"Entrer un nouveau type"
                    };
                },
                currentElt: function () {
                    return $scope.prp.category.code;
                }
            }
        });

        modalInstance.result.then(function (param) {
            $log.info('Modal closed');
            $scope.types.push(param);
            $scope.saveData.type = param;
        }, function () {
            $log.info('Modal dismissed');
            $scope.saveData.type = undefined;
        });
    };

    // ##### MODAL LABEL #####
    $scope.openModalRoof = function () { openLabelOther("ROOF", { title:"Ajouter une toiture", placeholder:"Entrer une toiture" }, "roofs", "roof"); }
    $scope.openModalWall = function () { openLabelOther("WALL", { title:"Ajouter un mur", placeholder:"Entrer un type de mur" }, "walls", "wall"); }
    $scope.openModalInsulation = function () { openLabelOther("INSULATION", { title:"Ajouter une isolation", placeholder:"Entrer un type d'isolation" }, "insulations", "insulation"); }
    $scope.openModalParking = function () { openLabelOther("PARKING", { title:"Ajouter un stationnement", placeholder:"Entrer un type de stationnement" }, "parkings", "parking"); }
    /**
     * Open model to add a label
     * @param labelType type of label
     * @param labels labels to display on layer
     * @param vScopeName name of array variable into scope (generally ending with "s")
     * @param vPrpName name of variable into scope save data and real property representing the label
     */
    function openLabelOther(labelType, labels, vScopeName, vPrpName) {
        var modalInstance = $modal.open({
            templateUrl: 'modalAddType.html',
            controller: ModalLabelInstanceCtrl,
            resolve: {
                labels: function(){
                    return labels;
                },
                currentElt: function () {
                    return labelType;
                }
            }
        });

        modalInstance.result.then(function (param) {
            $log.debug('Modal closed');
            $scope[vScopeName].push(param);
            $scope.saveData[vPrpName] = param;
        }, function () {
            $log.debug('Modal dismissed');
            $scope.saveData[vPrpName] = null;
        });
    }
}

var ModalTypeInstanceCtrl = function ($scope, $modalInstance, ServiceConfiguration, $http, currentElt, labels) {
    $scope.newType = {"label" : null}; // Have to use object else in function ok value is not up to date
    $scope.labels =labels;

    $scope.ok = function () {
        var label = $scope.newType.label;

        $http.put(ServiceConfiguration.API_URL+"/rest/category/"+currentElt+"/types/"+label).success(function (data) {
            $modalInstance.close(data);
        });
    };
};

var ModalLabelInstanceCtrl = function ($scope, $modalInstance, ServiceConfiguration, $http, currentElt, labels) {
    $scope.newType = {"label" : null}; // Have to use object else in function ok value is not up to date
    $scope.labels =labels;

    $scope.ok = function () {
        var label = {
            "techid": null,
            "type": currentElt,
            "label": $scope.newType.label
        }

        $http.post(ServiceConfiguration.API_URL+"/rest/label/", label).success(function (data) {
            $modalInstance.close(data);
        });
    };
};
