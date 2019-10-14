	app.controller('brandController', function($scope,$controller,brandService) {
		$controller('baseController',{$scope:$scope})
		//查询所有品牌列表
		$scope.findAll = function() {
			brandService.findAll().success(function(resule) {
				$scope.list = resule;
			});
		}
		
	
		//分页
// 		$scope.findPage = function(page,size){
// 			$http.get('../brand/findPage.do?pageNum='+page+'&pageSize='+size).success(
// 			function(response){
// 				$scope.list=response.rows;	
// 				$scope.paginationConf.totalItems=response.total;//更新总记录数
// 			}			
// 	);
// 		}
		//添加 or 更改
		$scope.save = function(){
			var object = null;
			if($scope.entity.id==null){
			  object = brandService.add($scope.entity);
			}else{
				object = brandService.update($scope.entity);
			}
			object.success(function(response){
							if(response.success){
										$scope.reloadList();
									}else{
										alert(response.message);
									}
			   });
			
			
		}
		
		//查询一个商品信息
		$scope.findOne = function(id){
			brandService.findOne(id).success(function(result){
				 $scope.entity = result;
			});
		}
		
		
		//删除
		$scope.delet=function(){
			brandService.delet($scope.selectIds).success(function(result){
				 if(result.success){
				 			$scope.reloadList();
				 		}else{
				 			alert(result.message);
				 		}
			});
		}
		//查询
		$scope.searchEntity={};
		$scope.search=function(page,size){
				brandService.search(page,size,$scope.searchEntity).success(function(result){
					$scope.list=result.rows;	
					$scope.paginationConf.totalItems=result.total;
				});
		}
		
	});