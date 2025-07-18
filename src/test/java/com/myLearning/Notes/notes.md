### StepVerifier Operators

| Operator              | Purpose                                   |
| --------------------- | ----------------------------------------- |
| `.expectNext()`       | Expect specific value                     |
| `.assertNext()`       | Use custom assertions                     |
| `.expectComplete()`   | Expect normal completion                  |
| `.expectError()`      | Expect an error                           |
| `.thenConsumeWhile()` | Consume while condition matches           |
| `.expectNextCount(n)` | Expect exactly `n` number of values       |
| `.verify()`           | Triggers subscription and runs assertions |

### StepVerifier Use Cases

| Use Case                | Operator                                    |
|-------------------------| ------------------------------------------- |
| Mono, one value         | `.expectNext()`                             |
| Flux, many values       | `.expectNext()`, `.expectNextCount()`       |
| Custom assertions       | `.assertNext()`                             |
| Test error flow         | `.expectError()` or `.expectErrorMessage()` |
| Test stream completion  | `.expectComplete()`                         |
| Testing while filtering | `.thenConsumeWhile()`                       |