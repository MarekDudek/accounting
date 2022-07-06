package md.accounting.ipko;

import md.accounting.ipko.domain.AccountHistory;
import md.accounting.ipko.domain.Operation;

import java.util.List;

import static java.util.Collections.reverse;
import static java.util.stream.Collectors.toList;

public enum OperationsExtractor
{
    ;

    public static List<Operation> extract(List<AccountHistory> histories)
    {
        return histories.stream().flatMap(h -> {
                    List<Operation> ops = h.getOperations().getOperation();
                    reverse(ops);
                    return ops.stream();
                }
        ).collect(toList());
    }
}
