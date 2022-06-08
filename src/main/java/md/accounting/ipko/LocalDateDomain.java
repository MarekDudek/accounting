package md.accounting.ipko;

import com.google.common.collect.DiscreteDomain;

import javax.annotation.Nonnull;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public final class LocalDateDomain extends DiscreteDomain<LocalDate>
{
    public static final LocalDateDomain LocalDates = new LocalDateDomain();

    @Override
    public LocalDate next(LocalDate value)
    {
        return value.plusDays(1);
    }

    @Override
    public LocalDate previous(LocalDate value)
    {
        return value.minusDays(1);
    }

    @Override
    public long distance(@Nonnull LocalDate start, @Nonnull LocalDate end)
    {
        return DAYS.between(start, end);
    }
}
