function showModalEmployee(itemId)
{
	
	$(".name_employee").text($(".info_employee"+itemId).attr("title"));
	$(".name_client").text($(".info_employee"+itemId).attr("client"));
	$(".profile_type").text($(".info_employee"+itemId).attr("profile"));
	$("#showEmployeeServiceAndScheduling").modal();
}

function Employee()
{
	
	var getTitle = function()
	{
		var url = document.URL;
		
		if(url.indexOf("services")!= -1)
		{
			return "Serviço";
		}
		
		if(url.indexOf("schedules")!= -1)
		{
			return "Agendamento";
		}
		
		return "";
	}
	
	this.init = function()
	{
		
		let title = getTitle();
		
		$('body').append( "<div id='showEmployeeServiceAndScheduling' class='modal modal-group fade' tabindex='-1' role='dialog'>" +
				"<div class='modal-dialog' role='document'>" +
					"<div id='modal-content-continue' class='modal-content modal-continue'>" +
						"<div class='modal-header'>" +
							"<h4>Informações do "+title+"</h4>"+
						"</div>" +
						
						"<div class='modal-body'>" +
							"<table border='2' class='table_info_employee'>"+
								"<tr><th>Nome Funcionário</th><th>Nome Cliente</th><th>TipoPerfil</th></tr>"+
								"<tr><td class='name_employee'></td><td class='name_client'></td><td class='profile_type'></td></tr>"+
							"<table>"+
						"</div>" +
		
						"<div class='modal-footer'>" +
							"<div class='btn_submit_cancel'><button type='button' class='btn_ok_close_employee_info'>Fechar</button></div>" +
						"</div>" +
					"</div>" +
				"</div>" +
			"</div>" );
		
		$(".btn_ok_close_employee_info").css({"background": "black", "border": "none", "border-radius": "4px", "color" : "#fff", "font-weight" : "600", "padding" : "8px 15px", "margin-bottom" : "0" });

		$(".table_info_employee td").css({"padding":"3px"});
		
		
		$(".btn_ok_close_employee_info").click(function(){
			
			$("#showEmployeeServiceAndScheduling").modal('hide');
			
		});
		
	}
	
	
}

try
{
	var employee = new Employee();
	
	var $document = $( document );
	$document.ready( employee.init );
}

catch(exception)
{
	showError(exception);
}