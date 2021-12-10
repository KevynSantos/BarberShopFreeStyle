class HappyBirthdayOfClient
{
	checkDiscount(dtNascimento)
	{
		
		var now = new Date();
		
		var monthByNow = this.getMonthByDate(now);
		
		var monthByNasc = this.getMonthByDate(new Date(dtNascimento));
		
		if(monthByNow == monthByNasc)
		{
			return true;
		}
		
		return false;
		
	}
	
	checkIconBirthday(dtNascimento)
	{
		if(this.checkDiscount(dtNascimento) == true)
		{
			return "<img src='img/birthday-cake.png' height='30' width='30' style='margin-left: 5px; margin-bottom: 2px;'>";
		}
		
		return "";
	}
	
	
	
	calculateTotalValue(dtNascimento,valueTotal)
	{
		if(this.checkDiscount(dtNascimento) == true)
		{
			var situation = $("#discount_of_client").text();
			
			if((situation == "") || (situation == null))
			{
				return valueTotal;
			}
			
			var discount = parseInt($("#discount_of_client").text());
			
			var calcDiscount = discount/100;
			
			var total = (valueTotal*calcDiscount);
			
			var desconto = valueTotal - total;
			
			desconto = desconto.toString().replace(".",",");
			
			return desconto;
		}
		
		return valueTotal+",00";
	}
	
	getMonthByDate(data)
	{
		
		let mes  = (data.getMonth()+1).toString(); //+1 pois no getMonth Janeiro começa com zero.
		let mesF = (mes.length == 1) ? '0'+mes : mes;
						
		return mesF;
	}
	
	
}