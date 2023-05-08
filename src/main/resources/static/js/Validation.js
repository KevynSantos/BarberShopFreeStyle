function ValidationException(message) {
   this.message = message;
   this.name = "ValidationException";
}

var phone_pattern = /\([0-9][0-9]\) ([0-9]{4}|[0-9]{5})\-[0-9]{4}/;
	
var email_pattern = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

class Validation
{
	
	//requer toastr.js
	static validTelephone(telephone)
	{
		$(telephone).change(function(){
			
			var situation = phone_pattern.test(this.value);
			
			var valueStr = $(telephone).val();

			if((!situation && this.value!="") || (valueStr.length==16 || valueStr.length>16 ))
			{
				toastr.warning('O telefone esta incorreto!');
			}
				
			
		});
			
	}
	
	static validAllTelephones()
	{
		
		$("input.telefone")
        .mask("(99) 9999-99999")
        .focusout(function (event) {  
            var target, phone, element;  
            target = (event.currentTarget) ? event.currentTarget : event.srcElement;  
            phone = target.value.replace(/\D/g, '');
            element = $(target);  
            element.unmask();  
            if(phone.length > 10) {  
                element.mask("(99) 99999-9999");  
            } else {  
                element.mask("(99) 9999-99999");  
            }  
        });
	}
	
	static validEmail(email)
	{
		$(email).change(function(){
			
			var situation = email_pattern.test(this.value);
			
			if(!situation && this.value != "")
			{
				toastr.warning('O email esta incorreto!');
			}
			
		});
	}
	
	static assertValidEmail(value)
	{
		if(value=="")
		{
			return;
		}
		
		var valid = email_pattern.test(value);
		
		if(!valid)
		{
			throw new ValidationException("Email inválido");
		}
	}
	
	static assertValidTelefone(value)
	{
		if(value=="")
		{
			return;
		}
		
		var valid = phone_pattern.test(value);

		if(!valid)
		{
			throw new ValidationException("Telefone inválido");
		}
	}
}