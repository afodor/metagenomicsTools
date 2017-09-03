

function MyFunc() {
let aVal = 0;
this.getAVal = function(){
return aVal;
};
this.increment = function(){
aVal++;
}
}

// invoking directly adds properties to "global" environment

MyFunc()

increment()
increment()

console.log(getAVal())

// which kind of works as a static

MyFunc()
console.log(getAVal())

// use of the "new" keywork allows for a more Java like OO scheme (via closures)

myObj1 = new MyFunc()
myObj2 = new MyFunc()

myObj1.increment()
myObj1.increment()
myObj1.increment()
myObj2.increment()

console.log(myObj1.getAVal())
console.log(myObj2.getAVal())
