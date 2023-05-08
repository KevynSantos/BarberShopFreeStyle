var clientId = null;

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

function getLastName(name)
{
	
	
	var cutName = name.split(' ');
	
	var lastName = "";
	
	let count = 1;
	for(let x of cutName)
	{
		if(count == 1)
		{
			count++;
			continue;
		}
		
		if(count == 2)
		{
			lastName += x;	
		}
		
		else
		{
			lastName += " "+x;
		}
		
		count++;
	}
	
	return lastName;
}

function editClients(itemId)
{
	clientId = itemId;
	
	$("#editClients").modal();
	
	$(".btn_close").click(function(){
		
		$("#editClients").modal('hide');
		
	});
	
	$("#idClientUpdate").val(itemId);
	
	var cliente = null;
	
	$.ajax({
	     url : contextPath + "/client/getById",
	     type : 'GET',
	     data : {
	    	 idClient : itemId
	     },
	     async: false,
	     beforeSend : function(){
	          //none
	     },
	     success: function(response) { 
	            
	    	 response = JSON.parse(response);
	    	 cliente = response.cliente;
	           
	            			
	     }
	})
	.fail(function(jqXHR, textStatus, msg){
	     error.show(msg);
	});
	
	var name = cliente.nome.split(' ')[0];
	
	var sobrenome = getLastName(cliente.nome);
	
	$("#text_edit_nome").val(name);
	
	$("#text_edit_sobrenome").val(sobrenome);
	
	$("#text_edit_cpf").val(cliente.cpf);
	
	$("#text_edit_data_nascimento").val(formatDateUsToEditScheduling(new Date(cliente.dtNascimento)));
	
	$("#text_edit_email").val(cliente.email);
	
	$("#text_edit_telefone").val(cliente.telefone);
	
	$("#text_edit_endereco").val(cliente.endereco);
}

function showAdress(count)
{
	var adress = $(".adressInfo"+count).attr("adress");
	
	$("#addressInfoModal").modal();
	$("#lbl_info_adress").text(adress);
}

function showEraseClients(item)
{
	
	$("#idClientErase").val(item);
	$("#eraseModal").modal();
}

function Clients(){
	
	
	
	var checkEmail = function()
	{
		
		Validation.validEmail("#text_email,#text_new_email,#text_edit_email");	
	};
	
	var updateClient = function()
	{
		$(".btn_update").click(function(){
			
		
			
			var nome = true;
			var cpf = true;
			var dataNasc = true;
			var email = true;
			var telefone = true;
			var sobrenome = true;
			
			if($("#text_edit_nome").val() == "")
			{
				nome = false;
				toastr.warning('Preencha o campo Nome!');
			}
			
			if($("#text_edit_sobrenome").val() == "")
			{
				sobrenome = false;
				toastr.warning('Preencha o campo Sobrenome!');
			}
			
			if($("#text_edit_cpf").val() == "")
			{
				cpf = false;
				toastr.warning('Preencha o campo CPF!');
			}
			
			if($("#text_edit_data_nascimento").val() == "")
			{
				toastr.warning('Preencha o campo data de nascimento!');
				dataNasc = false;
			}
			
			if($("#text_edit_email").val() == "")
			{
				toastr.warning('Preencha o campo email');
				email = false;
			}
			
			if($("#text_edit_telefone").val() == "")
			{
				telefone = false;
				toastr.warning('Preencha o campo Telefone!');
			}
			
			
			
			
			if(nome && cpf && dataNasc && email && telefone && sobrenome)
			{
				
				var idClient = $("#idClientUpdate").val();
				var formData = new FormData();
				formData.append("nome",$("#text_edit_nome").val());
				formData.append("cpf",$("#text_edit_cpf").val());
				formData.append("dataNasc",$("#text_edit_data_nascimento").val());
				formData.append("email",$("#text_edit_email").val());
				formData.append("telefone",$("#text_edit_telefone").val());
				formData.append("endereco",$("#text_edit_endereco").val());
				formData.append("sobrenome",$("#text_edit_sobrenome").val());
				formData.append("idClient",idClient);
				
				$.ajax({
				     url : contextPath + "/client/update",
				     type : 'PUT',
				     data : formData,
				     async: false,
				     processData: false,
				     contentType: false,
				     beforeSend : function(){
				          //none
				     },
				     success: function(data) { 
				            
				    	 response = JSON.parse(data);

				    	 var listStatus = response.status;
				    	 
				    	 for(let i=0;i<listStatus.length;i++)
				    	 {
				    		
				    		 var status = listStatus[i];
						    	 switch(status.message)
						    	 {
						    		 case 'SUCCESS':
						    			 toastr.success('Atualizado com sucesso!');
								    	 
								    	 setTimeout(function() {
											  window.location.reload(1);
										}, 3000);
								    	break;
						    		 case 'CPF_INVALID':
						    			 toastr.warning('CPF inválido!');
						    			 break;
						    		 case 'TELEPHONE_INVALID':
						    			 toastr.warning('Telefone inválido!');
						    			 break;
						    		 case 'EMAIL_INVALID':
						    			 toastr.warning('E-mail inválido!');
						    			 break;
						    		 case 'NO_CHANGE':
						    			 toastr.warning('Sem alteração!');
						    			 break;
						    		 default:
						    			 toastr.error('Houve um erro durante a solicitação!');
						    		 	 error.show(status);
						    	 	}
				    	 		}
				     	}
			
			
			
					});
				}
		})
	};
	
	
	
	var showOrHideFilter = function()
	{
		$(".btn_filter").click(function(){
			
			$("#filterClients").modal();
			
		});
		
		$(".btn_ok").click(function(){
			
			$("#filterClients").modal('hide');
			$("#addressInfoModal").modal('hide');
		});
	};
	
	var mask = function()
	{
		$("#text_cpf").mask('000.000.000-00', {reverse: true});
		
		$("#text_new_cpf").mask('000.000.000-00', {reverse: true});
		
		$("#text_edit_cpf").mask('000.000.000-00', {reverse: true});
	
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
	
	var saveClient = function()
	{
		
		$(".btn_submit_client").click(function(){
			
			var checkName = true;
			if($("#text_new_name").val()=="")
			{
				toastr.warning('Digite o Nome');
				$("#text_new_name").focus();
				checkName = false;
			}
			
			var checkLastName = true;
			if($("#text_new_sobrenome").val()=="")
			{
				toastr.warning('Digite o Sobrenome');
				$("#text_new_sobrenome").focus();
				checkLastName = false;
			}
			
			var checkCPF = true;
			if($("#text_new_cpf").val()=="")
			{
				toastr.warning('Digite o CPF');
				$("#text_new_cpf").focus();
				checkCPF = false;
			}
			
			var checkData = true;
			if($("#text_new_data_nascimento").val()=="")
			{
				toastr.warning('Digite a data de nascimento');
				$("#text_new_data_nascimento").focus();
				checkData = false;
			}
			
			var checkEmail = true;
			if($("#text_new_email").val()=="")
			{
				toastr.warning('Digite o email');
				$("#text_new_email").focus();
				checkEmail = false;
			}
			
			var checkTelephone = true;
			if($("#text_new_telefone").val()=="")
			{
				toastr.warning('Digite o telefone');
				$("#text_new_telefone").focus();
				checkTelephone = false;
			}
			
			
			
			var checkSourceCPF = true;
			
			if(getClientByCPF($("#text_new_cpf").val()) != null)
			{
				toastr.warning('CPF já cadastrado');
				$("#text_new_cpf").focus();
				checkSourceCPF = false;
			}
			
			if(checkName && checkLastName && checkCPF && checkData && checkEmail && checkTelephone  && checkSourceCPF)
			{
				
				
				
				var nome = $("#text_new_name").val();
				var sobrenome = $("#text_new_sobrenome").val();
				var cpf = $("#text_new_cpf").val();
				var dataNascimento = $("#text_new_data_nascimento").val();
				var email = $("#text_new_email").val();
				var telefone = $("#text_new_telefone").val();
				var endereco = $("#text_new_endereco").val();
				
				var formData = new FormData();
				formData.append("text_new_name",nome);
				formData.append("text_new_sobrenome",sobrenome);
				formData.append("text_new_cpf",cpf);
				formData.append("text_new_data_nascimento",dataNascimento);
				formData.append("text_new_email",email);
				formData.append("text_new_telefone",telefone);
				formData.append("text_new_endereco",endereco);
				
				$.ajax({
				     url : contextPath + "/client/add",
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
						    			 $("#newClientModal").modal('hide');
								    	 
								    	 setTimeout(function() {
											  window.location.reload(1);
										}, 3000);
								    	break;
						    		 case 'CPF_INVALID':
						    			 toastr.warning('CPF inválido!');
						    			 break;
						    		 case 'TELEPHONE_INVALID':
						    			 toastr.warning('Telefone inválido!');
						    			 break;
						    		 case 'EMAIL_INVALID':
						    			 toastr.warning('E-mail inválido!');
						    			 break;
						    		 default:
						    			 toastr.error('Houve um erro durante a solicitação!');
						    		 	 error.show(status);
						    	 }
				    	 	}
				    	
				     	}
			
				})
			}
		})
		
		$(".btn_cancel_new_client").click(function(){
			
			$("#newClientModal").modal('hide');
			
			$('#form_newClientModal').each (function(){
				  this.reset();
			});
			
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
			toastr.success('Cliente removido com sucesso!');
		}
	}
	
	this.init = function()
	{
		try
		{
			
			
			
			$('body').loadingModal({
		  		  position: 'auto',
		  		  text: 'Carregando',
		  		  color: '#fff',
		  		  opacity: '0.7',
		  		  backgroundColor: 'rgb(0,0,0)',
		  		  animation: 'doubleBounce'
		  	});
			
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
			
			Validation.validAllTelephones();
			
			Validation.validTelephone("#text_telefone");
			
			let rememberFilter = new RememberFilter();
			rememberFilter.init();
			
			
			var paginationListBuilder = new PaginationListBuilder();
			
			paginationListBuilder.setUrl("/client/json");
			paginationListBuilder.setPaginationContainer(".pagination-container");
			paginationListBuilder.setFormSerialize("#form_clientes_filter");
			paginationListBuilder.setLocator("clientes");
			paginationListBuilder.setTableGrid('#registros_tabela');
			paginationListBuilder.setCountResult("#count_results");
			paginationListBuilder.setNameFileExcel("Clientes");
			paginationListBuilder.setContextDownloadExcel("/client/xls");
			paginationListBuilder.setNameFilePdf("Clientes");
			paginationListBuilder.setContextDownloadPdf("/client/pdf");
			paginationListBuilder.build();
			
			$(".select_count").change(function(){
				
				paginationListBuilder.build(this.value);
				
			});
			
			$(".btn_filter_submit").click(function(){
				
				paginationListBuilder.build($(".select_count").val());
				
				$("#filterClients").modal('hide');
				
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
			
			checkEmail();

			
			$(".addUser").click(function(){
				
				$("#newClientModal").modal('show');
				
			});
			
			saveClient();
			
			hideEraseModal();
			 
			checkDeleteSucess();
			
			updateClient();
			
			$(".btn_excel").click(function(){
				
				 paginationListBuilder.downloadExcel($(".select_count").val());
				 
			 });
			 
			 $(".btn_pdf").click(function(){
					
				 paginationListBuilder.downloadPdf($(".select_count").val());
				 
			 });
			 
			 Validation.validAllTelephones();
			 
			 Validation.validTelephone("#text_new_telefone");
			 
			 Validation.validTelephone("#text_edit_telefone");
		}
		catch ( exception )
		{
			console.error( 'this.init:'+exception );
		}
	}
}

try
{
	var clients = new Clients();
	var error = new Error();
	var $document = $( document );
	$document.ready( clients.init );
}
catch ( exception )
{
	console.error( 'clients.js:'+exception );
}