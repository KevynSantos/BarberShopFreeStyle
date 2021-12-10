function RememberFilter()
{
	const fieldName = "_field_";
	const schedules = 'SCHEDULES';
	const services = 'SERVICES';
	const clients = 'CLIENTS';
	const schedulesByUrl = 'schedules';
	const servicesByUrl = 'services';
	const clientsByUrl = 'clients';
	var functionality = null;
	const formSchedules = "#form_filterSchedules";
	const formServices = "#form_filterServices";
	const formClients = "#form_clientes_filter";
	const isNotEmpty = "isNotEmpty";
	
	var checkFilter = function()
	{
		var rota = document.URL;

		if(rota.indexOf(schedulesByUrl) != -1)
		{
			functionality = schedules;
		}
		
		if(rota.indexOf(servicesByUrl) != -1)
		{
			functionality = services;
		}
		
		if(rota.indexOf(clientsByUrl) != -1)
		{
			functionality = clients;
		}
	}
	
	var getRememberSchedules = function()
	{
		var isNotEmpty = checkFunctionOfPage("SCHEDULES");
		if(isNotEmpty)
		{
			if(localStorage.getItem(schedules+fieldName+"text_pedido") != null)
			{
				$("#text_pedido").val(localStorage.getItem(schedules+fieldName+"text_pedido"));
			}
			if(localStorage.getItem(schedules+fieldName+"text_cpf") != null)
			{
				$("#text_cpf").val(localStorage.getItem(schedules+fieldName+"text_cpf"));
			}
			if(localStorage.getItem(schedules+fieldName+"data_inicial") != null)
			{
				$("#data_inicial").val(localStorage.getItem(schedules+fieldName+"data_inicial"));
			}
			if(localStorage.getItem(schedules+fieldName+"data_final") != null)
			{
				$("#data_final").val(localStorage.getItem(schedules+fieldName+"data_final"));
			}
			if(localStorage.getItem(schedules+fieldName+"hora_inicial") != null && localStorage.getItem(schedules+fieldName+"hora_inicial") != "")
			{
				$("#hora_inicial").val(localStorage.getItem(schedules+fieldName+"hora_inicial"));
			}
			if(localStorage.getItem(schedules+fieldName+"hora_final") != null && localStorage.getItem(schedules+fieldName+"hora_final") != "")
			{
				$("#hora_final").val(localStorage.getItem(schedules+fieldName+"hora_final"));
			}
			if(localStorage.getItem(schedules+fieldName+"text_cpf_employee") != null)
			{
				$("#text_cpf_employee").val(localStorage.getItem(schedules+fieldName+"text_cpf_employee"));
			}
			if(localStorage.getItem(schedules+fieldName+"select_speciality_employee") != null)
			{
				$("#select_speciality_employee option:eq("+parseInt(localStorage.getItem(schedules+fieldName+"select_speciality_employee"))+")").attr("selected", "selected");
			}
		}
		
	}
	
	var getRememberServices = function()
	{
		
		var isNotEmpty = checkFunctionOfPage("SERVICES");
		if(isNotEmpty)
		{
			if(localStorage.getItem(services+fieldName+"text_pedido") != null)
			{
				$("#text_pedido").val(localStorage.getItem(services+fieldName+"text_pedido"));
			}
			if(localStorage.getItem(services+fieldName+"text_cpf") != null)
			{
				$("#text_cpf").val(localStorage.getItem(services+fieldName+"text_cpf"));
			}
			if(localStorage.getItem(services+fieldName+"data_inicial") != null)
			{
				$("#data_inicial").val(localStorage.getItem(services+fieldName+"data_inicial"));
			}
			if(localStorage.getItem(services+fieldName+"data_final") != null)
			{
				$("#data_final").val(localStorage.getItem(services+fieldName+"data_final"));
			}
			
			if(localStorage.getItem(services+fieldName+"select_status") != null)
			{
				$("#select_status option:eq("+parseInt(localStorage.getItem(services+fieldName+"select_status"))+")").attr("selected", "selected");
			}
			if(localStorage.getItem(services+fieldName+"text_cpf_employee") != null)
			{
				$("#text_cpf_employee").val(localStorage.getItem(services+fieldName+"text_cpf_employee"));
			}
			if(localStorage.getItem(services+fieldName+"select_speciality_employee") != null)
			{
				$("#select_speciality_employee option:eq("+parseInt(localStorage.getItem(services+fieldName+"select_speciality_employee"))+")").attr("selected", "selected");
			}
		}
	}
	
	var getRememberClients = function()
	{
		var isNotEmpty = checkFunctionOfPage("CLIENTS");
		if(isNotEmpty)
		{
			if(localStorage.getItem(clients+fieldName+"text_nome") != null)
			{
				$("#text_nome").val(localStorage.getItem(clients+fieldName+"text_nome"));
			}
			if(localStorage.getItem(clients+fieldName+"text_cpf") != null)
			{
				$("#text_cpf").val(localStorage.getItem(clients+fieldName+"text_cpf"));
			}
			if(localStorage.getItem(clients+fieldName+"data_inicial") != null)
			{
				$("#data_inicial").val(localStorage.getItem(clients+fieldName+"data_inicial"));
			}
			if(localStorage.getItem(clients+fieldName+"data_final") != null)
			{
				$("#data_final").val(localStorage.getItem(clients+fieldName+"data_final"));
			}
			if(localStorage.getItem(clients+fieldName+"text_email") != null)
			{
				$("#text_email").val(localStorage.getItem(clients+fieldName+"text_email"));
			}
			if(localStorage.getItem(clients+fieldName+"text_telefone") != null)
			{
				$("#text_telefone").val(localStorage.getItem(clients+fieldName+"text_telefone"));
			}
			if(localStorage.getItem(clients+fieldName+"text_endereco") != null)
			{
				$("#text_endereco").val(localStorage.getItem(clients+fieldName+"text_endereco"));
			}
			
		}
	}
	
	var saveRememberSchedules = function()
	{
		
		
		
		$(formSchedules).change(function(){
			
			localStorage.setItem(isNotEmpty,isNotEmpty);
			localStorage.setItem("functionPage","SCHEDULES");
			
			
		});
		
		$("#text_pedido").change(function(){
			localStorage.setItem(schedules+fieldName+"text_pedido",$("#text_pedido").val());
		});
		$("#text_cpf").change(function(){
			localStorage.setItem(schedules+fieldName+"text_cpf",$("#text_cpf").val());
		});
		$("#data_inicial").change(function(){
			localStorage.setItem(schedules+fieldName+"data_inicial",$("#data_inicial").val());
		});
		$("#data_final").change(function(){
			localStorage.setItem(schedules+fieldName+"data_final",$("#data_final").val());
		});
		$("#hora_inicial").change(function(){
			localStorage.setItem(schedules+fieldName+"hora_inicial",$("#hora_inicial").val());
		});
		$("#hora_final").change(function(){
			localStorage.setItem(schedules+fieldName+"hora_final",$("#hora_final").val());
		});
		$("#text_cpf_employee").change(function(){
			localStorage.setItem(schedules+fieldName+"text_cpf_employee",$("#text_cpf_employee").val());
		});
		
		$("#select_speciality_employee").change(function(){
			localStorage.setItem(schedules+fieldName+"select_speciality_employee",$("#select_speciality_employee option:selected").index());
		});
		
	}
	
	var saveRememberServices = function()
	{
		
		
		$(formServices).change(function(){
			
			localStorage.setItem(isNotEmpty,isNotEmpty);
			localStorage.setItem("functionPage","SERVICES");
			
			
		});
		
		$("#text_pedido").change(function(){
			localStorage.setItem(services+fieldName+"text_pedido",$("#text_pedido").val());
		});
		$("#text_cpf").change(function(){
			localStorage.setItem(services+fieldName+"text_cpf",$("#text_cpf").val());
		});
		$("#data_inicial").change(function(){
			localStorage.setItem(services+fieldName+"data_inicial",$("#data_inicial").val());
		});
		$("#data_final").change(function(){
			localStorage.setItem(services+fieldName+"data_final",$("#data_final").val());
		});
		$("#select_status").change(function(){
			localStorage.setItem(services+fieldName+"select_status",$("#select_status option:selected").index());
		});
		$("#text_cpf_employee").change(function(){
			localStorage.setItem(services+fieldName+"text_cpf_employee",$("#text_cpf_employee").val());
		});
		$("#select_speciality_employee").change(function(){
			localStorage.setItem(services+fieldName+"select_speciality_employee",$("#select_speciality_employee option:selected").index());
		});
		
		
	}
	
	var saveRememberClients = function()
	{
		
		
		
		$(formClients).change(function(){
			
			
			
			localStorage.setItem(isNotEmpty,isNotEmpty);
			
			localStorage.setItem("functionPage","CLIENTS");
			
			
			
		});
		
		$("#text_nome").change(function(){
			localStorage.setItem(clients+fieldName+"text_nome",$("#text_nome").val());
		});
		$("#text_cpf").change(function(){
			localStorage.setItem(clients+fieldName+"text_cpf",$("#text_cpf").val());
		});
		$("#data_inicial").change(function(){
			localStorage.setItem(clients+fieldName+"data_inicial",$("#data_inicial").val());
		});
		$("#data_final").change(function(){
			localStorage.setItem(clients+fieldName+"data_final",$("#data_final").val());
		});
		$("#text_email").change(function(){
			localStorage.setItem(clients+fieldName+"text_email",$("#text_email").val());
		});
		$("#text_telefone").change(function(){
			localStorage.setItem(clients+fieldName+"text_telefone",$("#text_telefone").val());
		});
		$("#text_endereco").change(function(){
			localStorage.setItem(clients+fieldName+"text_endereco",$("#text_endereco").val());
		});
		
	}
	
	var getRemember = function()
	{
		var empty = checkIsEmpty();
		if(empty)
		{
			return;
		}
		else
		{
			
			
			if(functionality==schedules)
			{
				getRememberSchedules();
			}
			
			if(functionality==services)
			{
				getRememberServices();
			}
			
			if(functionality==clients)
			{
				getRememberClients();
			}
		}
	}
	
	var checkIsEmpty = function()
	{
		let rememberFilter = new RememberFilter();
		
		return rememberFilter.isEmpty();
	}
	
	var checkFunctionOfPage = function(functionPage)
	{
		if(localStorage.getItem(functionPage) == null)
		{
			return true;
		}
		
		return false;
	}
	
	this.isEmpty = function()
	{
		
		if(localStorage.getItem(isNotEmpty) == null)
		{
			return true;
		}
		
		return false;
	}
	
	var saveRemember = function()
	{
		
		
		if(functionality==schedules)
		{
			saveRememberSchedules();
		}
	
		if(functionality==services)
		{
			saveRememberServices();
		}
		
		if(functionality==clients)
		{
			saveRememberClients();
		}
	}
	
	this.init = function()
	{
		checkFilter();
		getRemember();
		saveRemember();
	}
}