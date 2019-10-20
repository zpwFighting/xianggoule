 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,goodsService,uploadService,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	//添加 
	$scope.add=function(){			
		$scope.entity.goodsDesc.introduction=editor.html();
		var serviceObject;//服务层对象  				
		serviceObject=goodsService.add( $scope.entity  );//增加 
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
					alert(response.message);
						$scope.entity={};
						editor.html('');
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    //上传图片
		$scope.uploadFile=function(){
			uploadService.uploadFile().success(function(responce){
				if(responce.success){
					$scope.image_entity.url=responce.message;

				}else{
					alert(responce.message);
				}
			});
		}
		$scope.entity={goodsDesc:{itemImages:[]}};
		$scope.add_image_entity=function(){
			$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
		}
		$scope.remove_image_entity=function(index){
			$scope.entity.goodsDesc.itemImages.splice(index,1);
		}
		//itemCatService
		//查询一级商品分类
		$scope.selectItemCat1List=function(){
			itemCatService.findParentById(0).success(function(response){
				$scope.itemCat1List=response;
			});
		}
		
		//查询二级商品列表   监控entity.goods.category1Id值会不会发生改变
		$scope.$watch('entity.goods.category1Id',function(newValue,oldValue){
			itemCatService.findParentById(newValue).success(function(response){
				$scope.itemCat2List=response;
				$scope.entity.goods.category3Id={};
			});
		});
		//查询三级商品列表 
		$scope.$watch('entity.goods.category2Id',function(newValue,oldValue){
			itemCatService.findParentById(newValue).success(function(response){
				$scope.itemCat3List=response;
			});
		});
		//读取模板id 
		$scope.$watch('entity.goods.category2Id',function(newValue,oldValue){
			itemCatService.findOne(newValue).success(function(response){
				$scope.entity.goods.typeTemplateId=response.typeId;
			});
		});
		
		//读取模板id 后读取品牌列表
		$scope.$watch('entity.goods.typeTemplateId',function(newValue,oldValue){
			typeTemplateService.findOne(newValue).success(function(response){
				$scope.typeTemplate=response;
				$scope.typeTemplate.brandIds=JSON.parse($scope.typeTemplate.brandIds);//品牌列表类型转换
				
				$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.typeTemplate.customAttributeItems);
				
			});
		});
});	
