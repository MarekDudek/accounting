package md.accounting.ipko.domain;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import md.accounting.ipko.xml.LocalDateAdapter;

import java.time.LocalDate;

import static jakarta.xml.bind.annotation.XmlAccessType.FIELD;

@XmlRootElement
@XmlAccessorType(FIELD)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Operation
{
    @XmlElement(name = "exec-date", required = true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate execDate;

    @XmlElement(name = "order-date", required = true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate orderDate;

    @XmlElement(required = true)
    private String type;

    @XmlElement(required = true)
    private String description;

    @XmlElement(required = true)
    private Amount amount;

    @XmlElement(required = true)
    private EndingBalance endingBalance;
}
