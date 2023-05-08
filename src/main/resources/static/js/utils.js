function Utils()
{
	var formatDateFilter = function(data)
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
		return diaF+"/"+mesF+"/"+anoF;
	}
	
	this.setNowInDateFromFilter = function(field)
	{
		$(field).val(formatDateFilter(new Date()));
	}

}