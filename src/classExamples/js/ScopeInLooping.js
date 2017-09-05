
// each invocation of the function has access to a new instance of the scoped variable
function loop(id)
{
	var aVal = 1000 * id;
	
	setInterval( function(){
		aVal = aVal +10;
		console.log(id + " " + aVal)
	},100)
}

loop(1)
loop(2)
