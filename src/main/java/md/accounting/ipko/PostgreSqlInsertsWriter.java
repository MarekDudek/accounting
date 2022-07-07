package md.accounting.ipko;

import md.accounting.ipko.domain.Operation;

public enum PostgreSqlInsertsWriter
{
    ;

    public static String createInsert(Operation operation)
    {
        return String.format(
                "INSERT INTO operation (exec_date, order_date, type, description, amount, amount_currency, ending_balance) VALUES ('%s', '%s', '%s', '%s', %s, '%s', %s);",
                operation.getExecDate(),
                operation.getOrderDate(),
                operation.getType(),
                operation.getDescription().replace("'", "''"),
                operation.getAmount().getValue(),
                operation.getAmount().getCurrency(),
                operation.getEndingBalance().getValue()
        );
    }
}
