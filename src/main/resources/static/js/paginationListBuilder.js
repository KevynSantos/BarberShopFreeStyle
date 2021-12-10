function PaginationListBuilder()
{
	
	var tableGrid = null;
	
	this.setUrl = function(url)
	{
		this.url = url;
	}
	
	this.setPaginationContainer = function(paginationContainer)
	{
		this.paginationContainer = paginationContainer;
	}
	
	this.setFormSerialize = function(formSerialize)
	{
		this.formSerialize = formSerialize;
	}
	
	this.setLocator = function(locator)
	{
		this.locator = locator;
	}
	
	this.setTableGrid = function(newTableGrid)
	{
		tableGrid = newTableGrid;
	}
	
	this.setCountResult = function(countResult)
	{
		this.countResult = countResult;
	}
	
	this.setNameFileExcel = function(nameFileExcel)
	{
		this.nameFileExcel = nameFileExcel;
	}
	
	this.setContextDownloadExcel = function(contextDownloadExcel)
	{
		this.contextDownloadExcel = contextDownloadExcel;
	}
	
	this.setNameFilePdf = function(nameFilePdf)
	{
		this.nameFilePdf = nameFilePdf;
	}
	
	this.setContextDownloadPdf = function(contextDownloadPdf)
	{
		this.contextDownloadPdf = contextDownloadPdf;
	}
	
	this.downloadExcel = function(numItem = 10)
	{
		
		$('body').loadingModal('show');
		
		var fileName = this.nameFileExcel+formatDateUs(new Date());
		var serializeForm = $(this.formSerialize).serialize();
		let filterData = "pageSize="+numItem+"&"+serializeForm;
		let fileUrl = window.location.origin+this.contextDownloadExcel+fileName+".xls?"+filterData;
		
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
			    toastr.error("Houve um erro durante o download do Relatório Excel");
			  }
			})
			.catch((error) => {
				$('body').loadingModal('hide');
				toastr.error("Houve um erro durante o download do Relatório Excel");
			});
		
	};
	
	this.downloadPdf = function(numItem = 10)
	{
		$('body').loadingModal('show');
		
		var fileName = this.nameFileExcel+formatDateUs(new Date());
		var serializeForm = $(this.formSerialize).serialize();
		let filterData = "pageSize="+numItem+"&"+serializeForm;
		let fileUrl = window.location.origin+this.contextDownloadExcel+fileName+".pdf?"+filterData;
		
			fetch(fileUrl).then((response) => {
				
			  if (response.status == 200) {
				  response.blob().then(blob => {
					  	const url = window.URL.createObjectURL(blob);
					    const a = document.createElement('a');
					    a.style.display = 'none';
					    a.href = url;
					    a.download = fileName+".pdf";
					    document.body.appendChild(a);
					    a.click();
					    window.URL.revokeObjectURL(url);
					    $('body').loadingModal('hide');
				  });
				  
			  } else {
			    $('body').loadingModal('hide');
			    toastr.error("Houve um erro durante o download do Relatório PDF");
			  }
			})
			.catch((error) => {
				$('body').loadingModal('hide');
				toastr.error("Houve um erro durante o download do Relatório PDF");
			});
	};
	
	this.build = function(numItem = 10)
	{
		
		var formFilter = $(this.formSerialize).serialize();
		
		let filterData = "pageSize="+numItem+"&"+formFilter;
		
		var url = window.location.origin + this.url + `?${filterData}`;
		
		$(this.paginationContainer).pagination({
			dataSource: url,
			pageSize:numItem,
			locator:this.locator,
			totalNumberLocator: (response) => {
				$('body').loadingModal('show');
				let total = response.totalElements;
				$(this.countResult).html(total);
				return parseInt(total);
			},
			callback: function(data, pagination) {
				
				
				var html = "";
				if(this.locator=="servicos")
				{
					html = simpleTemplateServices(data);
				}
				
				if(this.locator=="agendamentos")
				{
					html = simpleTemplateSchedules(data);
				}
				
				if(this.locator=="clientes")
				{
					html = simpleTemplateClients(data);
				}
				
				if(this.locator=="usuarios")
				{
					html = simpleTemplatingManagement(data);
				}
				
				if(this.locator=="requests")
				{
					html = simpleTemplatingManagementRequests(data);
				}
				
				if(this.locator=="products")
				{
					html = simpleTemplatingManagementProducts(data);
				}
				
				$(tableGrid).html(html);
				
				$('body').loadingModal('hide');
				
			}
		});
		
	}
	
	var formatDateUs = function(data)
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
	
	var calculateTotalValue = function(requests,dtNascimento,idService)
	{
		
		var total = parseInt(0);
		
		for(let request of requests)
		{
			if(request.dataExclusao == null)
			{
				total = total + parseInt(request.pedido.preco.replace('R$',''));
			}
		}
		
		let happyBirthdayOfClient = new HappyBirthdayOfClient();
		
		total = happyBirthdayOfClient.calculateTotalValue(dtNascimento,total,idService);
		
		return "R$"+total;
	}
	
	var simpleTemplatingManagementProducts = function(data)
	{
		if(data.length == 0)
		{
			return notTemplateProducts();
		}
		
		var line = "";
		for(let object of data)
		{
			
			line += "<tr>";
					
			line += "<td>"+object.nome+"</td>"
			
			line += "<td>"+object.preco+"</td>";
	
			line += "<td><a href='#' class='img_edit' onClick='editProduct("+object.idPedido+")'><img src='img/edit-icon.png' width='30' height='30'></a> <a href='#' onClick='showEraseProduct("+object.idPedido+")'><img src='img/erase-icon.png' class='img_erase'></a></td>";
			
			line += "</tr>";

		}
			
		return line;
	}
	
	var simpleTemplatingManagementRequests = function(data)
	{
		if(data.length == 0)
		{
			return notTemplate();
		}
		
		var line = "";
		for(let object of data)
		{
			
			line += "<tr>";
					
			line += "<td>"+object.nome+"</td>";
			
			var hour = object.tempo.split(':');
					
			line += "<td>"+hour[0]+":"+hour[1]+"</td>";
			
			line += "<td>"+object.preco+"</td>";
			
			line += "<td><a href='#' onClick='showOrderMakers("+object.idPedido+")'>"+object.tiposPerfis+"</a></td>";
	
			line += "<td><a href='#' class='img_edit' onClick='editRequest("+object.idPedido+")'><img src='img/edit-icon.png' width='30' height='30'></a> <a href='#' onClick='showEraseRequest("+object.idPedido+")'><img src='img/erase-icon.png' class='img_erase'></a></td>";
			
			line += "</tr>";

		}
			
		return line;
	};
	
	var simpleTemplatingManagement = function(data)
	{
		
		
		if(data.length == 0)
		{
			return notTemplate();
		}

		var line = "";
		var count = 0;
		for(let object of data)
		{
			
			
		
			line += "<tr>";
					
			line += "<td>"+object.funcionario.nome.split(' ')[0]+"</td>";
			
			line += "<td>"+object.tipoPerfil.tipo+"</td>";
					
			line += "<td>"+object.funcionario.cpf+"</td>";
					
			line += "<td>"+formatLocalDate(new Date(object.funcionario.dtNascimento))+"</td>";
					
			line += "<td>"+object.funcionario.email+"</td>";
					
			line += "<td>"+object.funcionario.telefone+"</td>";
			
			let endereco = checkAdressIsNullOrEmpty(object.funcionario.endereco);
			
			line += "<td><a href='#'><img src='img/iconMap.png' width='40' height='40' adress='"+endereco+"' class='adressInfo"+count+" img_local' onClick='showAdress("+count+")'/></a></td>";
					
			line += "<td><a href='#' class='img_edit' onClick='editEmployee("+object.funcionario.id+")'><img src='img/edit-icon.png' width='30' height='30'></a> <a href='#' onClick='showEraseEmployee("+object.funcionario.id+")'><img src='img/erase-icon.png' class='img_erase'></a> <a href='#' class='img_edit' onClick='editPassword("+object.id+")'><img src='img/key.png' width='30' height='30'></a><a href='#' class='img_ranking_employee' onClick='rankingEmployee("+object.id+")'><img src='img/ranking-employee.png' width='23' height='25'></a></td>";
			
			line += "</tr>";
			
			count++;
		}
			
		return line;
	};
	
	var checkAdressIsNullOrEmpty = function(adress)
	{
		if((adress == null) || (adress == ""))
		{
			return "(sem endereço)";
		}
		
		return adress;
	};
	
	var notTemplate = function()
	{
		var line = "";
		
		line += "<tr>";
				
		line += "<td></td>";
				
		line += "<td></td>";
				
		line += "<td></td>";
				
		line += "<td><label style='position:relative;left:-45px;'>Não há registros</label></td>";
				
		line += "<td></td>";
		
		line += "<td></td>";
				
		line += "</tr>";
			
		return line;
	}
	
	var notTemplateProducts = function()
	{
		var line = "";
		
		line += "<tr>";
				
		line += "<td></td>";
				
		line += "<td><label style='position:relative;left:10px;'>Não há registros</label></td>";
				
		line += "<td></td>";
				
		line += "</tr>";
			
		return line;
	}
	
	var constructRequest = function(item)
	{
		return "<a href='#' onClick='showRequest("+item.pedidoServico[0].idServico+");'><img src='img/listagem.png' class='img_details_service'/></a>";
	};
	
	var simpleTemplateServices = function(data)
	{

		if(data.length == 0)
		{
			return notTemplate();
		}
		var count = 0;
		var line = "";
		for(var item of data)
		{
			if(item != null)
			{
				let descricao = "";
				
				let infoEmployee = "<a href='#' onClick='showModalEmployee("+item.id+")'><img src ='img/info-employee.png' width='30' height='30' title='"+item.usuario.funcionario.nome+"' client='"+item.cliente.nome+"' profile='"+item.usuario.tipoPerfil.tipo+"'  class='info_employee"+item.id+"'/></a>";
				
				if(item.descricaoServico != "")
				{
					descricao = "<a href='#' onClick=showDescription("+item.id+")><input type='hidden' value='"+item.descricaoServico+"' id='descriptionHidden"+item.id+"'><img src='img/fala-balao.png' width='20' height='20'></a>";
				}
				
				line += "<tr>";
				
				line += "<td>"+constructRequest(item)+descricao+infoEmployee+"</td>";
				let happyBirthdayOfClient = new HappyBirthdayOfClient();
				line += "<td>"+calculateTotalValue(item.pedidoServico,item.cliente.dtNascimento)+happyBirthdayOfClient.checkIconBirthday(item.cliente.dtNascimento)+"</td>";
				
				line += "<td>"+item.cliente.cpf+"</td>";
				
				line += "<td>"+formatDateTime(new Date(item.dataCriacao))+"</td>";
				
				line += "<td>"+getTranslationStatus(item.status)+"</td>";
				
				line += "<td><a href='#' class='img_edit' onClick='editServices("+item.id+")'><img src='img/edit-icon.png' width='30' height='30'></a> <a href='#' onClick='showEraseService("+item.id+")'><img src='img/erase-icon.png' class='img_erase'></a></td>";
				
				line += "</tr>";
			}
			
			count++;
		}
		
		return line;
	}
	
	var checkinOrScheduledFinal = function(servico)
	{
		if(servico.agendamento.checkin != null)
		{
			return "<a href='#' onClick='sheduledFinal("+servico.id+")'><img src='img/checkin.png' class='img_schedule_final' title='Data do checkin: "+formatDateTime(new Date(servico.agendamento.checkin))+". Finalização do agendamento'></a>";
		}
		
		return "<a href='#' onClick='checkin("+servico.id+")'><img src='img/ok.png' class='img_checkin' title='Checkin de Presença'></a>";
	}
	
	var simpleTemplateSchedules = function(data)
	{
		if(data.length == 0)
		{
			return notTemplate();
		}
		
		var line = "";
		var count = 0;
		for(var item of data)
		{
			if(item != null)
			{
				var imgCheckin = checkinOrScheduledFinal(item);
				
				let descricao = "";
				
				let infoEmployee = "<a href='#' onClick='showModalEmployee("+item.id+")'><img src ='img/info-employee.png' width='30' height='30' title='"+item.usuario.funcionario.nome+"' client='"+item.cliente.nome+"' profile='"+item.usuario.tipoPerfil.tipo+"'  class='info_employee"+item.id+"'/></a>";
				
				if(item.descricaoServico != "")
				{
					descricao = "<a href='#' onClick=showDescription("+item.id+")><input type='hidden' value='"+item.descricaoServico+"' id='descriptionHidden"+item.id+"'><img src='img/fala-balao.png' width='20' height='20'></a>";
				}
				
				line += "<tr class='schedulesId"+item.agendamento.id+"'>";
				
				line += "<td>"+constructRequest(item)+descricao+infoEmployee+"</td>";
				let happyBirthdayOfClient = new HappyBirthdayOfClient();
				line += "<td class='tdValor'>"+calculateTotalValue(item.pedidoServico,item.cliente.dtNascimento)+happyBirthdayOfClient.checkIconBirthday(item.cliente.dtNascimento)+"</td>";
				
				line += "<td class='tdCpf'>"+item.cliente.cpf+"</td>";
				
				line += "<td class='tdData'>"+formatLocalDate(new Date(item.agendamento.data))+"</td>";
				
				line += "<td class='tdHora'>"+item.agendamento.hora.replace(":00","")+"</td>";
				
				line += "<td><a href='#' class='img_edit' onClick='editSchedules("+item.id+")'><img src='img/edit-icon.png' width='30' height='30'></a><a href='#' onClick='showEraseSchedules("+item.id+")'><img src='img/erase-icon.png' class='img_erase'></a>"+imgCheckin+"</td>";
				
				line += "</tr>";
			}
			
			count++;
		}
		
		return line;
	}
	
	var simpleTemplateClients = function(data)
	{
		if(data.length == 0)
		{
			return notTemplate();
		}
		
		var count = 0;
		var line = "";
		for(var item of data)
		{
			if(item != null)
			{
				line += "<tr>";
				
				line += "<td>"+item.nome.split(" ")[0]+"</td>";
				
				line += "<td>"+item.cpf+"</td>";
				
				line += "<td>"+formatLocalDate(new Date(item.dtNascimento))+"</td>";
				
				line += "<td>"+item.email+"</td>";
				
				line += "<td>"+item.telefone+"</td>";
				
				line += "<td><a href='#'><img src='img/iconMap.png' width='40' height='40' adress='"+checkAdressIsNullOrEmpty(item.endereco)+"' class='adressInfo"+count+" img_local' onClick='showAdress("+count+")'/></a></td>";
				
				line += "<td><a href='#' class='img_edit' onClick='editClients("+item.id+")'><img src='img/edit-icon.png' width='30' height='30'></a><a href='#' onClick='showEraseClients("+item.id+")'><img src='img/erase-icon.png' class='img_erase'></a></td>";
				
				line += "</tr>";
				
				count++;
			}
		}
		
		return line;
	};
	
	var getTranslationStatus = function(status)
	{
		if(status=="IN_PROGRESS")
		{
			return "Em progresso";
		}
		
		if(status=="DONE")
		{
			return "Finalizado";
		}
		
		return status;
		
	}
	
	var formatDateTime = function(data){
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
	

}