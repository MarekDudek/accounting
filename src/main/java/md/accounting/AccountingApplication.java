package md.accounting;

import lombok.extern.slf4j.Slf4j;
import md.accounting.ipko.Deser;
import md.accounting.ipko.domain.AccountHistory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

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
        Path directory = new File(args[0]).toPath();
        log.info("Listing directory {}", directory);
        List<Path> files =
                Files.list(directory).
                        sorted().
                        collect(Collectors.toList());
        Deser deser = new Deser();
        files.forEach(file -> {
                    log.info("- {}", file);
                    AccountHistory accountHistory = deser.deserialize(file.toString());
                    log.info("  deserialized {} operations", accountHistory.getOperations().getOperation().size());
                }
        );
    }
}
