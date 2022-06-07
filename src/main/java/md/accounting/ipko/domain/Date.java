package md.accounting.ipko.domain;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
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
public class Date
{
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate since;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate to;
}
