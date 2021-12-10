function Home()
{
	this.init = function()
	{
		$('body').css('background-image', 'url(/img/barbearia-fundo-index-orig.jpg)');
	}
}

try
{
	var home = new Home();
	var $document = $( document );
	$document.ready( home.init );
}
catch ( exception )
{
	console.error( 'home.js:'+exception );
}