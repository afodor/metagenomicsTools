
var MyFunc = function()
{
	this.x =0;
	this.increment = function() { this.x++}
}

// use of the new keyword gives us Java like private variables (with closure keeping x in scope)
funcA = new MyFunc()
funcB = new MyFunc()

funcA.increment()
funcB.increment()
funcB.increment()

console.log(funcA.x)
console.log(funcB.x)

funcC = Object.create(MyFunc)

console.log(Object.getPrototypeOf(funcC)) 

// here, increment lives in the prototype of 
// funcC, so is not directly accessible
//funcC.increment()
