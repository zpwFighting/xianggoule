app.controller('indexController',function($scope,loginService){
	$scope.showLoginName = function(){
		loginService.loginName().success(function(responce){
			$scope.loginName=responce.loginName;
		});
	}
});