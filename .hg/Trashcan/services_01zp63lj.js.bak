function removeRequest(id)
{
	localStorage.removeItem(id);
	
	document.getElementById(id).remove();
	
	$(".count_requests").text(document.querySelectorAll(".requests_content td a").length);
	

}

function editServices(itemId)
{
	
	serviceId = itemId;
	
	$("#editServices").modal();
	
	$(".btn_close").click(function(){
		
		$("#editServices").modal('hide');
		
	});
	
	let requestServices = new RequestServices();
	requestServices.setIdService(itemId);
	var service = requestServices.getServiceById();
	
	$("#text_edit_cpf").val(service.cliente.cpf);
	
	$("#text_edit_status").val(service.status);
	
	$("#idServiceUpdate").val(itemId);
	
	
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



function showEraseService(item)
{
	
	$("#idServiceErase").val(item);
	$("#eraseModal").modal();
}

function showDescription(item)
{
	
	
	var description = $("#descriptionHidden"+item).val();
	
	$("#descriptionSpan").html(description);
	
	$("#descriptionModal").modal();
	
	
}

function formatDateTime(data){
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
	return diaF+"/"+mesF+"/"+anoF+" "+hourF+":"+minuteF+":"+secondF;
}



var serviceId = null;

function Services(){
	
	var showOrHideFilter = function()
	{
		$(".btn_filter").click(function(){
			
			$("#filterServices").modal();
			
		});
		
		$(".btn_ok").click(function(){
			
			$("#filterServices").modal('hide');
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
	
	var getClientByCPF = function(cpf)
	{
		var response;
		
			$.ajax({
			     url : "/client/getClientByCPF",
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
	
	var updateService = function(valor,cpf,status)
	{
		
		
		$.ajax({
		     url : "/service/update",
		     type : 'PUT',
		     data : {
		    	 
		    	 serviceId : serviceId,
		    	 cpf : cpf,
		    	 valor : valor,
		    	 status : status
		     },
		     async: false,
		     beforeSend : function(){
		          //none
		     },
		     success: function(data) { 
		            
		    	 var response = JSON.parse(data);
		    	 
		    	 if(response.status == "SUCESSO")
		    	 {
		    		 toastr.success("Serviço atualizado com sucesso!");
		    		 
		    		 setTimeout(function(){
		    			 location.href = "/services";
			    		}, 3000);
		    	 }
		    	 
		    	 else
		    	 {
		    		 toastr.error(response.status);
		    	 }
		    	 
		    	 
		    	 
		    	
		            			
		     }
		})
		.fail(function(jqXHR, textStatus, msg){
			error.show(msg);
		});
		
		
	};
	
	var checkFildsSetServices = function()
	{
		$(".btn_update").click(function(){
			
			var valor = $("#text_edit_valor").val();
			
			var cpf = $("#text_edit_cpf").val();
			
			var status = $("#text_edit_status").val();
			
			
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
				if(valor == "")
				{
					checkUpdate = false;
				}
				
				if(status == "")
				{
					checkUpdate = false;
				}
				
				if(checkUpdate)
				{
					updateService(valor,cpf,status);
				}
				else
				{
					toastr.warning("Preecnha todos os campos!");
				}
			}
			
		});
	}
	
	var hideDescriptionAndRequests = function()
	{
		$(".btn_ok_requests_show").click(function(){
			
			$("#requestModal").modal('hide');
			
		});
		
		$(".btn_ok_description").click(function(){
			
			$("#descriptionModal").modal('hide');
			
		});
	};
	
	var hideEraseModal = function()
	{
		$(".btn_ok_close_erase").click(function(){
			
			$("#eraseModal").modal("hide");
			
		});
			
	};
	
	var checkDeleteSucess = function()
	{
		var url = document.URL;
		
		if(url.indexOf('deleteSuccess') != -1)
		{
			toastr.success('Serviço removido com sucesso!');
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
	
	var sourceRequest = function()
	{
		
		$(".btn_source_request").click(function(){
			
			var name = $(".name_source_request").val();
			
			$.ajax({
			     url : "/request/getByName",
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
			
			$('body').css('background-image', 'url(/img/barbearia-fundo-index-orig.jpg)');
			
			
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
			
			paginationListBuilder.setUrl("/service/json");
			paginationListBuilder.setPaginationContainer(".pagination-container");
			paginationListBuilder.setFormSerialize("#form_filterServices");
			paginationListBuilder.setLocator("servicos");
			paginationListBuilder.setTableGrid('#registros_tabela');
			paginationListBuilder.setCountResult("#count_results");
			paginationListBuilder.setNameFileExcel("Servicos");
			paginationListBuilder.setContextDownloadExcel("/service/xls");
			paginationListBuilder.setNameFilePdf("Servicos");
			paginationListBuilder.setContextDownloadPdf("/service/pdf");
			
			paginationListBuilder.build();
			
			$(".select_count").change(function(){
				
				paginationListBuilder.build(this.value);
			});
			
			$("#btn_filter_services").click(function(){
				
				paginationListBuilder.build($(".select_count").val());
				
				$("#filterServices").modal('hide');
				
			});
			
			showOrHideFilter();
			
			$( "#data_final" ).datepicker({
				 dateFormat: 'dd/mm/yy',
				 monthNames: [ "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" ]
			 });
			 $( "#data_inicial" ).datepicker({
				 dateFormat: 'dd/mm/yy',
				  monthNames: [ "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" ]
			 });
			 
			 mask();
			 
			 hideDescriptionAndRequests();
			 
			 hideEraseModal();
			 
			 checkDeleteSucess();
			 
			 resetRequests();
			 
			 sourceRequest();
			 
			 updateRequest(paginationListBuilder);
			 
			 checkFildsSetServices();
			 
			 $(".btn_excel").click(function(){
				
				 paginationListBuilder.downloadExcel($(".select_count").val());
				 
			 });
			 
			 $(".btn_pdf").click(function(){
					
				 paginationListBuilder.downloadPdf($(".select_count").val());
				 
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
	var services = new Services();
	var $document = $( document );
	$document.ready( services.init );
}
catch ( exception )
{
	console.error( 'services.js:'+exception );
}