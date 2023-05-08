function ChangeData()
{
	var bottonModalStyle = function()
	{
		$(".btn_edit_data,.btn_close_edit_data")
		.css("background","black")
		.css("border","none")
		.css("border-radius","4px")
		.css("color","#fff").css("font-weight","600")
		.css("padding","8px 15px")
		.css("margin-bottom","0");
		
		
		$(".btn_submit_cancel").css("margin-top","30px");
		
		$(".btn_submit_cancel button").css("margin-left","10px").css("margin-right","10px");
	}
	
	var checkUserIsManagement = function()
	{
		var user = $(".typeProfileAdministrador").text();
		
		if(user=='GESTOR')
		{
			return true;
		}
		
		return false;
	}
	
	var addModal = function()
	{
		var newPassword = checkUserIsManagement() == true ?
			
				"Nova Senha<br>" +
				"<input type='password' class='text-filtro' name='text_edit_name_senha_data' id='text_edit_name_senha_data' >":"";
		
		$("body").append(
				"<div id='changeDataModal' class='modal modal-group fade' tabindex='-1' role='dialog'>" +
					"<div class='modal-dialog' role='document'>" +
						"<div id='modal-content-continue' class='modal-content modal-continue'>" +
							"<div class='modal-header'>" +
								"<h3>Alterar Dados</h3>" +
							"</div>" +
						
						"<div class='modal-body'>" +
							"<div class='grid_change_data'>" +
								"<div>" +
									"Nome<br>" +
									"<input type='text' class='text-filtro' name='text_edit_name_data' id='text_edit_name_data' >" +
								"</div>" +
								"<div>" +
									"Telefone<br>" +
									"<input type='text' class='text-filtro telefone' name='text_edit_name_telefone_data' id='text_edit_name_telefone_data' >" +
								"</div>" +
								"<div>" +
									"E-mail<br>" +
									"<input type='text' class='text-filtro' name='text_edit_name_email_data' id='text_edit_name_email_data' >" +
								"</div>" +
								"<div>" +
									"Data de Nascimento<br>" +
									"<input type='date' class='text-filtro' name='text_edit_data_nascimento_data' id='text_edit_data_nascimento_data' >" +
								"</div>" +
								"<div>" +
									"CPF<br>" +
									"<input type='text' class='text-filtro' name='text_edit_name_cpf_data' id='text_edit_name_cpf_data' >" +
								"</div>" +
								"<div>"+newPassword+"</div>" +
								"<div>" +
			    					"Endereço<br>" +
			    					"<textarea rows='5' cols='70' id='text_edit_endereco_employee_data' name='text_edit_endereco_employee_data'></textarea>" +
			    				"</div>" +
							"</div>" +
						"</div>" +
						"<div class='modal-footer'>" +
							"<div class='btn_submit_cancel'>" +
								"<button type='button' class='btn_edit_data'>Atualizar</button><button type='button' class='btn_close_edit_data'>Cancelar</button>" +
							"</div>" +
						"</div>" +
					"</div>" +
				"</div>" +
			"</div>");
	}
	
	var enableBottons = function()
	{
		$(".changeDataBtn").click(function(){
			
			$("#changeDataModal").modal();
			fillFields();
			
		});
		
		$(".btn_close_edit_data").click(function(){
			
			$("#changeDataModal").modal('hide');
		});
	}
	
	var fieldsModalStyle = function()
	{
		$(".text-filtro")
		.css("width","100%")
		.css("height","45px")
		.css("border-radius","3px")
		.css("border","1px solid black");
		
		$("#text_edit_endereco_employee_data").css("width","510px");
	}
	
	var gridModalStyle = function()
	{
		$(".grid_change_data")
		.css("display","grid")
		.css("width","100%")
		.css("grid-template-columns","250px 250px")
		.css("justify-content","space-between")
		.css("padding-left","30px")
		.css("padding-right","30px");
		
		$(".grid_change_data div")
		.css("margin-top","10px");
	}
	
	var checkCpf = function(cpf)
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
	}
	
	var validationFields = function()
	{
		Validation.validAllTelephones();
		
		Validation.validEmail("#text_edit_name_email_data");
		
		Validation.validTelephone("#text_edit_name_telefone_data");
		
		$("#text_edit_name_cpf_data").mask('000.000.000-00', {reverse: true});
		
		$("#text_edit_name_cpf_data").change(function(){
			
			var cpf = $("#text_edit_name_cpf_data").val();
			
			var clientExists = checkCpf(cpf);
			
			if(clientExists != null)
			{
				toastr.warning('CPF Existente!');
			}
			
		});
		
	}
	
	var formatdtNascimento = function(data){
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
	
	var putData = function()
	{
		$(".btn_edit_data").click(function(){
			
				var password = $("#text_edit_name_senha_data").val();
				var properties;
				
				if(password == "" || password == null)
				{
					properties = {
						 text_edit_name_data: $("#text_edit_name_data").val(),
				    	 text_edit_name_telefone_data: $("#text_edit_name_telefone_data").val(),
				    	 text_edit_name_email_data: $("#text_edit_name_email_data").val(),
				    	 text_edit_data_nascimento_data: $("#text_edit_data_nascimento_data").val(),
				    	 text_edit_name_cpf_data: $("#text_edit_name_cpf_data").val(),
				    	 text_edit_endereco_employee_data: $("#text_edit_endereco_employee_data").val()
					};
				}
				
				else
				{
					properties = {
							 text_edit_name_data: $("#text_edit_name_data").val(),
					    	 text_edit_name_telefone_data: $("#text_edit_name_telefone_data").val(),
					    	 text_edit_name_email_data: $("#text_edit_name_email_data").val(),
					    	 text_edit_data_nascimento_data: $("#text_edit_data_nascimento_data").val(),
					    	 text_edit_name_cpf_data: $("#text_edit_name_cpf_data").val(),
					    	 text_edit_endereco_employee_data: $("#text_edit_endereco_employee_data").val(),
					    	 text_edit_name_senha_data: $("#text_edit_name_senha_data").val()
					};
				}
		
				$.ajax({
				     url : contextPath + "/us/changeData",
				     type : 'POST',
				     data : properties,
				     async: false,
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
						    			 toastr.success('Atualizado com sucesso!');
								    	 
								    	$("#changeDataModal").modal('hide');
								    	
								    	$("#text_edit_name_senha_data").val('');
								    	
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
				})
				.fail(function(jqXHR, textStatus, msg){
					error.show(msg);
				});
		});
	}
	
	var fillFields = function()
	{
		var response;
		
		$.ajax({
		     url : contextPath + "/us/getUserFromSession",
		     type : 'GET',
		     data : {
		    	
		     },
		     async: false,
		     beforeSend : function(){
		          //none
		     },
		     success: function(data) { 
		            
		    	 response = JSON.parse(data);
		    	 var employee = response.user.funcionario;
		    	 
		    	 $("#text_edit_name_data").val(employee.nome);
		    	 $("#text_edit_name_telefone_data").val(employee.telefone);
		    	 $("#text_edit_name_email_data").val(employee.email);
		    	 $("#text_edit_data_nascimento_data").val(formatdtNascimento(new Date(employee.dtNascimento)));
		    	 $("#text_edit_name_cpf_data").val(employee.cpf);
		    	 $("#text_edit_endereco_employee").val(employee.endereco);
		         
		            			
		     }
		})
		.fail(function(jqXHR, textStatus, msg){
			error.show(msg);
		});
		
		
	}
	
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
		
		
		addModal();
		
		fieldsModalStyle();
		
		gridModalStyle();
		
		bottonModalStyle();
		
		enableBottons();
		
		validationFields();
		
		putData();
		
	}
}

try
{
	var changeData = new ChangeData();
	var error = new Error();
	var $document = $( document );
	$document.ready( changeData.init );
}
catch ( exception )
{
	console.error( 'ChangeData.js:'+exception );
}