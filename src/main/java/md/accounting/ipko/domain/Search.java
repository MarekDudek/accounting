package md.accounting.ipko.domain;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.xml.bind.annotation.XmlAccessType.FIELD;

@XmlRootElement
@XmlAccessorType(FIELD)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Search
{
    @XmlElement(required = true)
    private String account;

    @XmlElement(required = true)
    private Date date;

    @XmlElement(required = true)
    private String filtering;
}
