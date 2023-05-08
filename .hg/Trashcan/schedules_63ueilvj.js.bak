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
		
		$(".requests_content").append("<tr id='"+id+"' class='registry'><td>"+name+"</td><td>"+timeStr+"</td><td>"+money+"</td><td><a href='#' idRequest='"+id+"' id='"+id.split('idRequest')[1]+"' Onclick=(removeRequest('"+id+"'))><img src='img/minus.png' width='20' height='20'/></a></td></tr>");
		
		$(".count_requests").text(document.querySelectorAll(".requests_content td a").length);
	}
	
	else
	{
		toastr.error('Elemento já adicionado!');
	}
	
	
	
}

var serviceId = null;

var calculateTime = null;

var formatLocalDate = function(data){
		let dia  = data.getDate().toString();
		let diaF = (dia.length == 1) ? '0'+dia : dia;
				let mes  = (data.getMonth()+1).toString(); //+1 pois no getMonth Janeiro começa com zero.
				let mesF = (mes.length == 1) ? '0'+mes : mes;
						let anoF = data.getFullYear();
						let hour  = data.getHours().toString();
						let hourF = (hour.length == 1) ? '0'+hour : hour;
								let minute  = data.getMinutes().toString();
								let minuteF = (minute.length == 1) ? '0'+minute : minute;
										let second  = data.getSeconds().toString();
										let secondF = (second.length == 1) ? '0'+second : second;
		return diaF+"/"+mesF+"/"+anoF;
}

var formatDateUsToEditScheduling = function(data)
{
	let dia  = data.getDate().toString();
	let diaF = (dia.length == 1) ? '0'+dia : dia;
			let mes  = (data.getMonth()+1).toString(); //+1 pois no getMonth Janeiro começa com zero.
			let mesF = (mes.length == 1) ? '0'+mes : mes;
					let anoF = data.getFullYear();
					let hour  = data.getHours().toString();
					let hourF = (hour.length == 1) ? '0'+hour : hour;
							let minute  = data.getMinutes().toString();
							let minuteF = (minute.length == 1) ? '0'+minute : minute;
									let second  = data.getSeconds().toString();
									let secondF = (second.length == 1) ? '0'+second : second;
	return anoF+"-"+mesF+"-"+diaF;
}

function editSchedules(itemId)
{
	serviceId = itemId;
	
	$("#editSchedules").modal();
	
	$(".btn_close").click(function(){
		
		$("#editSchedules").modal('hide');
		
	});
	
	var service = null;
	
	$.ajax({
	     url : contextPath + "/scheduling/getById",
	     type : 'GET',
	     data : {
	    	 idService : itemId
	     },
	     async: false,
	     beforeSend : function(){
	          //none
	     },
	     success: function(response) { 
	            
	    	 response = JSON.parse(response);
	    	 
	    	 service = response.service;
	           
	            			
	     }
	})
	.fail(function(jqXHR, textStatus, msg){
	     error.show(msg);
	});
	
	$("#text_edit_cpf").val(service.cliente.cpf);
	
	$("#edit_hora").val(service.agendamento.hora);
	
	calculateTime.initChange(formatDateUsToEditScheduling(new Date(service.agendamento.data)));
	
}

function checkin(item)
{
	$("#idSchedulesCheckin").val(item);
	$("#checkinModal").modal();
}

function showEraseSchedules(item)
{
	
	$("#idSchedules").val(item);
	$("#eraseModal").modal();
}

function sheduledFinal(item)
{
	$("#idServiceFinal").val(item);
	$("#finalServiceModal").modal();
}

function showDescription(item)
{
	
	
	var description = $("#descriptionHidden"+item).val();
	
	$("#descriptionSpan").html(description);
	
	$("#descriptionModal").modal();
	
	
}



function showRequest(item)
{
	
	var requestServices = new RequestServices();
	requestServices.setIdService(item);
	var requests = requestServices.getRequestsByService();
	
	$("#idServiceToUpdateRequest").val(item);
	
	for(let request of requests)
	{
		addRequest(request.nome,request.tempo,request.preco,"idRequest"+request.id);
	}
	
	$("#requestModal").modal();
	
	
}

function createGridRequests(requests)
{
	var html = "<table border='1'>";
	
	html += "<tr><th>Nome</th><th>Tempo</th><th>Preço</th></tr>";
	
	for(var request of requests)
	{
		html += "<tr><td>"+request[0].nome+"</td>"+"<td>"+request[0].tempo+"</td>"+"<td>"+request[0].preco+"</td></tr>";
	}
	
	if(requests.length == 0)
	{
		html = "Não há registros disponíveis";
	}
	
	else
	{
		html += "</table>";
	}
	
	return html;
}

function createJsonRequest(requests)
{
	
	var result = [];
	
	var requestsArray = requests.split('|');
	
	for(var request of requestsArray)
	{
		if(request != "")
		{
			request = request.split('&');
			var json = [
				{
					"nome":request[2].split('nome=')[1],
					"tempo":request[3].split('tempo=')[1]
				}
			]
			
			result.push(json);
		}
	}
	
	return result;
}

function Schedules()
{
	
	var hideDescriptionAndRequests = function()
	{
		$(".btn_ok_requests_show").click(function(){
			
			$("#requestModal").modal('hide');
			
		});
		
		$(".btn_ok_description").click(function(){
			
			$("#descriptionModal").modal('hide');
			
		});
	};
	
	var showOrHideFilter = function()
	{
		$(".btn_filter").click(function(){
			
			$("#filterSchedules").modal();
			
		});
		
		$(".btn_ok").click(function(){
			
			$("#filterSchedules").modal('hide');
		});
	};
	
	var updateRequest = function(paginationListBuilder)
	{
		
		$(".btn_update_request").click(function(){
			
			let requestServices = new RequestServices();
			
			requestServices.setIdService($("#idServiceToUpdateRequest").val());
			var requests = [];
			$(".requests_content .registry").each(function(){

				requests.push($(this).attr("id").split('idRequest')[1]);
			});
			
			requestServices.setListIdRequests(requests);
			requestServices.updateRequest();
			
			$(".btn_ok_requests_show").click();
			
			paginationListBuilder.build($(".select_count").val());
			
		});
		
	};
	
	var mask = function()
	{
		$("#text_cpf").mask('000.000.000-00', {reverse: true});
		
		$("#text_valor").maskMoney({symbol:'R$ ', 
			showSymbol:true, thousands:'.', decimal:',', symbolStay: true});
		
		$("#text_edit_cpf").mask('000.000.000-00', {reverse: true});
		
		$("#text_cpf_employee").mask('000.000.000-00', {reverse: true});
		
		$("#text_edit_valor").maskMoney({symbol:'R$ ', 
			showSymbol:true, thousands:'.', decimal:',', symbolStay: true});
		
	};

	
	var hideEraseModal = function()
	{
		$(".btn_ok_close_erase").click(function(){
			
			$("#eraseModal").modal("hide");
			
		});
			
	};
	
	var hideCheckinModal = function()
	{
		$(".btn_ok_close_checkin").click(function(){
			
			$("#checkinModal").modal("hide");
			
		});
	}
	
	var checkDeleteSucess = function()
	{
		var url = document.URL;
		
		if(url.indexOf('deleteSuccess') != -1)
		{
			toastr.success('Agendamento removido com sucesso!');
		}
	}
	
	var checkCheckinSucess = function()
	{
		var url = document.URL;
		
		if(url.indexOf('checkinSuccess') != -1)
		{
			toastr.success('Checkin realizado com sucesso!');
		}
	}
	
	var hideScheduledFinal = function()
	{
		
		$(".btn_ok_close_final_scheduled").click(function(){
			
			$("#finalServiceModal").modal("hide");
			
		});
	}
	
	var checkScheduledFinal = function()
	{
		var url = document.URL;
		
		if(url.indexOf('sheduledFinal') != -1)
		{
			toastr.success('Agendamento finalizado com sucesso!');
		}
	}
	
	var resetRequests = function()
	{
		
		$(".btn_ok_requests_show").click(function(){
			
			$("#result_requests").html("");
			
			$(".requests_content tbody").html("<tr><th>Nome</th><th>Tempo</th><th>Preço</th><th>Ações</th></tr><tr class='initRequestEmpty'><td>--</td><td>--</td><td>--</td><td>--</td></tr>");
			
			$(".count_requests").text(document.querySelectorAll(".requests_content td a").length);
			
			$(".name_source_request").val("");
			
			RequestServices.clearBag();
			
			$("#requestModal").modal('hide');
			
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
			            	toastr.error('Nenhum pedido encontrado!');
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
	
	var updateSchedules = function(cpf,data,hora)
	{
		
		
		$.ajax({
		     url : contextPath + "/scheduling/update",
		     type : 'POST',
		     data : {
		    	 
		    	 serviceId : serviceId,
		    	 cpf : cpf,
		    	 data : data,
		    	 hora : hora
		     },
		     async: false,
		     beforeSend : function(){
		          //none
		     },
		     success: function(data) { 
		            
		    	 
		    	 var response = JSON.parse(data);
		    	 
		    	
		    	 
		    	 switch(response.status.message)
		    	 {
				     case 'SCHEDULING_INVALID':
				    	 toastr.warning("Agendamento indisponível!");
				    	 break;
		    		 case 'SUCCESS':
		    			 toastr.success('Agendamento atualizado com sucesso!');
		    			 setTimeout(function(){
				    		   window.location.reload(1);
				    		}, 3000);
		    			 break;
		    		 default:
		    			 toastr.error('Houve um erro durante a solicitação!');
		    		 	 error.show(response.status.message);
		    	 }
		    	 
		    	
		            			
		     }
		})
		.fail(function(jqXHR, textStatus, msg){
			error.show(msg);
		});
		
		
	};
	
	var checkFildsSetSchedules = function()
	{
		$(".btn_update").click(function(){
			
			
			
			var cpf = $("#text_edit_cpf").val();
			
			var data = $("#edit_data").val();
			
			var hora = $("#edit_hora").val()+":00";
			
			if(cpf == "")
			{
				toastr.warning("Campo CPF obrigatório!");
				return;
			}
			
			var cliente = getClientByCPF(cpf);
			
			if(cliente == null)
			{
				toastr.warning("CPF do cliente inexistente!");
			}
			
			else
			{
				var checkUpdate = true;
				
				if(data == "")
				{
					checkUpdate = false;
				}
				
				if(hora == "")
				{
					checkUpdate = false;
				}
				
				if(checkUpdate)
				{
					updateSchedules(cpf,data,hora);
				}
				else
				{
					toastr.warning("Preecnha todos os campos!");
				}
			}
			
		});
	}
	
	this.init = function()
	{
		
		RequestServices.clearBag();
		try
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
			
			
			
			$('body').loadingModal({
		  		  position: 'auto',
		  		  text: 'Carregando',
		  		  color: '#fff',
		  		  opacity: '0.7',
		  		  backgroundColor: 'rgb(0,0,0)',
		  		  animation: 'doubleBounce'
		  	});
			
			
			let rememberFilter = new RememberFilter();
			rememberFilter.init();
			
			if(rememberFilter.isEmpty() == true)
			{
				var utils = new Utils();
				utils.setNowInDateFromFilter("#data_inicial");
				utils.setNowInDateFromFilter("#data_final");
			}
			
			var paginationListBuilder = new PaginationListBuilder();
			
			paginationListBuilder.setUrl("/scheduling/json");
			paginationListBuilder.setPaginationContainer(".pagination-container");
			paginationListBuilder.setFormSerialize("#form_filterSchedules");
			paginationListBuilder.setLocator("agendamentos");
			paginationListBuilder.setTableGrid('#registros_tabela');
			paginationListBuilder.setCountResult("#count_results");
			paginationListBuilder.setNameFileExcel("Agendamentos");
			paginationListBuilder.setContextDownloadExcel("/scheduling/xls");
			paginationListBuilder.setNameFilePdf("Agendamentos");
			paginationListBuilder.setContextDownloadPdf("/scheduling/pdf");
			paginationListBuilder.build();
			
			$(".select_count").change(function(){
				
				paginationListBuilder.build(this.value);
			});
			
			$("#btn_filter_schedules").click(function(){
				
				paginationListBuilder.build($(".select_count").val());
				
				$("#filterSchedules").modal('hide');
				
			});
			
			showOrHideFilter();
			 
			 mask();
			 
			 hideDescriptionAndRequests();
			 
			 hideEraseModal();
			 
			 checkDeleteSucess();
			 
			 hideCheckinModal();
			 
			 checkCheckinSucess();
			 
			 hideScheduledFinal();
			 
			 checkScheduledFinal();
			 
			 checkFildsSetSchedules();
			 
			 calculateTime = new CalculateTime();
			 calculateTime.setFieldTime("#edit_hora");
			 calculateTime.setFieldDate("#edit_data");
			 calculateTime.setFinalTime(false);
			 calculateTime.calc();
			 
			 resetRequests();
			 
			 sourceRequest();
			 
			 updateRequest(paginationListBuilder);
			 
			 $(".btn_excel").click(function(){
					
				 paginationListBuilder.downloadExcel($(".select_count").val());
				 
			 });
			 
			 $(".btn_pdf").click(function(){
					
				 paginationListBuilder.downloadPdf($(".select_count").val());
				 
			 });
			 
			 $( "#data_final" ).datepicker({
				 dateFormat: 'dd/mm/yy',
				 monthNames: [ "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" ]
			 });
			 $( "#data_inicial" ).datepicker({
				 dateFormat: 'dd/mm/yy',
				  monthNames: [ "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" ]
			 });
			 
		}
		catch ( exception )
		{
			console.error( 'this.init:'+exception );
		}
	};
}

try
{
	var schedules = new Schedules();
	var $document = $( document );
	$document.ready( schedules.init );
}
catch ( exception )
{
	console.error( 'schedules.js:'+exception );
}