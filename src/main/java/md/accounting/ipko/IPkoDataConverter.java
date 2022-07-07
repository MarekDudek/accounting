package md.accounting.ipko;

import com.google.common.collect.RangeSet;
import lombok.extern.slf4j.Slf4j;
import md.accounting.ipko.domain.AccountHistory;
import md.accounting.ipko.domain.Operation;
import org.javatuples.Triplet;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Slf4j
public enum IPkoDataConverter
{
    ;

    public static void convert(Path input, Path output) throws IOException
    {
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

        Files.write(output, inserts);
    }
}
