package md.accounting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class AccountingApplication implements CommandLineRunner
{
    public static void main(String[] args)
    {
        SpringApplication.run(AccountingApplication.class, args);
    }

    @Override
    public void run(String... args)
    {
        log.info("Running with arguments");
        Stream.of(args).forEach(arg -> log.info("{}", arg));
    }
}
