var formatInputDate = function(data){
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

var rankingEmployee = function(idUser)
{
	$('body').loadingModal('show');
	
	var fileName = "Ranking"+formatInputDate(new Date());
	let fileUrl = contextPath +"/us/getRankingByUser?idUser="+idUser;
	
	
	fetch(fileUrl).then((response) => {
		
		  if (response.status == 200) {
			  response.blob().then(blob => {
				  	const url = window.URL.createObjectURL(blob);
				    const a = document.createElement('a');
				    a.style.display = 'none';
				    a.href = url;
				    a.download = fileName+".xlsx";
				    document.body.appendChild(a);
				    a.click();
				    window.URL.revokeObjectURL(url);
				    $('body').loadingModal('hide');
			  });
			  
		  } else {
		    $('body').loadingModal('hide');
		    toastr.error("Houve um erro durante o download do Ranking do funcionario em Excel");
		  }
		})
		.catch((error) => {
			$('body').loadingModal('hide');
			toastr.error("Houve um erro durante o download do Ranking do funcionario em Excel");
		});
}

function editPassword(itemId)
{

	$("#text_edit_password").val('');
	$("#text_edit_confirm_password").val('');
	$("#idUserFromPassword").val(itemId);
	$("#editPassword").modal();
}

function showOrderMakers(itemId)
{
	$.ajax({
	     url : contextPath + "/request/getTypeProfileByIdRequest",
	     type : 'GET',
	     data : {
	    	 idRequest : itemId
	     },
	     async: false,
	     beforeSend : function(){
	          //none
	     },
	     success: function(data) {
	    	 
	    	 var response = JSON.parse(data);
	    	
	    	 let html = "";
	    	 html += "<tr>";
	    	 for(let profile of response.pedidoTipoPerfil)
	    	 {
	    		 
	    		 html += "<td>"+profile.tipoPerfil.tipo+"</td>";
	    		
	    	 }
	    	 html += "</tr>";
	            
	    	 $(".table_type_profiles").html(html);
	    	 
	    	 $("#typesProfileModal").modal();
	     }
	})
	.fail(function(jqXHR, textStatus, msg){
		error.show(msg);
	});
}


function editRequest(itemId)
{
	$.ajax({
	     url : contextPath + "/request/getById",
	     type : 'GET',
	     data : {
	    	 idRequest : itemId
	     },
	     async: false,
	     beforeSend : function(){
	          //none
	     },
	     success: function(data) { 
	            
	    	 var response = JSON.parse(data);
	    	 
	    	 $("#text_edit_name_request").val(response.request.nome);
	    	 
	    	 var hour = response.request.tempo.split(':');
	    	 
	    	 $("#text_edit_time_request").val(hour[0]+":"+hour[1]);
	    	 
	    	 $("#text_edit_preco_request").val(response.request.preco.replace('R$',''));
	    	 
	    	 $("#idRequestEdit").val(response.request.id);
	    	 
	    	 $("#editRequestModal").modal();
	     }
	})
	.fail(function(jqXHR, textStatus, msg){
		error.show(msg);
	});
}

function editProduct(itemId)
{
	$.ajax({
	     url : contextPath + "/product/getById",
	     type : 'GET',
	     data : {
	    	 idProduct : itemId
	     },
	     async: false,
	     beforeSend : function(){
	          //none
	     },
	     success: function(data) { 
	            
	    	 var response = JSON.parse(data);
	    	 
	    	 $("#text_edit_name_product").val(response.product.nome);

	    	 $("#text_edit_preco_product").val(response.product.preco.replace('R$',''));
	    	 
	    	 $("#idProductEdit").val(response.product.id);
	    	 
	    	 $("#editProductModal").modal();
	     }
	})
	.fail(function(jqXHR, textStatus, msg){
		error.show(msg);
	});
}

function getEmployee(id)
{
	
	var response;
	
	$.ajax({
	     url : contextPath + "/us/getUser",
	     type : 'GET',
	     data : {
	    	 idUser : id
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
	
	return response;
}

function editEmployee(itemId)
{
	
		var response = getEmployee(itemId);

	    	 
	    	 $("#text_edit_name_employee").val(response.user.funcionario.nome);
	    	 
	    	 $("#select_edit_tipo_employee").val(response.user.tipoPerfil.tipo);
	    	 
	    	 $("#text_edit_cpf_employee").val(response.user.funcionario.cpf);
	    	 
	    	 $("#text_edit_date_employee").val(formatInputDate(new Date(response.user.funcionario.dtNascimento)));
	    	 
	    	 $("#text_edit_email_employee").val(response.user.funcionario.email);
	    	 
	    	 $("#text_edit_telefone_employee").val(response.user.funcionario.telefone);
	    	 
	    	 $("#text_edit_endereco_employee").val(response.user.funcionario.endereco);
	    	 
	    	 $("#idUserFromEmployee").val(itemId);
	    	 
	    	 $("#editEmployeeModal").modal();
	           
	            			
	     
}
function showAdress(count)
{
	var adress = $(".adressInfo"+count).attr("adress");
	
	$("#addressInfoModal").modal();
	$("#lbl_info_adress").text(adress);
}

function showEraseEmployee(itemId)
{
	$("#idEmployeeErase").val(itemId);
	$("#eraseEmployee").modal();
}

function showEraseRequest(itemId)
{
	$("#idRequestErase").val(itemId);
	$("#eraseRequest").modal();
}

function showEraseProduct(itemId)
{
	$("#idProductErase").val(itemId);
	$("#eraseProduct").modal();
}

function Management(){
	
	var checkDeleteSucess = function()
	{
		var url = document.URL;
		
		if(url.indexOf('deleteSuccessEmployee') != -1)
		{
			toastr.success('Removido com sucesso!');
		}
		
		if(url.indexOf('deleteSuccessRequest') != -1)
		{
			toastr.success('Pedido removido com sucesso!');
		}
		
		if(url.indexOf('deleteSuccessProduct') != -1)
		{
			toastr.success('Produto removido com sucesso!');
		}
	}
	

	

	
	
	var showOrHideDivsManagement = function()
	{
		$(".pageRequest").hide();
		
		$(".pageProducts").hide();
		
		$(".addRequestDiv").hide();
		
		$(".addProductsDiv").hide();
		
		$(".menu_pedidos").click(function(){
			
			$(".menu_pedidos").css("background-color","white");
			$(".menu_pedidos").css("color","black");
			
			$(".menu_funcionario").css("background-color","transparent");
			$(".menu_funcionario").css("color","white");
			
			$(".menu_produtos").css("background-color","transparent");
			$(".menu_produtos").css("color","white");
			
			$(".pageRequest").show();
			
			$(".pageEmployee").hide();
			
			$(".pageProducts").hide();
			
			$(".addEmployeeDiv").hide();
			
			$(".addProductsDiv").hide();
			
			$(".addRequestDiv").show();
			
		});
		
		$(".menu_funcionario").click(function(){
			
			$(".menu_funcionario").css("background-color","white");
			$(".menu_funcionario").css("color","black");
			
			$(".menu_pedidos").css("background-color","transparent");
			$(".menu_pedidos").css("color","white");
			
			$(".menu_produtos").css("background-color","transparent");
			$(".menu_produtos").css("color","white");
			
			$(".pageEmployee").show();
			
			$(".pageRequest").hide();
			
			$(".pageProducts").hide();
			
			$(".addEmployeeDiv").show();
			
			$(".addRequestDiv").hide();
			
			$(".addProductsDiv").hide();
			
		});
		
		$(".menu_produtos").click(function(){
			
			$(".menu_produtos").css("background-color","white");
			$(".menu_produtos").css("color","black");
			
			$(".menu_funcionario").css("background-color","transparent");
			$(".menu_funcionario").css("color","white");
			
			$(".menu_pedidos").css("background-color","transparent");
			$(".menu_pedidos").css("color","white");
			
			$(".pageEmployee").hide();
			
			$(".pageRequest").hide();
			
			$(".pageProducts").show();
			
			$(".addEmployeeDiv").hide();
			
			$(".addRequestDiv").hide();
			
			$(".addProductsDiv").show();
			
		});
	};
	
	var showAndHideAddEmployeeModal = function()
	{
		$(".addEmployeeLink").click(function(){
			
			
			 $("#addEmployee").modal();
			 
		 });
		
		$(".btn_close_add_employee").click(function(){
			
			$("#addEmployee").modal('hide');
		});
	};
	
	var showAndHideAddRequestModal = function()
	{
		$(".addRequestLink").click(function(){
			
			
			 $("#addRequest").modal();
			 
		 });
		
		$(".btn_close_add_request").click(function(){
			
			$("#addRequest").modal('hide');
		});
	}
	
	var showAndHideAddProductsModal = function()
	{
		$(".addProductLink").click(function(){
			
			
			 $("#addProduct").modal();
			 
		 });
		
		$(".btn_close_add_product").click(function(){
			
			$("#addProduct").modal('hide');
		});
	}
	
	var mask = function()
	{
		
		 
		 $("#text_new_cpf_employee").mask('000.000.000-00', {reverse: true});
		 
		 $("#text_edit_cpf_employee").mask('000.000.000-00', {reverse: true});
		 
		 $("#text_valor").maskMoney({symbol:'R$ ',showSymbol:true, thousands:'.', decimal:',', symbolStay: true});
		 
		 $("#text_edit_preco_request").maskMoney({symbol:'R$ ',showSymbol:true, thousands:'.', decimal:',', symbolStay: true});
		 
		 $("#text_valor_product").maskMoney({symbol:'R$ ',showSymbol:true, thousands:'.', decimal:',', symbolStay: true});
		 
		 $("#text_edit_preco_product").maskMoney({symbol:'R$ ',showSymbol:true, thousands:'.', decimal:',', symbolStay: true});
	};
	
	
	
	var updatePassword = function()
	{
		$(".btn_edit_password").click(function(){
			
			if($("#text_edit_password").val() == '' || $("#text_edit_confirm_password").val() == '')
			{
				toastr.warning('Preencha todos os campos de senha');
				return;
			}
			
			var formData = new FormData();
			
			formData.append('text_edit_password',$("#text_edit_password").val());
			formData.append('text_edit_confirm_password',$("#text_edit_confirm_password").val());
			formData.append('idUserFromPassword',$("#idUserFromPassword").val());
			
			$.ajax({
			     url : contextPath + "/us/changedPassword",
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
			    	 
			    	 var status = response.status;
			    	 
			    	 switch(status)
			    	 {
			    		 case 'PASSWORDS_NOT_EQUALS':
			    			 toastr.warning('Senhas não coincidem');
			    			 break;
			    		 case 'SUCCESS':
			    			 toastr.success('Senha atualizada com sucesso!');
			    			 break;
			    		 default:
			    			 toastr.error('Houve um erro durante a solicitação!');
			    		 	 error.show(response.status);
			    	 }
			    	 
			    	 $("#editPassword").modal('hide');

			            			
			     }
			})
			.fail(function(jqXHR, textStatus, msg){
			     toastr.error('Houve um erro durante a solicitação!');
			     error.show(msg);
			});
			
		});
	}
	
	var updateRequest = function()
	{
		$(".btn_edit_request").click(function(){
			
			var checkName = true;
			if($("#text_edit_name_request").val()=="")
			{
				toastr.warning('Digite o Nome');
				$("#text_edit_name_request").focus();
				checkName = false;
			}
			
			var checkTime = true;
			if($("#text_edit_time_request").val()=="")
			{
				toastr.warning('Digite o Tempo');
				$("#text_edit_time_request").focus();
				checkTime = false;
			}
			
			var checkMoney = true;
			if($("#text_edit_preco_request").val()=="")
			{
				toastr.warning('Digite o Valor');
				$("#text_edit_preco_request").focus();
				checkMoney = false;
			}
			
			
			if(checkName && checkTime && checkMoney)
			{
				var formData = new FormData();

				formData.append("text_edit_name_request",$("#text_edit_name_request").val());
				formData.append("text_edit_time_request",$("#text_edit_time_request").val());
				formData.append("idRequest",$("#idRequestEdit").val());
				formData.append("text_new_valor_request",$("#text_edit_preco_request").val());
				
				$.ajax({
				     url : contextPath + "/request/update",
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
	
	var updateProduct = function()
	{
		$(".btn_edit_product").click(function(){
			
			var checkName = true;
			if($("#text_edit_name_product").val()=="")
			{
				toastr.warning('Digite o Nome');
				$("#text_edit_name_product").focus();
				checkName = false;
			}
			
			var checkMoney = true;
			if($("#text_edit_preco_product").val()=="")
			{
				toastr.warning('Digite o Valor');
				$("#text_edit_preco_product").focus();
				checkMoney = false;
			}
			
			
			if(checkName && checkMoney)
			{
				var formData = new FormData();

				formData.append("text_edit_name_product",$("#text_edit_name_product").val());
				formData.append("text_edit_preco_product",$("#text_edit_preco_product").val());
				formData.append("idProduct",$("#idProductEdit").val());
				
				$.ajax({
				     url : contextPath + "/product/update",
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
	var updateEmployee = function()
	{
		$(".btn_edit_employee").click(function(){
			
			var checkName = true;
			if($("#text_edit_name_employee").val()=="")
			{
				toastr.warning('Digite o Nome');
				$("#text_edit_name_employee").focus();
				checkName = false;
			}
		
			var checkCPF = true;
			if($("#text_edit_cpf_employee").val()=="")
			{
				toastr.warning('Digite o CPF');
				$("#text_edit_cpf_employee").focus();
				checkCPF = false;
			}
			
			var checkData = true;
			if($("#text_edit_date_employee").val()=="")
			{
				toastr.warning('Digite a data de nascimento');
				$("#text_edit_date_employee").focus();
				checkData = false;
			}
			
			var checkEmail = true;
			if($("#text_edit_email_employee").val()=="")
			{
				toastr.warning('Digite o email');
				$("#text_edit_email_employee").focus();
				checkEmail = false;
			}
			
			var checkTelephone = true;
			if($("#text_edit_telefone_employee").val()=="")
			{
				toastr.warning('Digite o telefone');
				$("#text_edit_telefone_employee").focus();
				checkTelephone = false;
			}
			
			var checkSourceCPF = true;
			
			if(getClientByCPF($("#text_edit_cpf_employee").val()) != null)
			{
				toastr.warning('CPF já cadastrado');
				$("#text_edit_cpf_employee").focus();
				checkSourceCPF = false;
			}
			
			if(checkName && checkCPF && checkData && checkEmail && checkTelephone  && checkSourceCPF)
			{
				var formData = new FormData();
				
				formData.append("text_edit_name_employee",$("#text_edit_name_employee").val());
				formData.append("text_edit_cpf_employee",$("#text_edit_cpf_employee").val());
				formData.append("text_edit_date_employee",$("#text_edit_date_employee").val());
				formData.append("text_edit_email_employee",$("#text_edit_email_employee").val());
				formData.append("text_edit_telefone_employee",$("#text_edit_telefone_employee").val());
				formData.append("text_edit_endereco_employee",$("#text_edit_endereco_employee").val());
				formData.append("select_type_employee",$("#select_edit_tipo_employee option:selected").val());
				formData.append("idUser",$("#idUserFromEmployee").val());
				
				$.ajax({
				     url : contextPath + "/us/update",
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
	
	var checkEmail = function()
	{
		
		Validation.validEmail("#text_new_email_employee,#text_edit_email_employee");	
	};
	
	
	
	var saveRequest = function()
	{
		$(".btn_add_request").click(function(){
			
			var checkName = true;
			if($("#text_new_name_request_save").val()=="")
			{
				toastr.warning('Digite o Nome');
				$("#text_new_name_request_save").focus();
				checkName = false;
			}
			
			var checkTime = true;
			if($("#text_new_time_request").val()=="")
			{
				toastr.warning('Digite o Tempo');
				$("#text_new_time_request").focus();
				checkTime = false;
			}
			
			var checkMoney = true;
			if($("#text_valor").val()=="")
			{
				toastr.warning('Digite o Valor');
				$("#text_valor").focus();
				checkMoney = false;
			}
			
			var checkSpeciality = true;
			if ($('.select_speciality_request_receive option').length == 0)
			{
				toastr.warning('Adicione alguma especialidade');
				$(".select_speciality_request_receive").focus();
				checkSpeciality = false;
			}
			
			
			
			if(checkName  && checkTime && checkMoney && checkSpeciality)
			{
				
				$("#addRequest").modal('hide');
				var nome = $("#text_new_name_request_save").val();
				var tempo = $("#text_new_time_request").val();
				var valor = $("#text_valor").val();
				var specialitys = getSpecialitys();
				
				var formData = new FormData();
				formData.append("text_new_name_request_save",nome);
				formData.append("text_new_time_request",tempo);
				formData.append("text_new_valor_request",valor);
				formData.append("specialitys",specialitys);
				
				
				
				$.ajax({
				     url : contextPath + "/request/add",
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
				    	 
				    	 if(response.status=='exists')
				    	 {
				    		 toastr.warning('Pedido já existente');
				    		 return;
				    	 }
				    	 
				    	 if(response.status=='success')
				         {
				    		 toastr.success('Cadastrado com sucesso!');
					    	 
					    	 setTimeout(function() {
					    		 location.href = '/management'
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
	}
	
	var saveProduct = function()
	{
		$(".btn_add_product").click(function(){
			
			var checkName = true;
			if($("#text_new_name_product_save").val()=="")
			{
				toastr.warning('Digite o Nome');
				$("#text_new_name_product_save").focus();
				checkName = false;
			}
			
			var checkMoney = true;
			if($("#text_valor_product").val()=="")
			{
				toastr.warning('Digite o Valor');
				$("#text_valor_product").focus();
				checkMoney = false;
			}
			
			
			
			if(checkName && checkMoney)
			{
				
				$("#addProduct").modal('hide');
				var nome = $("#text_new_name_product_save").val();
				var valor = $("#text_valor_product").val();
				
				var formData = new FormData();
				formData.append("text_new_name_product_save",nome);
				formData.append("text_new_valor_product",valor);
				
				
				
				$.ajax({
				     url : contextPath + "/product/add",
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
				    	 
				    	 if(response.status=='exists')
				    	 {
				    		 toastr.warning('Produto já existente');
				    		 return;
				    	 }
				    	 
				    	 if(response.status=='success')
				         {
				    		 toastr.success('Cadastrado com sucesso!');
					    	 
					    	 setTimeout(function() {
					    		 location.href = '/management'
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
	}
	
	var getSpecialitys = function()
	{
		var response = [];
		
		$(".select_speciality_request_receive option").each(function(index){
			
			response.push($(this).val());
			
		});
		
		return response;
	}
	
	
	var saveEmployee = function()
	{
		
		$(".btn_add_employee").click(function(){
			
			var checkName = true;
			if($("#text_new_name_employee").val()=="")
			{
				toastr.warning('Digite o Nome');
				$("#text_new_name_employee").focus();
				checkName = false;
			}
			
			var checkLogin = true;
			if($("#text_new_login_employee").val()=="")
			{
				toastr.warning('Digite o Login');
				$("#text_new_login_employee").focus();
				checkLogin = false;
			}
			
			var checkPassword = true;
			if($("#text_new_senha_employee").val()=="")
			{
				toastr.warning('Digite a Senha');
				$("#text_new_senha_employee").focus();
				checkPassword = false;
			}
			
			var checkCPF = true;
			if($("#text_new_cpf_employee").val()=="")
			{
				toastr.warning('Digite o CPF');
				$("#text_new_cpf_employee").focus();
				checkCPF = false;
			}
			
			var checkData = true;
			if($("#text_new_data_nascimento_employee").val()=="")
			{
				toastr.warning('Digite a data de nascimento');
				$("#text_new_data_nascimento_employee").focus();
				checkData = false;
			}
			
			var checkEmail = true;
			if($("#text_new_email_employee").val()=="")
			{
				toastr.warning('Digite o email');
				$("#text_new_email_employee").focus();
				checkEmail = false;
			}
			
			var checkTelephone = true;
			if($("#text_new_telefone_employee").val()=="")
			{
				toastr.warning('Digite o telefone');
				$("#text_new_telefone_employee").focus();
				checkTelephone = false;
			}
			
			
			
			var checkSourceCPF = true;
			
			if(getClientByCPF($("#text_new_cpf_employee").val()) != null)
			{
				toastr.warning('CPF já cadastrado');
				$("#text_new_cpf_employee").focus();
				checkSourceCPF = false;
			}
			
			
			
			var typeEmployee = true;
			
			if($("#select_type_employee").val()== null)
			{
				toastr.warning('Selecione um tipo do funcionário');
				$("#select_type_employee").focus();
				typeEmployee = false;
			}
			
			if(checkName  && checkCPF && checkData && checkEmail && checkTelephone  && checkSourceCPF && typeEmployee && checkLogin && checkPassword)
			{
				
				
				var nome = $("#text_new_name_employee").val();
				var cpf = $("#text_new_cpf_employee").val();
				var dataNascimento = $("#text_new_data_nascimento_employee").val();
				var email = $("#text_new_email_employee").val();
				var telefone = $("#text_new_telefone_employee").val();
				var endereco = $("#text_new_adress_employee").val();
				var tipoFuncionario = $("#select_type_employee").val();
				var login = $("#text_new_login_employee").val();
				var password = $("#text_new_senha_employee").val();
				
				var formData = new FormData();
				formData.append("text_new_name_employee",nome);
				formData.append("text_new_cpf_employee",cpf);
				formData.append("text_new_data_nascimento_employee",dataNascimento);
				formData.append("text_new_email_employee",email);
				formData.append("text_new_telefone_employee",telefone);
				formData.append("text_new_adress_employee",endereco);
				formData.append("select_type_employee",tipoFuncionario);
				formData.append("text_new_login_employee",login);
				formData.append("text_new_senha_employee",password);
				
				$.ajax({
				     url : contextPath + "/us/add",
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
						    			 $("#addEmployee").modal('hide');
								    	 
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
						    		 case 'LOGIN_EXIST':
						    			 toastr.warning('Login '+login+' já existe!');
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
	}	
	
	var hideEditEmployee = function()
	{
		$(".btn_close_edit_employee").click(function(){
			
			$("#editEmployeeModal").modal('hide');
			
		});
	}
	
	var hideEditRequest = function()
	{
		$(".btn_close_edit_request").click(function(){
			
			$("#editRequestModal").modal('hide');
			
		});
	}
	
	var hideEditPassword = function()
	{
		$(".btn_close_edit_password").click(function(){
			
			$("#editPassword").modal('hide');
			
		});
	}
	
	var getStatusOfDiscount = function()
	{
		$.ajax({
		     url : contextPath + "/service/getStatusOfDiscount",
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
		
		return response;
	}
	
	var updateStatusDiscount = function()
	{
		$(".liga-desliga__botao").click(function(){
			
			var enableOrDisable = !$(".liga-desliga__checkbox").is(':checked');
			
			$.ajax({
			     url : contextPath + "/service/enableOrDisableDiscount?enableOrDisable="+enableOrDisable.toString(),
			     type : 'POST',
			     async: false,
			     beforeSend : function(){
			          //none
			     },
			     success: function(data) { 
			            
			    	 response = JSON.parse(data);
			    	 
			    	 var status = response.status;
			    	 
			    	 if(status.message == 'SUCCESS')
			    	 {
			    		 if(enableOrDisable)
						 {
								toastr.success('Desconto ativado!');
						 }
			    		 else
						 {
								toastr.success('Desconto desativado!');
						 }
			    	 }
			    	 else
			    	 {
			    			 toastr.error('Houve um erro durante a solicitação!');
			    		 	 error.show(status.message);
			    	 }
			    	 
			    	 
			     }
			})
			.fail(function(jqXHR, textStatus, msg){
				error.show(msg);
			});
			
			
			
		});
		
		
	}
	
	var updateValueDiscount = function()
	{
		$(".btn_update_value_discount").click(function(){
			
			let value = $("#value_discount").val();
			
			$.ajax({
			     url : contextPath + "/service/updateValueDiscount?newValue="+value.toString(),
			     type : 'POST',
			     async: false,
			     beforeSend : function(){
			          //none
			     },
			     success: function(data) { 
			            
			    	 response = JSON.parse(data);
			    	 var status = response.status;
			    	 if(status.message == 'SUCCESS')
			    	 {
			    			 toastr.success('Desconto atualizado com sucesso!');
			    	 }
			    	 else
			    	 {
			    			 toastr.error('Houve um erro durante a solicitação!');
			    		 	 error.show(status.message);
			    	 }
			     }
			})
			.fail(function(jqXHR, textStatus, msg){
				error.show(msg);
			});
			
		});
	}
	
	var hideOrShowDiscountModal = function()
	{
		$(".link_img_birthday_cake").click(function(){
			
			let discount = getStatusOfDiscount();
			
			var checked = $(".liga-desliga__checkbox").is(':checked');
			
			if((discount.enable == true) && (checked == false))
			{
				$(".liga-desliga__checkbox")[0].checked = true;
			}
			
			$("#value_discount").val(discount.value);
			
			$("#discountModal").modal();
			
		});
		
		$(".btn_close_discount").click(function(){
			
			$("#discountModal").modal('hide');
			
		});
	}
	
	var hideTypeProfilesModal = function()
	{
		$(".btn_close_type_profiles").click(function(){
			
			$("#typesProfileModal").modal('hide');
			
		});
	}
	
	var chooseSpeciality = function()
	{
		$(".selected_speciality_request").click(function(){
			
			if ($('.select_speciality_request_add option:selected').length > 0)
			{
			
				var value = $('.select_speciality_request_add').val();
				
				var text = $('.select_speciality_request_add option:selected').text();
				
				var option = "<option value='"+value+"'>"+text+"</option>";
				
				$(".select_speciality_request_receive").append(option);
				
				$('.select_speciality_request_add option:selected').remove();
			}
			
		});
		
		$(".selected_speciality_request_return").click(function(){
			
			if ($('.select_speciality_request_receive option:selected').length > 0)
			{
			
				var value = $('.select_speciality_request_receive').val();
				
				var text = $('.select_speciality_request_receive option:selected').text();
				
				var option = "<option value='"+value+"'>"+text+"</option>";
				
				$(".select_speciality_request_add").append(option);
				
				$('.select_speciality_request_receive option:selected').remove();
			}
			
		});
	}
	
	var hideModalEraseClient = function()
	{
		$(".btn_close_erase_product").click(function(){
			
			$("#eraseProduct").modal('hide');
			
		});
	}
	
	this.init = function()
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
		var paginationListBuilderEmployee = new PaginationListBuilder();
		
		paginationListBuilderEmployee.setUrl("/us/json");
		paginationListBuilderEmployee.setPaginationContainer(".pagination-container");
		paginationListBuilderEmployee.setFormSerialize("#form_filterManagement");
		paginationListBuilderEmployee.setLocator("usuarios");
		paginationListBuilderEmployee.setTableGrid('#registros_tabela');
		paginationListBuilderEmployee.setCountResult("#count_results");
		paginationListBuilderEmployee.setNameFileExcel("Funcionarios");
		paginationListBuilderEmployee.setContextDownloadExcel("/us/xls");
		paginationListBuilderEmployee.setNameFilePdf("Funcionarios");
		paginationListBuilderEmployee.setContextDownloadPdf("/us/pdf");
		paginationListBuilderEmployee.build();
		
		$(".select_count").change(function(){
			
			paginationListBuilderEmployee.build(this.value);
		});
		
		$(".btn_ok").click(function(){
			
			
			$("#addressInfoModal").modal('hide');
		});
		
		showOrHideDivsManagement();
		
		
		var paginationListBuilderRequest = new PaginationListBuilder();
		
		paginationListBuilderRequest.setUrl("/request/json");
		paginationListBuilderRequest.setPaginationContainer(".pagination-requests-container");
		paginationListBuilderRequest.setFormSerialize("#form_filterManagementRequests");
		paginationListBuilderRequest.setLocator("requests");
		paginationListBuilderRequest.setTableGrid('#registros_tabela_requests');
		paginationListBuilderRequest.setCountResult("#count_results_requests");
		paginationListBuilderRequest.setNameFileExcel("Pedidos");
		paginationListBuilderRequest.setContextDownloadExcel("/request/xls");
		paginationListBuilderRequest.setNameFilePdf("Pedidos");
		paginationListBuilderRequest.setContextDownloadPdf("/request/pdf");
		paginationListBuilderRequest.build();
		
		$(".select_count_requests").change(function(){
			
			paginationListBuilderRequest.build(this.value);
		});
		
		$(".btn_excel_employee").click(function(){
			
			paginationListBuilderEmployee.downloadExcel($(".select_count").val());
			 
		 });
		 
		 $(".btn_pdf_employee").click(function(){
				
			 paginationListBuilderEmployee.downloadPdf($(".select_count").val());
			 
		});
		 
		 $(".btn_excel_requests").click(function(){
				
			 paginationListBuilderRequest.downloadExcel($(".select_count_requests").val());
				 
		 });
			 
		 $(".btn_pdf_requests").click(function(){
					
			 paginationListBuilderRequest.downloadPdf($(".select_count_requests").val());
				 
		 });
		 
		 $(".btn_close_erase_employee").click(function(){
			
			 $("#eraseEmployee").modal('hide');
			 
		 });
		 
		 $(".btn_close_erase_request").click(function(){
				
			 $("#eraseRequest").modal('hide');
			 
		 });
		 
		 $(".btn_close_edit_product").click(function(){
			
			 $("#editProductModal").modal('hide');
			 
		 });
		 
		 var paginationListBuilderProduct = new PaginationListBuilder();
			
		 paginationListBuilderProduct.setUrl("/product/json");
		 paginationListBuilderProduct.setPaginationContainer(".pagination-products-container");
		 paginationListBuilderProduct.setFormSerialize("#form_filterManagementProducts");
		 paginationListBuilderProduct.setLocator("products");
		 paginationListBuilderProduct.setTableGrid('#registros_tabela_products');
		 paginationListBuilderProduct.setCountResult("#count_results_products");
		 paginationListBuilderProduct.setNameFileExcel("Produtos");
		 paginationListBuilderProduct.setContextDownloadExcel("/product/xls");
		 paginationListBuilderProduct.setNameFilePdf("Produtos");
		 paginationListBuilderProduct.setContextDownloadPdf("/product/pdf");
		 paginationListBuilderProduct.build();
		 
		 $(".select_count_products").change(function(){
				
			 paginationListBuilderProduct.build(this.value);
		});
		 
		 $(".btn_excel_products").click(function(){
				
			 paginationListBuilderProduct.downloadExcel($(".select_count_products").val());
				 
		});
		 
		 $(".btn_pdf_products").click(function(){
				
			 paginationListBuilderProduct.downloadPdf($(".select_count_products").val());
				 
		 });
		 
		 checkDeleteSucess();
		 
		 showAndHideAddEmployeeModal();
		 
		 mask();
		 
		
		 saveEmployee();
		 
		 checkEmail();
		 
		 showAndHideAddRequestModal();
		 
		 showAndHideAddProductsModal();
		 
		 saveRequest();
		 
		 hideEditEmployee();
		 
		 hideEditRequest();
		 
		 hideEditPassword();
		 
		 updateEmployee();
		 
		 updateRequest();

		 updatePassword();
		 
		 Validation.validAllTelephones();
		 
		 Validation.validTelephone("#text_edit_telefone_employee");
		 
		 Validation.validTelephone("#text_new_telefone_employee");
		 
		 hideOrShowDiscountModal();
		 
		 updateStatusDiscount();
		 
		 updateValueDiscount();
		 
		 hideTypeProfilesModal();
		 
		 chooseSpeciality();
		 
		 saveProduct();
		 
		 updateProduct();
		 
		 hideModalEraseClient();
	}
	
}

try
{
	var management = new Management();
	var error = new Error();
	var $document = $( document );
	$document.ready( management.init );
}
catch ( exception )
{
	console.error( 'management.js:'+exception );
}