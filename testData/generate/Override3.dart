class Foo extends Bar<Baz> {
  <caret>
}

class Bar<T extends Base> {
  T find(boolean condition(T item)) {
    return false;
  }
}

class Baz extends Base{}

class Base {}