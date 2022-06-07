package md.accounting.ipko;

import md.accounting.ipko.domain.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Collections.singletonList;

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
    @Disabled
    void marshall_to_file() throws Exception
    {
        Deser deser = new Deser();
        deser.serialize(HISTORY, "./target/account-history.xml");
    }
}
