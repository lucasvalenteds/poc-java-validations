# POC: Java Validation

It demonstrates different approaches to validate data in Java applications.

We want to explore software design techniques, patterns, libraries and frameworks that aim to make it easy for us to
validate data in different type of applications. Each Gradle module demonstrates a different approach (annotation based,
interfaces, pattern) and must be self-contained.

The goal is to find out the trade-offs of each approach and not to have a production-ready line of business application.
No application is implemented here, the source code is evaluated and verified using automated unit tests managed by
JUnit.

## How to run

| Description   | Command          |
|:--------------|:-----------------|
| Run all tests | `./gradlew test` |

## Preview

`jakarta-validation`:

```java
@Test
void creatingCustomerWithLastName() {
    final var customer = new Customer(
        new CustomerId(UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492")),
        new FirstName("John"),
        Optional.of(new LastName("Smith"))
    );

    final var violations = validator.validate(customer);

    assertThat(violations)
        .isEmpty();
}
```

`layered`:

```java
@Test
void creatingCustomerWithLastName() {
    final var id = UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492");
    final var firstName = "John";
    final var lastName = "Smith";

    final var validCustomer = customerService.create(id, firstName, lastName);

    assertEquals(firstName, validCustomer.firstName().value());
    assertEquals(lastName, validCustomer.lastName().orElseThrow().value());
    assertEquals("John Smith", validCustomer.fullName());
    assertEquals("15d5650e-82b3-4239-abac-de48e65e3492", validCustomer.id().value().toString());
}
```

`result`:

```java
@Test
void creatingCustomerWithLastName() {
    final var result = CustomerId.of(UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492"))
        .flatMap(customerId -> FirstName.of("John")
        .flatMap(firstName -> LastName.of("Smith")
        .flatMap(lastName -> Customer.of(customerId, firstName, lastName))));

    assertEquals(Result.Success.class, result.getClass());

    final var customer = result.value();
    assertEquals("15d5650e-82b3-4239-abac-de48e65e3492", customer.id().value().toString());
    assertEquals("John", customer.firstName().value());
    assertEquals("Smith", customer.lastName().orElseThrow().value());
    assertEquals("John Smith", customer.fullName());
}
```

`validator`:

```java
@Test
void creatingCustomerWithLastName() {
    final var customer = new Customer(
        new CustomerId(UUID.fromString("15d5650e-82b3-4239-abac-de48e65e3492")),
        new FirstName("John"),
        Optional.of(new LastName("Smith"))
    );

    final var validCustomer = CustomerValidator.customerId()
        .and(CustomerValidator.firstName())
        .and(CustomerValidator.lastName())
        .apply(customer)
        .<Customer>getOrElseThrow();

    assertEquals("John", validCustomer.firstName().value());
    assertEquals("Smith", validCustomer.lastName().orElseThrow().value());
    assertEquals("John Smith", validCustomer.fullName());
    assertEquals("15d5650e-82b3-4239-abac-de48e65e3492", validCustomer.id().value().toString());
}
```
