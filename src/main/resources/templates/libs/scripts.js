angular.module('birch', [])
.controller('MainCtrl', function($scope) {
	$scope.tabs = ["results","runtime","parameters"];
	$scope.tabActive = $scope.tabs[0];
	$scope.results = window.result;
	$scope.active = function(tab){
		$scope.tabActive = tab;
	};
})
.controller('ResultsCtrl', function($scope,$window) {
	$scope.selected = $scope.results.queryResults[0];
	$scope.select = function(el){
		$scope.selected = el;
	};
	$scope.openImage = function(url){
		$window.open(url, '_blank');
	};
});;