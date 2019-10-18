app.controller('loginController',function($scope,loginService){
	$scope.loginName = function(){
		loginService.loginName().success(function(responce){
			$scope.loginName = responce.loginName;
		});
	}
});