	app.service('brandService',function($http){
		this.findAll=function(){
			return $http.get('../brand/findAll.do');
		}
		this.findOne=function(id){
			return $http.get('../brand/findOne.do?id='+id);
		}
		this.add=function(entity){
			return $http.post('../brand/add.do',entity);
		}
		this.update=function(entity){
			return $http.post('../brand/update.do',entity);
		}
		this.delet =function(selectIds){
			return $http.get('../brand/delete.do?ids='+selectIds);
		} 
		this.search = function(page,size,searchEntity){
			return $http.post('../brand/search.do?page='+page+'&size='+size,searchEntity);
		}
	});