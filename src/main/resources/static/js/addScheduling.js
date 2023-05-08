
function removeRequest(id)
{
	localStorage.removeItem(id);
	
	document.getElementById(id).remove();
	
	$(".count_requests").text(document.querySelectorAll(".requests_content td a").length);
	
	if(parseInt($(".count_requests").text())>0)
	{
		$("#text_hora_inicial").removeAttr('disabled');
		
		calculateIntervalScheduling();
	}
	
	else
	{
		$("#text_hora_inicial").attr("disabled","disabled");
	}
	

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
	
	if(parseInt($(".count_requests").text())>0)
	{
		$("#text_hora_inicial").removeAttr('disabled');
		
		calculateIntervalScheduling();
	}
	
	else
	{
		$("#text_hora_inicial").attr("disabled","disabled");
	}
	
}

var getSumTimeRequestByIds = function(ids)
{
	var hour = $("#text_hora_inicial").val()==""?"00:00":$("#text_hora_inicial").val();
	
	var countTime
	
	$.ajax({
		     url : contextPath + "/request/getSumTimeRequestByIds?ids="+ids+"&hour="+hour,
		     type : 'GET',
		     async: false,
		     beforeSend : function(){
		          //none
		     },
		     success: function(response) { 
		            
		    	 response = JSON.parse(response);
		    	
		    	 countTime = response.countTime;
		            			
		     }
		})
		.fail(function(jqXHR, textStatus, msg){
		     error.show(msg);
		});
	
	return countTime;
}

var calculateIntervalScheduling = function()
{
	
	
		
		
		
		var links = document.querySelectorAll(".requests_content tr a");
		
		var pedidos = [];
		
		links.forEach(function(a){
			pedidos.push(a.getAttribute("id"));
		});
		
		var sumTime = getSumTimeRequestByIds(pedidos);
		
		$("#text_hora_final").val(sumTime.replace(':00',''));
		
	
		
	
	
}

function AddScheduling()
{
	
	var mask = function()
	{
		$("#text_cpf").mask('000.000.000-00', {reverse: true});
	};
	
	
	var resetRequests = function()
	{
		
		$(".btn_filter_submit").click(function(){
			
			$("#result_requests").html("");
			
			$(".requests_content tbody").html("<tr><th>Nome</th><th>Tempo</th><th>Preço</th><th>Ações</th></tr><tr class='initRequestEmpty'><td>--</td><td>--</td><td>--</td><td>--</td></tr>");
			
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
			            		var requestName = request.pedido.nome;
			            		var tempoStr = request.pedido.tempo==null?"N/A":request.pedido.tempo;
			            		line += "<tr><td>"+request.pedido.nome+"</td><td>"+tempoStr+
			            		"</td><td>"+request.pedido.preco+"</td><td><a href='#' onClick=\"addRequest('"+requestName+"','"+tempoStr+"','"+request.pedido.preco+"','idRequest"+request.pedido.id+"');\" ><img src='img/plus-button.png' height='50' width='50'/></a></td></tr>";
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
	
	var getHoursSchedulingByDate = function()
	{
		
		
		var schedules;
		
		$("#text_data").change(function(){
			
			
			var date = $("#text_data").val();
			
			$.ajax({
			     url : contextPath + "/scheduling/getByDate",
			     type : 'GET',
			     data : {
			    	 date : date
			     },
			     async: false,
			     beforeSend : function(){
			          //none
			     },
			     success: function(response) { 
			            
			    	 response = JSON.parse(response);
			    	 schedules = response.schedules;
			    	 
			    	 
			    	 
			         $(".table_scheduling .row_table_scheduling").remove();
			         
			         for(var scheduling of schedules)
			         {
			        	 $(".table_scheduling").append("<tr class='row_table_scheduling'>"+"<td>"+scheduling.horaInicial +"</td>"+"<td>"+scheduling.horaFinal +"</td>"+"</tr>");
			         }
			            			
			     }
			})
			.fail(function(jqXHR, textStatus, msg){
			     error.show(msg);
			});
			
		});
		
		
		
	}
	
	
	var saveScheduling = function()
	{
		
		$(".btn_scheduling_add").click(function(){
			
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
			
			
			
			var checkSourceCPF = true;
			
			if(getClientByCPF($("#text_cpf").val()) == null)
			{
				toastr.warning('CPF do cliente não encontrado');
				$("#text_cpf").focus();
				checkSourceCPF = false;
			}
			
			var date = true;
			if($("#text_data").val()=="")
			{
				toastr.warning('Escolha uma Data para o Agendamento');
				$("#text_data").focus();
				date = false;
			}
			
			var hour = true;
			if($("#text_hora_inicial").val()=="")
			{
				toastr.warning('Escolha uma Hora para o Agendamento');
				$("#text_hora_inicial").focus();
				hour = false;
			}
			
			if(checkRequest && checkCPF && checkSourceCPF && date && hour)
			{
			
				
				var cpf = $("#text_cpf").val();
				var descricao = $("#text_descricao").val();
				var dateStr = $("#text_data").val();
				var hourStr = $("#text_hora_inicial").val();
				
				
				var links = document.querySelectorAll(".requests_content tr a");
				
				var pedidos = [];
				
				links.forEach(function(a){
					pedidos.push(a.getAttribute("id"));
				});
				
				var formData = new FormData();
			
				formData.append("cpf",cpf);
				formData.append("pedidos",pedidos);
				formData.append("descricao",descricao);
				formData.append("data",dateStr);
				formData.append("hora",hourStr);
			
				
				$.ajax({
				     url : contextPath + "/scheduling/add",
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
				    	 
				    	 var listStatus = response.status;
				    	 
				    	 for(let i=0;i<listStatus.length;i++)
				    	 {
				    		
				    		 var status = listStatus[i];
						    	 switch(status.message)
						    	 {
						    		 case 'SUCCESS':
						    			 toastr.success('Cadastrado com sucesso!');
						    			 
								    	 
								    	 setTimeout(function() {
											  window.location.reload(1);
										}, 3000);
								    	break;
						    		 case 'SCHEDULING_INVALID':
						    			 toastr.warning('Agendamento não disponível para este horário!');
						    			 break;
						    		 case 'ONLY_PRODUCT_IN_SCHEDULING':
						    			 toastr.warning('Não permitido apenas produto!');
						    			 break;
						    		 default:
						    			 toastr.error('Houve um erro durante a solicitação!');
						    		 	 error.show(status);
						    	 }
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
		
		showOrHideModalRequest();
		
		sourceRequest();
		
		resetRequests();
		
		mask();
		
		saveScheduling();
		
		var calculateTime = new CalculateTime();
		calculateTime.setFieldDate("#text_data");
		calculateTime.setFieldTime('input.timepicker');
		calculateTime.init();
		calculateTime.calc();
		
		
		getHoursSchedulingByDate();
		
		ClientModal.init();
		
	}
}

try
{
	var addScheduling = new AddScheduling();
	var error = new Error();
	var $document = $( document );
	$document.ready( addScheduling.init );
}
catch ( exception )
{
	console.error( 'addScheduling.js:'+exception );
}