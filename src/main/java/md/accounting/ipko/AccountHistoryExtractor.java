package md.accounting.ipko;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import md.accounting.ipko.domain.AccountHistory;
import md.accounting.ipko.domain.Date;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static md.accounting.ipko.LocalDateDomain.LocalDates;

public enum AccountHistoryExtractor
{
    ;

    private static final Deser DESER = new Deser();

    public static List<AccountHistory> read(Path directory) throws IOException
    {
        Stream<Path> fs = Files.list(directory).sorted();
        return fs.map(f ->
                DESER.deserialize(f.toString())
        ).collect(toList());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static RangeSet<LocalDate> range(List<AccountHistory> histories)
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
    public static boolean isValid(RangeSet<LocalDate> set)
    {
        return set.asRanges().size() == 1;
    }
}
