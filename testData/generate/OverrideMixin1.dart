abstract class Mixin {
  mixinMethod() {}
}

class AClass extends Object with Mixin {
  <caret>
}