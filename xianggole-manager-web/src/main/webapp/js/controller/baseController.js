app.controller('baseController',function($scope){
	//重新加载列表数据
	$scope.reloadList=function(){
		$scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
	}
	//分页控制配件
	$scope.paginationConf={
		currentPage: 1,
	totalItems: 10,
	itemsPerPage: 10,
	perPageOptions: [10, 20, 30, 40, 50],
	onChange: function(){
						$scope.reloadList();//重新加载
	  }
	}
	//定义一个数组保存要删除的id
	$scope.selectIds=[];
	$scope.updateSelection = function($event,id){
		if($event.target.checked){
			$scope.selectIds.push(id);
		}else{
			var index = $scope.selectIds.indexOf(id);
			$scope.selectIds.splice(index,1);
		}
	}
});