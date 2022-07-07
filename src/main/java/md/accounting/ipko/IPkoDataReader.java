package md.accounting.ipko;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.Streams;
import com.google.common.collect.TreeRangeSet;
import md.accounting.ipko.domain.AccountHistory;
import md.accounting.ipko.domain.Date;
import md.accounting.ipko.domain.Operation;
import org.javatuples.Triplet;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static java.math.BigDecimal.ZERO;
import static java.util.Collections.reverse;
import static java.util.stream.Collectors.toList;
import static md.accounting.ipko.LocalDateDomain.LocalDates;

public enum IPkoDataReader
{
    ;

    private static final Deser DESER = new Deser();

    public static List<AccountHistory> read
            (
                    Path directory
            ) throws IOException
    {
        Stream<Path> fs = Files.list(directory).sorted();
        return fs.map(f ->
                DESER.deserialize(f.toString())
        ).collect(toList());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static RangeSet<LocalDate> ranges
            (
                    List<AccountHistory> histories
            )
    {
        Stream<Date> ds =
                histories.stream().map(h ->
                        h.getSearch().getDate()
                );
        Stream<Range<LocalDate>> rs =
                ds.map(d -> {
                            LocalDate s = d.getSince();
                            LocalDate t = d.getTo();
                            Range<LocalDate> r = Range.closed(s, t);
                            return r.canonical(LocalDates);
                        }
                );
        return rs.reduce(
                TreeRangeSet.create(),
                (set, range) -> {
                    set.add(range);
                    return set;
                },
                (s1, s2) -> {
                    s1.addAll(s2);
                    return s1;
                }
        );
    }

    @SuppressWarnings("UnstableApiUsage")
    public static boolean isValid
            (
                    RangeSet<LocalDate> set
            )
    {
        return set.asRanges().size() == 1;
    }

    public static List<Operation> extract
            (
                    List<AccountHistory> histories
            )
    {
        return histories.stream().flatMap(h -> {
                    List<Operation> ops = h.getOperations().getOperation();
                    reverse(ops);
                    return ops.stream();
                }
        ).collect(toList());
    }

    public static List<Triplet<Operation, Operation, BigDecimal>> differences
            (
                    List<Operation> operations
            )
    {
        return Streams.zip(
                operations.stream(),
                operations.stream().skip(1),
                (o1, o2) -> {
                    BigDecimal endingBalance = o1.getEndingBalance().getValue();
                    BigDecimal startingBalance = o2.getEndingBalance().getValue().subtract(o2.getAmount().getValue());
                    BigDecimal d = endingBalance.subtract(startingBalance);
                    return Triplet.with(o1, o2, d);
                }
        ).collect(toList());
    }

    public static List<Triplet<Operation, Operation, BigDecimal>> inconsistencies
            (
                    List<Triplet<Operation, Operation, BigDecimal>> differences
            )
    {
        return differences.stream().filter(d ->
                d.getValue2().compareTo(ZERO) != 0
        ).collect(toList());
    }

    public static boolean noInconsistencies
            (
                    List<Triplet<Operation, Operation, BigDecimal>> inconsistencies
            )
    {
        return inconsistencies.isEmpty();
    }
}
