function CalculateTime()
{
	var fieldTime = null;
	var fieldDate = null;
	var seeFinalTime = true;
	this.setFieldTime = function(field)
	{
		fieldTime = field;
	};
	
	this.setFieldDate = function(field)
	{
		fieldDate = field;
	};
	
	this.setFinalTime = function(see)
	{
		seeFinalTime = see;
	}
	
	this.days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
	
	this.init = function()
	{
		var now = new Date();
		var day = this.days[ now.getDay() ];
		if(['Sunday','Saturday'].includes(day))
		{
			$(fieldTime).timepicker({
			    timeFormat: 'HH:mm',
			    interval: 60,
			    minTime: '08:00am',
			    maxTime: '13:00pm',
			    startTime: '8:00',
			    dynamic: false,
			    dropdown: true,
			    scrollbar: true,
			    change: function(time) {
			    	
			    	if(seeFinalTime == true)
			    	{
		        		calculateIntervalScheduling();
			    	}
		        }
			});
		}
		else
		{
			$(fieldTime).timepicker({
			    timeFormat: 'HH:mm',
			    interval: 60,
			    minTime: '08:00am',
			    maxTime: '19:00pm',
			    startTime: '8:00',
			    dynamic: false,
			    dropdown: true,
			    scrollbar: true,
			    change: function(time) {
			    	
			    	if(seeFinalTime == true)
			    	{
		        		calculateIntervalScheduling();
			    	}
		        }
			});
		}
	};
	
	this.initChange = function(dateUs)
	{
		
		
		document.getElementById(fieldDate.replace("#","")).value = dateUs;
		
		var day = null;
		$.ajax({
		     url : contextPath + "/scheduling/calculateTime?dateScheduling="+dateUs,
		     type : 'GET',
		     async: false,
		     processData: false,
		     contentType: false,
		     beforeSend : function(){
		          //none
		     },
		     success: function(data) { 
		            
		    	 var response = JSON.parse(data);
		    	 day = response.week;
		            			
		     }
		})
		.fail(function(jqXHR, textStatus, msg){
		     toastr.error('Houve um erro durante a solicitação!');
		     error.show(msg);
		});

		if(['SUNDAY','SATURDAY'].includes(day))
		{
			$(fieldTime).timepicker('destroy');
			
			$(fieldTime).timepicker({
			    timeFormat: 'HH:mm',
			    interval: 60,
			    minTime: '08:00am',
			    maxTime: '13:00pm',
			    startTime: '8:00',
			    dynamic: false,
			    dropdown: true,
			    scrollbar: true,
			    change: function(time) {
			    	if(seeFinalTime == true)
			    	{
		        		calculateIntervalScheduling();
			    	}
		        }
			});
		}
		else
		{
			$(fieldTime).timepicker('destroy');
			
			$(fieldTime).timepicker({
			    timeFormat: 'HH:mm',
			    interval: 60,
			    minTime: '08:00am',
			    maxTime: '19:00pm',
			    startTime: '8:00',
			    dynamic: false,
			    dropdown: true,
			    scrollbar: true,
			    change: function(time) {
			    	
			    	if(seeFinalTime == true)
			    	{
		        		calculateIntervalScheduling();
			    	}
		        }
			});
		}
	};
	
	this.calc = function()
	{
		
		$(fieldDate).change(function(){
			var date = $(fieldDate).val();
			var day = null;
			$.ajax({
			     url : contextPath + "/scheduling/calculateTime?dateScheduling="+date,
			     type : 'GET',
			     async: false,
			     processData: false,
			     contentType: false,
			     beforeSend : function(){
			          //none
			     },
			     success: function(data) { 
			            
			    	 var response = JSON.parse(data);
			    	 day = response.week;
			            			
			     }
			})
			.fail(function(jqXHR, textStatus, msg){
			     toastr.error('Houve um erro durante a solicitação!');
			     error.show(msg);
			});

			if(['SUNDAY','SATURDAY'].includes(day))
			{
				$(fieldTime).timepicker('destroy');
				
				$(fieldTime).timepicker({
				    timeFormat: 'HH:mm',
				    interval: 60,
				    minTime: '08:00am',
				    maxTime: '13:00pm',
				    startTime: '8:00',
				    dynamic: false,
				    dropdown: true,
				    scrollbar: true,
				    change: function(time) {
				    	if(seeFinalTime == true)
				    	{
			        		calculateIntervalScheduling();
				    	}
			        }
				});
			}
			else
			{
				$(fieldTime).timepicker('destroy');
				
				$(fieldTime).timepicker({
				    timeFormat: 'HH:mm',
				    interval: 60,
				    minTime: '08:00am',
				    maxTime: '19:00pm',
				    startTime: '8:00',
				    dynamic: false,
				    dropdown: true,
				    scrollbar: true,
				    change: function(time) {
				    	
				    	if(seeFinalTime == true)
				    	{
			        		calculateIntervalScheduling();
				    	}
			        }
				});
			}
			
		});
	};

}