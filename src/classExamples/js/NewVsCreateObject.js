
MyFunc = function()
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

 //here, increment lives in the prototype of 
 //funcC, so is not directly accessible
//funcC.increment()

/////////////////

function Foo()
{
	this.x =0;
	this.increment = function() { this.x++ }
}

var aFoo = new Foo();

console.log(Object.getPrototypeOf(aFoo)) 
funcD = Object.create(aFoo);
funcD.increment()
funcD.increment()
funcD.increment()
funcE = Object.create(aFoo);

// truly miserable scoping!  
// because this.x++ got called on funcD, funcD has it's own copy of X
// but FuncE does not!
console.log(funcD.x);
console.log(funcE.x);

aFoo.increment()

funcF = Object.create(aFoo);
// here funcD's version of x does not get incremented,
// but funcE and funcF, that never got a call to increment()
// share a static!
console.log(funcD.x);
console.log(funcE.x);
console.log(funcF.x);

// below, if I never use p and q, they act as if they share a static
function Foo2() 
{
	this.a = 5
	this.b = 6
}

var aFoo2 =new Foo2()


var p = Object.create(aFoo2)
var q = Object.create(aFoo2)
aFoo2.a = 100
console.log(q.a)
console.log(p.a)

// so Object.create() gives us a lot of flexibility
// but should maybe be avoided without careful consideration
