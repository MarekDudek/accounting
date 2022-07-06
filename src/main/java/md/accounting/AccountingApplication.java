package md.accounting;

import com.google.common.collect.RangeSet;
import com.google.common.collect.Streams;
import lombok.extern.slf4j.Slf4j;
import md.accounting.ipko.AccountHistoryExtractor;
import md.accounting.ipko.OperationsExtractor;
import md.accounting.ipko.domain.AccountHistory;
import md.accounting.ipko.domain.Operation;
import org.javatuples.Triplet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.toList;

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
        log.info("Reading from directory {}", directory);

        List<AccountHistory> histories = AccountHistoryExtractor.read(directory);
        log.info("Read {} history files", histories.size());

        @SuppressWarnings("UnstableApiUsage")
        RangeSet<LocalDate> set = AccountHistoryExtractor.range(histories);
        log.info("Search date ranges {}", set);

        if (!AccountHistoryExtractor.isValid(set)) {
            throw new IllegalArgumentException(String.format("Range is not valid: %s", set));
        }

        List<Operation> operations = OperationsExtractor.extract(histories);
        log.info("Read {} operations", operations.size());

        List<Triplet<Operation, Operation, BigDecimal>> differences =
                Streams.zip(
                        operations.stream(),
                        operations.stream().skip(1),
                        (o1, o2) -> {
                            BigDecimal endingBalance = o1.getEndingBalance().getValue();
                            BigDecimal startingBalance = o2.getEndingBalance().getValue().subtract(o2.getAmount().getValue());
                            BigDecimal difference = endingBalance.subtract(startingBalance);
                            return Triplet.with(o1, o2, difference);
                        }
                ).collect(toList());

        differences.stream().filter(difference ->
                difference.getValue2().compareTo(ZERO) != 0
        ).forEach(diff -> {
                    log.info("fst {}", diff.getValue0());
                    log.info("snd {}", diff.getValue1());
                }
        );

        List<Triplet<Operation, Operation, BigDecimal>> inconsistencies =
                differences.stream().filter(difference ->
                        difference.getValue2().compareTo(ZERO) != 0
                ).collect(toList());

        log.info("Number of inconsistencies: {}", inconsistencies.size());
    }
}
