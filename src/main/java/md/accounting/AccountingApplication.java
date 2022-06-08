package md.accounting;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import lombok.extern.slf4j.Slf4j;
import md.accounting.ipko.Deser;
import md.accounting.ipko.domain.AccountHistory;
import md.accounting.ipko.domain.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static md.accounting.ipko.LocalDateDomain.LocalDates;

@SpringBootApplication
@Slf4j
public class AccountingApplication implements CommandLineRunner
{
    public static void main(String[] args)
    {
        SpringApplication.run(AccountingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        if (args.length == 0)
            return;
        Path directory = new File(args[0]).toPath();
        log.info("Listing directory {}", directory);
        List<Path> files =
                Files.list(directory).
                        sorted().
                        collect(toList());
        Deser deser = new Deser();
        List<AccountHistory> histories =
                files.stream().map(file ->
                        deser.deserialize(file.toString())
                ).collect(toList());
        log.info("Deserialized {} histories", histories.size());
        List<Date> dates =
                histories.stream().map(history ->
                        history.getSearch().getDate()
                ).collect(toList());
        List<Range<LocalDate>> ranges =
                dates.stream().map(date -> {
                            LocalDate since = date.getSince();
                            LocalDate to = date.getTo();
                            Range<LocalDate> range = Range.closed(since, to);
                            return range.canonical(LocalDates);
                        }
                ).collect(toList());
        RangeSet<LocalDate> set =
                ranges.stream().reduce(
                        TreeRangeSet.create(),
                        (s, r) -> {
                            s.add(r);
                            return s;
                        },
                        (a, b) -> null
                );
        log.info("Range: {}", set);
    }
}
