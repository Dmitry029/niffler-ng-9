package guru.qa.niffler.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IsolationLevel {
    READ_UNCOMMITTED(1),
    READ_COMMITTED(2),
    REPEATABLE_READ(3),
    SERIALIZABLE(4);

    public final int value;
}
