function getSizeRequests()
{
		var response = null;
		
		$.ajax({
		     url : contextPath + "/request/getCountRequests",
		     type : 'GET',
		     data : {
		    	
		     },
		     async: false,
		     beforeSend : function(){
		          //none
		     },
		     success: function(data) { 
		            
		    	 response = JSON.parse(data);
		           
		            			
		     }
		})
		.fail(function(jqXHR, textStatus, msg){
			error.show(msg);
		});
		
		return parseInt(response.size);
}

class RequestServices
{
	setIdService(id)
	{
		this.id = id;
	}
	
	setListIdRequests(listIdRequests)
	{
		this.listIdRequests = listIdRequests;
	}
	
	static clearBag()
	{
		
		var count = 1;
		
		var size = getSizeRequests();
		
		
		
		while(count < size)
		{
			if(localStorage.getItem("idRequest"+count) != null)
			{
				localStorage.removeItem("idRequest"+count);
			}
			count++;
		}
	}
	
	getRequestsByService()
	{
		var response = null;
		
		$.ajax({
		     url : contextPath + "/request/getRequestByService",
		     type : 'GET',
		     data : {
		    	 idService : this.id
		     },
		     async: false,
		     beforeSend : function(){
		          //none
		     },
		     success: function(data) { 
		            
		    	 response = JSON.parse(data);
		           
		            			
		     }
		})
		.fail(function(jqXHR, textStatus, msg){
			error.show(msg);
		});
		
		
		return response.requests;
	}
	
	updateRequest()
	{
		var formData = new FormData();
		formData.append("idService",this.id);
		formData.append("idRequests",this.listIdRequests);
		
		$.ajax({
		     url : contextPath + "/requestService/update",
		     type : 'PUT',
		     data : formData,
		     async: false,
		     processData: false,
		     contentType: false,
		     beforeSend : function(){
		          //none
		     },
		     success: function(data) { 
		            
		    	 var response = JSON.parse(data);
		    	 
		    	
		    	 
		    	 switch(response.status.message)
		    	 {
				     case 'EMPTY_ORDER_LIST':
		    			 toastr.warning("Deve conter pelo menos 1 pedido!");
		    			 break;
				     case 'SCHEDULE_UNAVAILABLE_WITH_REQUEST':
		    			 toastr.warning("Agendamento indisponível!");
		    			 break;
				     case 'ONLY_PRODUCT_IN_SCHEDULING':
				    		toastr.warning("Não permitido apenas produto!");
				    		break;
		    		 case 'SUCCESS':
		    			 toastr.success('Pedido atualizado com sucesso!');
		    			 break;
		    		 default:
		    			 toastr.error('Houve um erro durante a solicitação!');
		    		 	 error.show(response.status.message);
		    	 }
		    	 
		    	
		    	 
		            			
		     }
		})
		.fail(function(jqXHR, textStatus, msg){
		     toastr.error('Houve um erro durante a solicitação!');
		     error.show(msg);
		});
	}
	
	getServiceById()
	{
		var response = null;
		
		$.ajax({
		     url : contextPath + "/service/getById",
		     type : 'GET',
		     data : {
		    	 serviceId : this.id
		     },
		     async: false,
		     beforeSend : function(){
		          //none
		     },
		     success: function(data) { 
		            
		    	 response = JSON.parse(data);
		           
		            			
		     }
		})
		.fail(function(jqXHR, textStatus, msg){
			error.show(msg);
		});
		
		return response.service;
	}
	
	
}