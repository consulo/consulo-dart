add(Foo foo, Bar bar) {
  <caret>
}

main() {
  var a = new Foo();
  var result = add(a, new Bar());
}

class Foo{}
class Bar{}