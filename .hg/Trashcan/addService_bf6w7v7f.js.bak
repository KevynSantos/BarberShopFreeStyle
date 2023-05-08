function removeRequest(id)
{
	localStorage.removeItem(id);
	
	document.getElementById(id).remove();
	
	$(".count_requests").text(document.querySelectorAll(".requests_content td a").length);
	

}

function addRequest(name,time,money,id)
{
	
	var exists = false;
	
	if(localStorage.getItem(id) == null)
	{
		localStorage.setItem(id,id);
		exists = true;
	}
	
	if(exists)
	{
		var timeStr = time==null?"N/A":time;
		
		$(".initRequestEmpty").remove();
		
		$(".requests_content").append("<tr id='"+id+"'><td>"+name+"</td><td>"+timeStr+"</td><td>"+money+"</td><td><a href='#' idRequest='"+id+"' id='"+id.split('idRequest')[1]+"' Onclick=(removeRequest('"+id+"'))><img src='img/minus.png' width='20' height='20'/></a></td></tr>");
		
		$(".count_requests").text(document.querySelectorAll(".requests_content td a").length);
	}
	
	else
	{
		toastr.error('Elemento já adicionado!');
	}
	
	
	
}

function AddService()
{
	
	var mask = function()
	{
		$("#text_cpf").mask('000.000.000-00', {reverse: true});
	};
	
	var resetRequests = function()
	{
		
		$(".btn_filter_submit").click(function(){
			
			$("#result_requests").html("");
			
			$(".requests_content tbody").html("<tr><th>Nome</th><th>Tempo</th><th>Preço</th><th>Ações</th></tr><tr class='initRequestEmpty'><td>--</td><td>--</td><td>--</td></tr>");
			
			$(".count_requests").text(document.querySelectorAll(".requests_content td a").length);
			
			$(".name_source_request").val("");
			
			localStorage.clear();
			
			
			
		});
		
	
		
	};
	
	var showOrHideModalRequest = function()
	{
		
		$(".requests").click(function(){
			
			$("#addRequest").modal();
			
		});
		
		$(".btn_ok").click(function(){
			
			$("#addRequest").modal('hide');
			
		});
		
		
	};
	

	
	var sourceRequest = function()
	{
		
		$(".btn_source_request").click(function(){
			
			var name = $(".name_source_request").val();
			
			$.ajax({
			     url : contextPath + "/request/getByName",
			     type : 'GET',
			     data : {
			         name : name
			     },
			     beforeSend : function(){
			          //none
			     },
			     success: function(response) { 
			            
			            
			            response = JSON.parse(response);
			            
			            
			            
			            var requests = response.requests;
			            
			            if(requests.length > 0)
			            {
			            	var table = "<table border='2'>";
				            var line = "<tr><th>Nome</th><th>Tempo</th><th>Preço</th><th>Ações</th></tr>";
			            	
			            	for(var request of requests)
			            	{
			            		var nameRequest = request.pedido.nome;
			            		
			            		var tempoStr = request.pedido.tempo==null?"N/A":request.pedido.tempo;
			            		line += "<tr><td>"+request.pedido.nome+"</td><td>"+tempoStr+
			            		
			            		"</td><td>"+request.pedido.preco+"</td><td><a href='#' onClick=\"addRequest('"+nameRequest+"','"+tempoStr+"','"+request.pedido.preco+"','idRequest"+request.pedido.id+"');\" ><img src='img/plus-button.png' height='50' width='50'/></a></td></tr>";
			            	}
			            	
			                table += line + "</table>";
				            
				            $("#result_requests").html(table);
			            		
			            }
			            
			            else
			            {
			            	toastr.error('Nenhum registro encontrado!');
			            	$("#result_requests").html("");
			            }
			            			
			     }
			})
			.fail(function(jqXHR, textStatus, msg){
			     error.show(msg);
			});
			
		});
		
	};
	
	var getClientByCPF = function(cpf)
	{
		var response;
		
			$.ajax({
			     url : contextPath + "/client/getClientByCPF",
			     type : 'GET',
			     data : {
			    	 cpf : cpf
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
			
			return response.client;
		
	};
	
	var saveService = function()
	{
		
		$(".btn_service_add").click(function(){
			
			var checkRequest = true;
			if(parseInt($(".count_requests").text())==0)
			{
				toastr.warning('Adicione pelo menos um pedido');
				$(".img_requests").css({filter : "invert(30%)"});
				setTimeout(function() {
					$(".img_requests").css({filter : "invert(0)"});
				}, 1000);
				checkRequest = false;
			}
			var checkCPF = true;
			if($("#text_cpf").val()=="")
			{
				toastr.warning('Digite o CPF');
				$("#text_cpf").focus();
				checkCPF = false;
			}
			
			
			var checkStatus = true;
			if($(".select_status option:selected").text()=="Selecione uma opção")
			{
				toastr.warning('Escolha um status do serviço');
				$(".select_status").focus();
				checkStatus = false;
			}
			
			var checkSourceCPF = true;
			
			if(getClientByCPF($("#text_cpf").val()) == null)
			{
				toastr.warning('CPF do cliente não encontrado');
				$("#text_cpf").focus();
				checkSourceCPF = false;
			}
			
			if(checkRequest && checkCPF  && checkStatus && checkSourceCPF)
			{
			
				
				var cpf = $("#text_cpf").val();
				var status = $(".select_status option:selected").val();
				var descricao = $("#text_descricao").val();
				
				var links = document.querySelectorAll(".requests_content tr a");
				
				var pedidos = [];
				
				links.forEach(function(a){
					pedidos.push(a.getAttribute("id"));
				});
				
				var formData = new FormData();
				
				
				formData.append("cpf",cpf);
				formData.append("status",status);
				formData.append("pedidos",pedidos);
				formData.append("descricao",descricao);
			
				
				$.ajax({
				     url : contextPath + "/service/add",
				     type : 'POST',
				     data : formData,
				     async: false,
				     processData: false,
				     contentType: false,
				     beforeSend : function(){
				          //none
				     },
				     success: function(data) { 
				            
				    	 var response = JSON.parse(data);
				    	 
				    	 if(response.status=='success')
				         {
				    		 toastr.success('Cadastrado com sucesso!');
					    	 
					    	 setTimeout(function() {
								  window.location.reload(1);
							}, 3000);
				         }
				    	 
				    	 else
				         {
				    		 toastr.error('Houve um erro durante a solicitação!');
				    		 error.show(response.status);
				         }
				    	 
				            			
				     }
				})
				.fail(function(jqXHR, textStatus, msg){
				     toastr.error('Houve um erro durante a solicitação!');
				     error.show(msg);
				});
			
			}
			
			
			
		});
			
		
		
	};
	
	this.init = function()
	{
		
		
		
		toastr.options = {
				  "closeButton": false,
				  "debug": false,
				  "newestOnTop": false,
				  "progressBar": false,
				  "positionClass": "toast-bottom-right",
				  "preventDuplicates": false,
				  "onclick": null,
				  "showDuration": "300",
				  "hideDuration": "1000",
				  "timeOut": "5000",
				  "extendedTimeOut": "1000",
				  "showEasing": "swing",
				  "hideEasing": "linear",
				  "showMethod": "fadeIn",
				  "hideMethod": "fadeOut"
		};
		
		localStorage.clear();
		
		mask();
		
		showOrHideModalRequest();
		
		sourceRequest();
		
		resetRequests();
		
		saveService();
		
		ClientModal.init();
		
		
	
	}
}

try
{
	var addService = new AddService();
	var error = new Error();
	var $document = $( document );
	$document.ready( addService.init );
}
catch ( exception )
{
	console.error( 'addService.js:'+exception );
}