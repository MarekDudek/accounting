package md.accounting.ipko;

import md.accounting.ipko.domain.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

final class DeserTest
{
    private static final AccountHistory HISTORY =
            AccountHistory.builder().
                    search(
                            Search.builder().
                                    account("012345678901234567890123456789").
                                    date(
                                            Date.builder().
                                                    since(LocalDate.of(2009, 2, 1)).
                                                    to(LocalDate.of(2022, 4, 14)).
                                                    build()
                                    ).
                                    filtering("Wszystkie").
                                    build()
                    ).
                    operations(
                            Operations.builder().
                                    operation(
                                            singletonList(
                                                    Operation.builder().
                                                            execDate(LocalDate.of(2022, 4, 14)).
                                                            orderDate(LocalDate.of(2022, 4, 13)).
                                                            type("Płatność kartą").
                                                            description("Tytuł: 000483849 74838490103119150532000\nLokalizacja: Kraj: POLSKA Miasto: Wroclaw Adres: SHELL 08\nData i czas operacji: 2022-04-13\nOryginalna kwota operacji: 21,98 PLN\nNumer karty: 425167******5510").
                                                            amount(
                                                                    Amount.builder().
                                                                            currency("PLN").
                                                                            value(new BigDecimal("-21.98")).
                                                                            build()
                                                            ).
                                                            endingBalance(
                                                                    EndingBalance.builder().
                                                                            currency("PLN").
                                                                            value(new BigDecimal("+44174.40")).
                                                                            build()
                                                            ).
                                                            build()
                                            )
                                    ).
                                    build()
                    ).
                    build();

    @Test
    void serialize() throws Exception
    {
        Deser deser = new Deser();
        deser.serialize(HISTORY, "./target/account-history.xml");
    }

    @Test
    void deserialize() throws Exception
    {
        Deser deser = new Deser();
        AccountHistory accountHistory = deser.deserialize("./src/test/resources/account-history.xml");
        assertThat(accountHistory).isNotNull();
        List<Operation> operations = accountHistory.getOperations().getOperation();
        assertThat(operations).hasSize(2);
    }
}
