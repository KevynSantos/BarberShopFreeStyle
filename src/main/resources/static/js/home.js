function Home()
{
	this.init = function()
	{

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