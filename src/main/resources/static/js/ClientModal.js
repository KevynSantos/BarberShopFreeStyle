class ClientModal
{
	
	
	
	static init()
	{
		$('body').append( "<div id='chooseClientModal' class='modal modal-group fade' tabindex='-1' role='dialog'>" +
				"<div class='modal-dialog' role='document'>" +
					"<div id='modal-content-continue' class='modal-content modal-continue'>" +
						"<div class='modal-header'>" +
							"<h4>Escolher cliente</h4>"+
						"</div>" +
						
						"<div class='modal-body'>" +
							"<table  class='table_search_client'>"+
								"<tr><td>Nome: <input type='text' name='name_search_client' id='name_search_client'/></td><td>CPF: <input type='text' name='cpf_search_client' id='cpf_search_client'/><input type='hidden' id='text_cpf_temporary'></td></tr>"+
							"<table><br>"+
						"</div>" +
		
						"<div class='modal-footer'>" +
							"<div class='btn_submit_cancel'><button type='button' class='btn_ok_choose_client_modal'>Selecionar</button><button type='button' class='btn_ok_close_choose_client_modal'>Fechar</button></div>" +
						"</div>" +
					"</div>" +
				"</div>" +
		"</div>" );
		
		$(".btn_ok_close_choose_client_modal,.btn_ok_choose_client_modal").css({"background": "black", "border": "none", "border-radius": "4px", "color" : "#fff", "font-weight" : "600", "padding" : "8px 15px", "margin-bottom" : "0" });
		
		$(".table_search_client td").css({"padding-left":"3px","padding-right":"3px"});
		
		$("#cpf_search_client").mask('000.000.000-00', {reverse: true});
		
		$(".btn_ok_close_choose_client_modal").click(function(){
			
			$("#chooseClientModal").modal('hide');
			
		});
		
		$(".btn_search_client").click(function(){
			
			$("#chooseClientModal").modal();
			
		});
		
		$(".btn_ok_choose_client_modal").click(function(){
			
			$("#text_cpf").val($("#text_cpf_temporary").val());
			
			$("#chooseClientModal").modal('hide');
			
		});
		
		$("#cpf_search_client").change(function(){
			
			var cpf = $("#cpf_search_client").val();
			
			if(cpf == "")
			{
				$("#cpf_search_client").val('');
				$("#name_search_client").val('');
				$("#text_cpf_temporary").val('');
				return;
			}
			
			var response;
			
			$.ajax({
			     url : contextPath + "/client/getClientLikeCpf",
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
			
			if(response.client != null)
			{
				$("#name_search_client").val(response.client.nome);
				$("#text_cpf_temporary").val(response.client.cpf);
			}
			else
			{
				$("#cpf_search_client").val('');
				$("#name_search_client").val('');
				$("#text_cpf_temporary").val('');
			}
			
		});
		
		$("#name_search_client").change(function(){
			
			var nome = $("#name_search_client").val();
			
			if(nome == "")
			{
				$("#cpf_search_client").val('');
				$("#name_search_client").val('');
				$("#text_cpf_temporary").val('');
				return;
			}
			
			var response;
			
			$.ajax({
			     url : contextPath + "/client/getClientLikeNome",
			     type : 'GET',
			     data : {
			    	 nome : nome
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
			
			if(response.client != null)
			{
				
				$("#text_cpf_temporary").val(response.client.cpf);
				$("#cpf_search_client").val(response.client.cpf);
			}
			else
			{
				$("#cpf_search_client").val('');
				$("#name_search_client").val('');
				$("#text_cpf_temporary").val('');
			}
			
		});
	}
}