# Forum-automation

List of nice-to-have improvements:

1. separate pages into their own package
1. use custom elements (Button, TextBox) instead of generic WebElements
1. implement nested elements that work fine with pageFactory. Currently nested elements are handled directly in page behavior methods.
While this technically fits to page object concept, since we have one place in code per locator, I'd like to have nested elements structure.
1. data preparation through api (create/delete users, ensure state of the system before start of the test)
