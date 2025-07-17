package com.myLearning.part09_sinks;

// Single API response and can have N number of subscribers => Sinks.one() [ Emits only one value or error, multiple subscribers can subscribe but will all get the same single value ]
// Single trigger (no value) => Sinks.empty() [ Emits no value, only completes or errors; useful when you just want to signal completion ]
// Live stream to 1 subscriber => Sinks.many().unicast() [Subscribers can join late and messages will be stared in the memory]
// Live stream to many subscribers => Sinks.many().multicast() [ Multiple subscribers allowed, but late subscribers will NOT receive previous messages**, they get data from the point they subscribe ]
// Chat messages or replaying old data and have N number of subscribers => Sinks.many().replay() [ Multiple subscribers allowed; late subscribers receive previously emitted messages (replay) — behavior depends on .all(), .latest(), or .limit(n) ]
/*
        | Sink Type              | Subscribers | Values | Replay | Use Case               |
        | ---------------------- | ----------- | ------ | ------ | ---------------------- |
        |  Sinks.One             | Many        | 1      |   Yes  | API Response           |
        |  Sinks.Empty           | Many        | 0      |   N/A  | Completion signal      |
        |  Sinks.Many.unicast    | One         | ∞      |   No   | Private data stream    |
        |  Sinks.Many.multicast  | Many        | ∞      |   No   | Live data to many      |
        |  Sinks.Many.replay     | Many        | ∞      |   Yes  | Replaying chat/history |



| Sink Type                  | Description                                 | Bracket Explanation                                                              |
| -------------------------- | ------------------------------------------- | -------------------------------------------------------------------------------- |
| Sinks.one()                | Single value to many                        | [ 1 value or error, multiple subscribers get same value ]                        |
| Sinks.empty()              | Complete without data                       | [ No data, just complete or error ]                                              |
| Sinks.many().unicast()     | Multi-values, 1 subscriber                  | [ Only one subscriber, late joiner gets all buffered messages ]                  |
| Sinks.many().multicast()   | Multi-values, many subscribers              | [ Many subscribers, no replay, subscribers get data from subscribe point ]       |
| Sinks.many().replay()      | Multi-values, many subscribers, with replay | [ Many subscribers, with replay of previous messages to late joiners ]           |

*/
public class Part03SinkTypes {
    public static void main(String[] args) {

    }
}
