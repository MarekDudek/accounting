package md.accounting.ipko;

import md.accounting.ipko.domain.Operation;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public enum PostgreSqlInsertsWriter
{
    ;

    public static String createInsert(Operation operation)
    {
        return format(
                "INSERT INTO operations (exec_date, order_date, type, description, amount, amount_currency, ending_balance) VALUES ('%s', '%s', '%s', '%s', %s, '%s', %s);",
                operation.getExecDate(),
                operation.getOrderDate(),
                operation.getType(),
                operation.getDescription().replace("'", "''"),
                operation.getAmount().getValue(),
                operation.getAmount().getCurrency(),
                operation.getEndingBalance().getValue()
        );
    }

    public static String createInsert(List<Operation> operations)
    {
        return "INSERT INTO operations (exec_date, order_date, type, description, amount, amount_currency, ending_balance) VALUES " +
                operations.stream().map(operation ->
                        format("('%s', '%s', '%s', '%s', %s, '%s', %s)",
                                operation.getExecDate(),
                                operation.getOrderDate(),
                                operation.getType(),
                                operation.getDescription().replace("'", "''"),
                                operation.getAmount().getValue(),
                                operation.getAmount().getCurrency(),
                                operation.getEndingBalance().getValue()
                        )
                ).collect(joining(", ")) +
                ";";
    }
}
