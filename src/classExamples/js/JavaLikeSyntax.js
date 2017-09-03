
// modified from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Inheritance_and_the_prototype_chain

'use strict';

class Polygon {
  constructor(height, width) {
    this.height = height;
    this.width = width;
  }
}

class Square extends Polygon {
  constructor(sideLength) {
    super(sideLength, sideLength);
  }
  get area() {
    return this.height * this.width;
  }
  set sideLength(newLength) {
    this.height = newLength;
    this.width = newLength;
  }
}

var square1 = new Square(2);
var square2 = new Square(3);

// works as in java (with setters and getters formalized as parameters
console.log( square1.area +  " " + square2.area)
square2.sideLength=10;
console.log( square1.area +  " " + square2.area)


// Object.create bypasses the constructor 
// (Java would not allow you do instantiate an object without invoking constructor)
var square3 = Object.create(Square)
console.log( square3.area)