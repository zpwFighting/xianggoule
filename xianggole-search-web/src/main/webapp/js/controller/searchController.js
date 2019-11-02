app.controller("searchController",function($scope,searchService){
	
	//定义搜索对象的结构
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':10};
	
	//搜索
	$scope.search = function(){
		$scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);
		searchService.search($scope.searchMap).success(function(response){
			
			$scope.resultMap = response;
			
			builtPageLabel();//构建分页栏
		});
	}
	//构建分页栏
	builtPageLabel=function(){
		//构建分页标签
		$scope.pageLabel=[];
		
		var firstPage=1;
		var lastPage=$scope.resultMap.totalPages;
		$scope.firstDot=true;
		$scope.lastDot=true;
		if($scope.resultMap.totalPages>5){
			if($scope.searchMap.pageNo<=3){
				lastPage=5;
				$scope.firstDot=false;
			}else if($scope.searchMap.pageNo>=$scope.resultMap.totalPages-2){
				firstPage=$scope.resultMap.totalPages-4;
				$scope.lastDot=false;
			}else{
				firstPage=$scope.searchMap.pageNo-2;
				lastPage=$scope.searchMap.pageNo+2;
				
			}
		}else{
			$scope.firstDot=false;
			$scope.lastDot=false;
		}
		for(var i=firstPage;i<=lastPage;i++){
			$scope.pageLabel.push(i);
		}
	}
	
	
	//添加搜索项
	$scope.addSearchItem=function(key,value){
		if(key=='category' || key=='brand' || key=='price'){
			$scope.searchMap[key]=value;
		}else{
			$scope.searchMap.spec[key]=value;
		}
		$scope.search();//查询
	}
	
	$scope.removeSearcItem=function(key){
		if(key=='category' || key=='brand' || key=='price'){
			$scope.searchMap[key]='';
		}else{
			delete $scope.searchMap.spec[key];
		}
	}
	//分页查询
	$scope.queryByPage=function(pageNo){
		if(pageNo<1 || pageNo>$scope.searchMap.totalPages){
			return ;
		}
		$scope.searchMap.pageNo=pageNo;
		$scope.search();
	}
	
	$scope.isTopPage=function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}else{
			return false;
		}
	}
	$scope.isEndPage=function(){
		if($scope.searchMap.pageNo==$scope.searchMap.totalPages){
			return true;
		}else{
			return false;
		}
	}
});