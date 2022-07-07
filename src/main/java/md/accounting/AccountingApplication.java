package md.accounting;

import com.google.common.collect.RangeSet;
import lombok.extern.slf4j.Slf4j;
import md.accounting.ipko.DataExtractor;
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

import static java.lang.String.format;

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

        List<AccountHistory> histories = DataExtractor.read(directory);
        log.info("Read {} history files", histories.size());

        @SuppressWarnings("UnstableApiUsage")
        RangeSet<LocalDate> set = DataExtractor.ranges(histories);
        log.info("Search date ranges {}", set);

        if (!DataExtractor.isValid(set))
        {
            throw new IllegalArgumentException(format("Range is not valid: %s", set));
        }

        List<Operation> operations = DataExtractor.extract(histories);
        log.info("Read {} operations", operations.size());

        List<Triplet<Operation, Operation, BigDecimal>> differences = DataExtractor.differences(operations);
        List<Triplet<Operation, Operation, BigDecimal>> inconsistencies = DataExtractor.inconsistencies(differences);

        if (!DataExtractor.noInconsistencies(inconsistencies))
        {
            throw new IllegalArgumentException(format("There are inconsistencies: %s", inconsistencies));
        }
    }
}
