package md.accounting;

import com.google.common.collect.RangeSet;
import lombok.extern.slf4j.Slf4j;
import md.accounting.ipko.IPkoDataReader;
import md.accounting.ipko.PostgreSqlInsertsWriter;
import md.accounting.ipko.domain.AccountHistory;
import md.accounting.ipko.domain.Operation;
import org.javatuples.Triplet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;
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
        if (args.length != 2)
        {
            throw new IllegalArgumentException(format("Program requires exactly two arguments and it got %d", args.length));
        }

        Path input =
                new File(args[0]).toPath();
        log.info("Reading from directory {}", input);

        List<AccountHistory> histories =
                IPkoDataReader.read(input);
        log.info("Read {} history files", histories.size());

        @SuppressWarnings("UnstableApiUsage")
        RangeSet<LocalDate> set =
                IPkoDataReader.ranges(histories);
        log.info("Search date ranges {}", set);

        if (!IPkoDataReader.isValid(set))
        {
            throw new IllegalArgumentException(format("Range is not valid: %s", set));
        }

        List<Operation> operations =
                IPkoDataReader.extract(histories);
        log.info("Read {} operations", operations.size());

        List<Triplet<Operation, Operation, BigDecimal>> differences =
                IPkoDataReader.differences(operations);
        List<Triplet<Operation, Operation, BigDecimal>> inconsistencies =
                IPkoDataReader.inconsistencies(differences);

        if (!IPkoDataReader.noInconsistencies(inconsistencies))
        {
            throw new IllegalArgumentException(format("There are inconsistencies: %s", inconsistencies));
        }

        List<String> inserts =
                operations.stream().map(
                        PostgreSqlInsertsWriter::createInsert
                ).collect(toList());

        Path output =
                new File(args[1]).toPath();
        Files.write(output, inserts);
    }
}
