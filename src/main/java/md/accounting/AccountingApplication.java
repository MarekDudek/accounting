package md.accounting;

import lombok.extern.slf4j.Slf4j;
import md.accounting.ipko.IPkoDataConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Path;

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
        if (args.length != 2)
        {
            throw new IllegalArgumentException(format("Program requires exactly two arguments and it got %d", args.length));
        }

        Path input = new File(args[0]).toPath();
        Path output = new File(args[1]).toPath();

        IPkoDataConverter.convert(input, output);
    }
}
