function Error()
{
	this.init = function()
	{
		
		var modalError = "<div id='errorModal' class='modal modal-group fade' tabindex='-1' role='dialog'><div class='modal-dialog' role='document'><div id='modal-content-continue' class='modal-content modal-continue'><div class='modal-header'><h3>Erro Encontrado</h3></div><div class='modal-body'><label class='msg_error'></label></div><div class='modal-footer'><div class='btn_submit_cancel'><button type='button' class='btn_ok_error'>Fechar</button></div></div></div></div></div>";
		
		$( "body" ).append( modalError );
		
		$(".btn_ok_error").click(function(){
			
			$("#errorModal").modal('hide');
			$(".msg_error").text('');
			
		});
		
	};
	
	this.show = function(message)
	{
		$(".msg_error").text(message);
		$("#errorModal").modal('show');
	};
	
	
}

try
{
	var error = new Error();
	var $document = $( document );
	$document.ready( error.init );
}
catch ( exception )
{
	console.error( 'error.js:'+exception );
}